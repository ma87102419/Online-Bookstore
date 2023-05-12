package business.order;

import api.ApiException;
import business.BookstoreDbException;
import business.JdbcUtils;
import business.book.Book;
import business.book.BookDao;
import business.cart.ShoppingCart;
import business.cart.ShoppingCartItem;
import business.customer.Customer;
import business.customer.CustomerDao;
import business.customer.CustomerForm;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {

	private BookDao bookDao;
	private LineItemDao lineItemDao;
	private OrderDao orderDao;
	private CustomerDao customerDao;

	public void setLineItemDao(LineItemDao lineItemDao) {
		this.lineItemDao = lineItemDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}

	@Override
	public OrderDetails getOrderDetails(long orderId) {
		Order order = orderDao.findByOrderId(orderId);
		Customer customer = customerDao.findByCustomerId(order.getCustomerId());
		List<LineItem> lineItems = lineItemDao.findByOrderId(orderId);
		List<Book> books = lineItems
				.stream()
				.map(lineItem -> bookDao.findByBookId(lineItem.getBookId()))
				.collect(Collectors.toList());
		return new OrderDetails(order, customer, lineItems, books);
	}

	@Override
    public long placeOrder(CustomerForm customerForm, ShoppingCart cart) {

		validateCustomer(customerForm);
		validateCart(cart);

		try (Connection connection = JdbcUtils.getConnection()) {
			Date date = getDate(
					customerForm.getCcExpiryMonth(),
					customerForm.getCcExpiryYear());
			return performPlaceOrderTransaction(
					customerForm.getName(),
					customerForm.getAddress(),
					customerForm.getPhone(),
					customerForm.getEmail(),
					customerForm.getCcNumber(),
					date, cart, connection);
		} catch (SQLException e) {
			throw new BookstoreDbException("Error during close connection for customer order", e);
		}

		// NOTE: MORE CODE PROVIDED NEXT PROJECT

		// return -1;
	}

	private Date getDate(String monthString, String yearString) {
		String dateString = yearString + "-" + monthString + "-01";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = dateFormat.parse(dateString);
			return date;
		} catch (ParseException e) {
			System.err.println("Invalid date format");
			return null;
		}

	}

	private long performPlaceOrderTransaction(
			String name, String address, String phone,
			String email, String ccNumber, Date date,
			ShoppingCart cart, Connection connection) {
		try {
			connection.setAutoCommit(false);
			long customerId = customerDao.create(
					connection, name, address, phone, email,
					ccNumber, date);
			long customerOrderId = orderDao.create(
					connection,
					cart.getComputedSubtotal() + cart.getSurcharge(),
					generateConfirmationNumber(), customerId);
			for (ShoppingCartItem item : cart.getItems()) {
				lineItemDao.create(connection, customerOrderId,
						item.getBookId(), item.getQuantity());
			}
			connection.commit();
			return customerOrderId;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new BookstoreDbException("Failed to roll back transaction", e1);
			}
			return 0;
		}
	}

	private int generateConfirmationNumber() {
		Random random = new Random();
		return random.nextInt(999999999);
	}

	private void validateCustomer(CustomerForm customerForm) {

    	String name = customerForm.getName();
		if (name == null || name.equals("") || name.length() < 4 ||name.length() > 45 ) {
			throw new ApiException.ValidationFailure("name", "Invalid name field");
		}

		String address = customerForm.getAddress();
		if (address == null || address.equals("") || address.length() < 4 ||address.length() > 45 ) {
			throw new ApiException.ValidationFailure("address", "Invalid address field");
		}

		// TODO: Validation checks for address, phone, email, ccNumber

		String phone = customerForm.getPhone();
		if (phone == null || phone.equals("") || phone.replaceAll("\\D", "").length() != 10) {
			throw new ApiException.ValidationFailure("phone", "Invalid phone field");
		}

		String email = customerForm.getEmail();
		if (email == null || email.equals("") || email.contains(" ") || !email.contains("@") || email.charAt(email.length() - 1) == '.') {
			throw new ApiException.ValidationFailure("email", "Invalid email field");
		}

		String ccNumber = customerForm.getCcNumber();
		if (ccNumber == null || ccNumber.equals("") || ccNumber.replaceAll("\\D", "").length() < 14 || ccNumber.replaceAll("\\D", "").length() > 16) {
			throw new ApiException.ValidationFailure("ccNumber", "Invalid ccNumber field");
		}

		String ccExpiryMonth = customerForm.getCcExpiryMonth();
		if (ccExpiryMonth == null || ccExpiryMonth.equals("") || Integer.parseInt(ccExpiryMonth) < 1 || Integer.parseInt(ccExpiryMonth) > 12) {
			throw new ApiException.ValidationFailure("ccExpiryMonth", "Invalid ccExpiryMonth field");
		};

		String ccExpiryYear = customerForm.getCcExpiryYear();
		if (ccExpiryYear == null || ccExpiryYear.equals("")) {
			throw new ApiException.ValidationFailure("ccExpiryYear", "Invalid ccExpiryYear field");
		}

		if (expiryDateIsInvalid(customerForm.getCcExpiryMonth(), customerForm.getCcExpiryYear())) {
			throw new ApiException.ValidationFailure("Please enter a valid expiration date");

		}
	}

	private boolean expiryDateIsInvalid(String ccExpiryMonth, String ccExpiryYear) {

		// TODO: return true when the provided month/year is before the current month/yeaR
		// HINT: Use Integer.parseInt and the YearMonth class
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth expireYearMonth = YearMonth.of(Integer.parseInt(ccExpiryYear), Integer.parseInt(ccExpiryMonth));
		return expireYearMonth.isBefore((currentYearMonth));

	}

	private void validateCart(ShoppingCart cart) {

		if (cart.getItems().size() == 0) {
			throw new ApiException.ValidationFailure("Cart is empty.");
		}

		cart.getItems().forEach(item-> {
			if (item.getQuantity() < 0 || item.getQuantity() > 99) {
				throw new ApiException.ValidationFailure("Invalid quantity");
			}
			Book databaseBook = bookDao.findByBookId(item.getBookId());
			// TODO: complete the required validations
			if (item.getBookForm().getPrice() != databaseBook.getPrice()) {
				throw new ApiException.ValidationFailure("Invalid price");
			}
			if (item.getBookForm().getCategoryId() != databaseBook.getCategoryId()) {
				throw new ApiException.ValidationFailure("Invalid category");
			}
		});
	}

}
