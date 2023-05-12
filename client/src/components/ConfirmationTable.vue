<script setup lang="ts">
import { asDollarsAndCents } from "@/main";
import { useOrderDetailsStore } from "@/stores/orderDetails";
import type { BookItem, OrderDetails } from "@/types";

const orderDetailsStore = useOrderDetailsStore();
const orderDetails: OrderDetails = orderDetailsStore.OrderDetails;

// A helper function - optional to use
const bookAt = function (orderDetails: OrderDetails, index: number): BookItem {
  return orderDetails.books[index];
};
</script>

<style scoped>
table {
  border: 1px black solid;
  width: 450px;
  margin-top: 1em;
  margin-bottom: 1em;
}

td {
  display: table-cell;
  padding: 0.5em 0.5em;
  vertical-align: middle;
}

.table-body {
  background-color: #c7e0e7;
}

td:nth-child(1) {
  text-align: left;
}

td:nth-child(2) {
  text-align: center;
}

td:nth-child(3) {
  text-align: right;
}

.table-heading {
  background-color: #333;
  color: white;
}
</style>

<template>
  <table>
    <thead class="table-heading">
      <tr>
        <th>Book title</th>
        <th>Quantity</th>
        <th>Price</th>
      </tr>
    </thead>
    <tbody class="table-body">
      <tr v-for="(item, index) in orderDetails.lineItems" :key="item.productId">
        <td>
          {{ orderDetails.books[index].title }}
        </td>
        <td>{{ item.quantity }}</td>
        <td>{{ asDollarsAndCents(orderDetails.books[index].price) }}</td>
      </tr>
    </tbody>
  </table>
</template>
