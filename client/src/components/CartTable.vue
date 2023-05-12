<script setup lang="ts">
import { asDollarsAndCents } from "@/main";
import { useCartStore } from "@/stores/cart";
import { useCategoryStore } from "@/stores/category";
import type { BookItem } from "@/types";
const cartStore = useCartStore();
const categoryStore = useCategoryStore();

const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.jpeg`;
};
function bookImageUrl(imageFileName: string) {
  return new URL(`../assets/book-images/${imageFileName}`, import.meta.url)
    .href;
}
const updateCart = function (book: BookItem, quantity: number) {
  cartStore.updateBookQuantity(book, quantity);
};
</script>

<style scoped>
.cart-table {
  display: grid;
  grid-template-columns: max-content minmax(10em, 20em) repeat(3, max-content);
  row-gap: 1em;
  width: fit-content;
  margin: 0 auto;
  background-color: aliceblue;
}

ul {
  display: contents;
}

ul > li {
  display: contents;
}

.table-heading {
  background-color: #333;
  color: white;
}

.table-heading > * {
  background-color: #333;
  padding: 0.5em;
}

.heading-book {
  grid-column: 1 / 3;
}

.heading-price {
  grid-column: 3 / 5;
  text-align: right;
}

.heading-subtotal {
  text-align: right;
  grid-column: -2 / -1;
}

.cart-book-image {
  padding: 0 1em;
}

.cart-book-image > * {
  margin-left: auto;
  margin-right: 0;
}

img {
  display: block;
  width: 80px;
  height: auto;
}

.square {
  width: 100px;
  height: 100px;
}

.wide-rect {
  width: 125px;
  height: 100px;
}

.cart-book-price {
  position: relative;
  left: 20px;
  padding-left: 1em;
}

.cart-book-quantity {
  position: relative;
  left: 30px;
  padding-left: 0.8em;
  /* padding-right: 1em; */
}

.cart-book-subtotal {
  text-align: right;
  padding-left: 1em;
  padding-right: 1em;
}

/* Row separators in the table */

.line-sep {
  display: block;
  height: 2px;
  background-color: gray;
  grid-column: 1 / -1;
}

/* Increment/decrement buttons */

.icon-button {
  position: relative;
  top: 50px;
  left: -80px;
  border: none;
  cursor: pointer;
}

.inc-button {
  font-size: 1.125rem;
  color: var(--primary-color);
  margin-right: 0.5em;
  background-color: rgb(0, 0, 0, 0);
}

.inc-button:hover {
  color: var(--primary-color-dark);
}

.dec-button {
  font-size: 1.125rem;
  color: #ccc;
  background-color: rgb(0, 0, 0, 0);
}

.dec-button:hover {
  color: #aaa;
}

input[type="number"] {
  width: 4em;
}

/* select {
  background-color: var(--primary-color);
  color: white;
  border: 2px solid var(--primary-color-dark);
  border-radius: 3px;
} */
.cart-book-image {
  width: 100px;
  height: auto;
}

button {
  cursor: pointer;
}
.top {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 1em 9em;
}

.non-empty-bottom {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 1em 9em;
}

.subtotal {
  position: relative;
  left: 735px;
  bottom: 10px;
}

.checkout-b {
  border-radius: 10px;
  text-align: center;
  color: white;
  display: inline-block;
  background: #017863;
  color: white;
  text-decoration: none;
  padding: 0.3em 0.5em;
  border: 0;
}

.continue-b:hover,
.checkout-b:hover {
  font-weight: 900;
}

.continue-b {
  border-radius: 10px;
  text-align: center;
  color: white;
  display: inline-block;
  background: rgb(86, 144, 13, 0.67);
  color: white;
  text-decoration: none;
  padding: 0.3em 0.5em;
  border: 0;
}
</style>

<template>
  <div class="top">
    <div
      class="item-num"
      v-if="cartStore.count == 0"
      style="position: relative; left: 240px"
    >
      Your shopping cart is empty.
    </div>
    <div class="item-num" v-else-if="cartStore.count == 1">
      Your shopping cart contains {{ cartStore.count }} book.
    </div>
    <div class="item-num" v-else>
      Your shopping cart contains {{ cartStore.count }} books.
    </div>
    <div class="clear-cart" v-if="cartStore.count > 0">
      <button @click="cartStore.clearCart()">Clear cart</button>
    </div>
  </div>
  <div class="cart-table" v-if="cartStore.count > 0">
    <ul>
      <li class="table-heading">
        <div class="heading-book">Book</div>
        <div class="heading-price">Price / Quantity</div>
        <div class="heading-subtotal">Amount</div>
      </li>
      <template v-for="item in cartStore.cart.items" :key="item.book.bookId">
        <li>
          <div class="cart-book-image">
            <img
              :src="bookImageUrl(bookImageFileName(item.book))"
              :alt="item.book.title"
            />
          </div>
          <div class="cart-book-title">{{ item.book.title }}</div>
          <div class="cart-book-price">
            ${{ (item.book.price / 100).toFixed(2) }}
          </div>
          <div class="cart-book-quantity">
            <span class="quantity">{{ item.quantity }}</span
            >&nbsp;
            <button
              class="icon-button inc-button"
              @click="updateCart(item.book, item.quantity + 1)"
            >
              <i class="fas fa-plus-circle"></i>
            </button>
            <button
              class="icon-button dec-button"
              @click="updateCart(item.book, item.quantity - 1)"
            >
              <i class="fas fa-minus-circle"></i>
            </button>
          </div>
          <div class="cart-book-subtotal">
            ${{ ((item.book.price * item.quantity) / 100).toFixed(2) }}
          </div>
        </li>
        <li class="line-sep"></li>
      </template>
    </ul>
  </div>
  <div class="non-empty-bottom" v-if="cartStore.count > 0">
    <div class="continue">
      <router-link :to="'/category/' + categoryStore.selectedCategoryName"
        ><button class="continue-b">Continue Shopping</button></router-link
      >
    </div>
    <div class="checkout" v-if="cartStore.count > 0">
      <router-link :to="'/checkout'"
        ><button class="checkout-b">Proceed to checkout</button></router-link
      >
    </div>
  </div>
  <div
    class="empty-bottom"
    v-if="cartStore.count == 0"
    style="position: relative; left: 450px; margin-bottom: 1em"
  >
    <div class="continue">
      <router-link :to="'/category/' + categoryStore.selectedCategoryName"
        ><button class="continue-b">Continue Shopping</button></router-link
      >
    </div>
  </div>

  <div class="subtotal" v-if="cartStore.count > 0">
    Subtotal : {{ asDollarsAndCents(cartStore.subtotal) }}
  </div>
</template>
