<script setup lang="ts">
import ConfirmationTable from "@/components/ConfirmationTable.vue";

import { useCartStore } from "@/stores/cart";
import { useOrderDetailsStore } from "@/stores/orderDetails";

import { computed } from "vue";
import { asDollarsAndCents } from "@/main";
import HomeView from "./HomeView.vue";

const cartStore = useCartStore();
const cart = cartStore.cart;
const orderDetailsStore = useOrderDetailsStore();
const orderDetails = orderDetailsStore.OrderDetails;

const orderDate = computed(function () {
  let date = new Date(orderDetails.order.dateCreated);
  return date.toLocaleString();
});

const ccExpDate = computed(function (): Date {
  return new Date(orderDetails.customer.ccExpDate);
});

const ccExpYear = computed(function (): number {
  return ccExpDate.value.getFullYear();
});

const ccExpMonth = computed(function (): string {
  let month = ccExpDate.value.getMonth() + 1;
  let result = "" + month;
  if (month < 10) {
    result = "0" + month;
  }
  return result;
});

const maskedCCNumber = computed(function (): string {
  let ccNumber = orderDetails.customer.ccNumber;
  const maskedChars = ccNumber.length - 4;
  const maskedString =
    "*".repeat(maskedChars) + ccNumber.substring(ccNumber.length - 4);
  return maskedString;
});

const subTotal = computed(function (): number {
  let subtotal = 0;
  for (let i = 0; i < orderDetails.lineItems.length; i++) {
    let item = orderDetails.lineItems[i];
    let book = orderDetails.books[i];
    subtotal += item.quantity * book.price;
  }
  return subtotal;
});
console.log("hasOrderDetails:", orderDetailsStore.hasOrderDetails());
console.log(orderDetails);
</script>
<style scoped>
h1,
h3 {
  margin: 1em auto;
  text-align: center;
}
.info {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
}

.price {
  text-align: right;
}

.variable {
  color: #026452;
  font-weight: bold;
}
button:hover {
  font-weight: 900;
}

button {
  cursor: pointer;
  border-radius: 10px;
  color: white;
  background: rgb(86, 144, 13, 0.67);
  color: white;
  text-decoration: none;
  padding: 0.3em 0.5em;
  border: 0;
}
</style>

<template>
  <div class="flow-content container light-background">
    <section
      class="no-detail"
      v-if="!orderDetails || !orderDetailsStore.hasOrderDetails()"
    >
      <div style="text-align: center">
        <h1>We are sorry, the order you requested could not be found.</h1>
        <router-link :to="{ name: 'home-view' }"
          ><button>Go to Home page</button></router-link
        >
      </div>
    </section>
    <section class="confirmation" v-else>
      <h1>Thank you for your order!</h1>
      <h3>Below are your order details.</h3>
      <section class="info">
        <div class="order-info">
          <ul>
            <li>
              Confirmation #:
              <span class="variable">{{
                orderDetails.order.confirmationNumber
              }}</span>
            </li>
            <li>
              Order Time:
              <span class="variable">{{ orderDate }}</span>
            </li>
          </ul>
          <confirmation-table> </confirmation-table>
          <ul class="price">
            <li>
              Subtotal:
              <span class="variable">{{ asDollarsAndCents(subTotal) }}</span>
            </li>
            <li>
              Surcharge:
              <span class="variable">{{
                asDollarsAndCents(cart.surcharge)
              }}</span>
            </li>
            <li>------------------</li>
            <li>
              Total:
              <span class="variable">{{
                asDollarsAndCents(cart.surcharge + subTotal)
              }}</span>
            </li>
          </ul>
        </div>
        <div class="customer-info">
          <ul>
            <li>
              Name:
              <span class="variable">{{
                orderDetails.customer.customerName
              }}</span>
            </li>
            <li>
              Address:
              <span class="variable">{{ orderDetails.customer.address }}</span>
            </li>
            <li>
              Email:
              <span class="variable">{{ orderDetails.customer.email }}</span>
            </li>
            <li>
              Phone:
              <span class="variable">{{ orderDetails.customer.phone }}</span>
            </li>
            <li>
              Payment Method:
              <span class="variable">
                {{ maskedCCNumber }} ({{ ccExpMonth }}/{{ ccExpYear }})</span
              >
            </li>
          </ul>
        </div>
      </section>
    </section>
  </div>
</template>
