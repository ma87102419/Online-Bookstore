<script setup lang="ts">
import { reactive, ref } from "vue";
import useVuelidate from "@vuelidate/core";
import type { OrderDetails, ServerErrorResponse } from "@/types";
import {
  helpers,
  maxLength,
  minLength,
  required,
  email,
} from "@vuelidate/validators";
import { useCartStore } from "@/stores/cart";
import { isMobilePhone, isCreditCard } from "@/validators";
import { asDollarsAndCents } from "@/main";
import { useCategoryStore } from "@/stores/category";
import CheckoutFieldError from "@/components/CheckoutFieldError.vue";
import router from "@/router";

const categoryStore = useCategoryStore();
const cartStore = useCartStore();
const cart = cartStore.cart;
const defaultServerErrorMessage =
  "An unexpected error occurred, please try again.";
const serverErrorMessage = ref(defaultServerErrorMessage);

const months: string[] = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const form = reactive({
  name: "",
  address: "",
  phone: "",
  email: "",
  ccNumber: "",
  ccExpiryMonth: new Date().getMonth() + 1,
  ccExpiryYear: new Date().getFullYear(),
  checkoutStatus: "",
});

const rules = {
  name: {
    required: helpers.withMessage("Please provide a name.", required),
    minLength: helpers.withMessage(
      "Name must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Name can have at most 45 letters.",
      maxLength(45)
    ),
  },
  address: {
    required: helpers.withMessage("Please provide an address.", required),
    minLength: helpers.withMessage(
      "Address must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Address can have at most 45 letters.",
      maxLength(45)
    ),
  },
  phone: {
    required: helpers.withMessage("Please provide a phone number.", required),
    phone: helpers.withMessage(
      "Please provide a valid phone number.",
      (value: string) => !helpers.req(value) || isMobilePhone(value)
    ),
  },
  email: {
    required: helpers.withMessage("Please provide an email.", required),
    email: helpers.withMessage("Please provide a valid email.", email),
  },
  ccNumber: {
    required: helpers.withMessage(
      "Please provide a credit card number.",
      required
    ),
    ccNumber: helpers.withMessage(
      "Please provide a valid credit card number.",
      (value: string) => !helpers.req(value) || isCreditCard(value)
    ),
  },
  ccExpiryMonth: {},
  //   TODO: Add more validations for these and other fields that need more validation.
};
const v$ = useVuelidate(rules, form);

async function submitOrder() {
  console.log("Submit order");
  const isFormCorrect = await v$.value.$validate();
  if (!isFormCorrect) {
    form.checkoutStatus = "ERROR";
  } else {
    try {
      form.checkoutStatus = "PENDING";
      serverErrorMessage.value = defaultServerErrorMessage;

      const placeOrderResponse: OrderDetails | ServerErrorResponse =
        await cartStore.placeOrder({
          name: form.name,
          address: form.address,
          phone: form.phone,
          email: form.email,
          ccNumber: form.ccNumber,
          ccExpiryMonth: form.ccExpiryMonth,
          ccExpiryYear: form.ccExpiryYear,
        });

      if ("error" in placeOrderResponse) {
        form.checkoutStatus = "SERVER_ERROR";
        serverErrorMessage.value = (
          placeOrderResponse as ServerErrorResponse
        ).message;
        console.log("Error placing order", placeOrderResponse);
      } else {
        form.checkoutStatus = "OK";
        await router.push({ name: "confirmation-view" });
      }
    } catch (e) {
      form.checkoutStatus = "SERVER_ERROR";
      serverErrorMessage.value = defaultServerErrorMessage;
      console.log("Error placing order", e);
    }
  }
}

/* NOTE: For example yearFrom(0) == <current_year> */
function yearFrom(index: number) {
  return new Date().getFullYear() + index;
}
</script>

<style scoped>
h1 {
  margin: 1em auto;
  text-align: center;
}
.checkout-page-body {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1em;
}

.continue-b:hover {
  font-weight: 900;
}

.continue-b {
  cursor: pointer;
  border-radius: 10px;
  /* text-align: center; */
  color: white;
  /* display: inline-block; */
  background: rgb(86, 144, 13, 0.67);
  color: white;
  text-decoration: none;
  padding: 0.3em 0.5em;
  border: 0;
}

.checkout-details {
  display: flex;
  justify-content: center;
  align-items: center;
}

form {
  /* display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center; */
  margin-right: 2.5em;
}

form > div {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0.5em;
}

form > div > input,
form > div > select {
  background-color: #666666;
  margin-left: 0.5em;
}

.exp-date {
  display: flex;
  flex-direction: row;
}

.err-msg {
  display: flex;
  flex-direction: column;
}

.error {
  color: red;
  text-align: right;
  font-size: 0.75em;
}

.checkoutStatusBox {
  margin: 1em;
  padding: 1em;
  text-align: center;
}

.purchase-button {
  cursor: pointer;
  border-radius: 10px;
  text-align: center;
  color: white;
  display: inline-block;
  background: #017863;
  color: white;
  text-decoration: none;
  padding: 0.3em 0.5em;
  border: 0;
  position: relative;
  left: 220px;
  top: 10px;
}

.purchase-button:hover {
  font-weight: 900;
}

b {
  font-weight: bolder;
}
</style>

<template>
  <div class="flow-content container light-background">
    <h1>Checkout Page</h1>
    <section class="checkout-page-body" v-if="cart.empty">
      <div style="position: relative; left: 100px; top: -40px">
        Add an item to your cart to chekout!
      </div>
      <div
        class="continue"
        style="position: relative; right: 150px; top: 60px; margin-bottom: 3em"
      >
        <router-link :to="'/category/' + categoryStore.selectedCategoryName"
          ><button class="continue-b">Continue Shopping</button></router-link
        >
      </div>
    </section>
    <section class="checkout-page-body" v-if="!cart.empty">
      <form @submit.prevent="submitOrder">
        <div>
          <label for="name">Name&nbsp;&nbsp;</label>
          <div class="err-msg">
            <input
              type="text"
              size="24"
              id="name"
              name="name"
              v-model.lazy="v$.name.$model"
            />
            <span class="error">
              <CheckoutFieldError :field-name="v$.name" />
            </span>
          </div>
        </div>

        <div>
          <label for="address">Address&nbsp;&nbsp;</label>
          <div class="err-msg">
            <input
              type="text"
              size="24"
              id="address"
              name="address"
              v-model.lazy="v$.address.$model"
            />
            <span class="error">
              <CheckoutFieldError :field-name="v$.address" />
            </span>
          </div>
        </div>

        <div>
          <label for="phone">Phone&nbsp;&nbsp;</label>
          <div class="err-msg">
            <input
              class="textField"
              type="text"
              size="24"
              id="phone"
              name="phone"
              v-model.lazy="v$.phone.$model"
            />
            <span class="error">
              <CheckoutFieldError :field-name="v$.phone" />
            </span>
          </div>
        </div>

        <div>
          <label for="email">Email&nbsp;&nbsp;</label>
          <div class="err-msg">
            <input
              type="text"
              size="24"
              id="email"
              name="email"
              v-model.lazy="v$.email.$model"
            />
            <span class="error">
              <CheckoutFieldError :field-name="v$.email" />
            </span>
          </div>
        </div>

        <div>
          <label for="ccNumber">Credit card&nbsp;&nbsp;</label>
          <div class="err-msg">
            <input
              type="text"
              size="24"
              id="ccNumber"
              name="ccNumber"
              v-model.lazy="v$.ccNumber.$model"
            />
            <span class="error">
              <CheckoutFieldError :field-name="v$.ccNumber" />
            </span>
          </div>
        </div>
        <div class="exp-date">
          <div>
            <label>Exp Month&nbsp;&nbsp;</label>
            <select v-model="form.ccExpiryMonth">
              <option
                v-for="(month, index) in months"
                :key="index"
                :value="index + 1"
              >
                {{ month }} ({{ index + 1 }})
              </option>
            </select>
          </div>
          &nbsp;
          <div>
            <label>Exp Year&nbsp;&nbsp;</label>
            <select v-model="form.ccExpiryYear">
              <option v-for="(year, index) in 15" :key="index">
                {{ yearFrom(index) }}
              </option>
            </select>
          </div>
        </div>

        <input
          type="submit"
          name="submit"
          class="purchase-button"
          :disabled="form.checkoutStatus === 'PENDING'"
          value="Complete Purchase"
        />
      </form>
    </section>
    <section class="checkout-details" v-if="!cart.empty">
      Your credit card will be charged &nbsp;
      <b>{{ asDollarsAndCents(cart.subtotal + cart.surcharge) }}</b>
    </section>
    <section class="checkout-details" v-if="!cart.empty">
      (<b>{{ asDollarsAndCents(cart.subtotal) }}</b> &nbsp;+&nbsp;
      <b>{{ asDollarsAndCents(cart.surcharge) }}</b> &nbsp;shipping)
    </section>

    <section v-show="form.checkoutStatus !== ''" class="checkoutStatusBox">
      <div v-if="form.checkoutStatus === 'ERROR'" style="color: red">
        Error: Please fix the problems above and try again.
      </div>

      <div v-else-if="form.checkoutStatus === 'PENDING'">Processing...</div>

      <div v-else-if="form.checkoutStatus === 'OK'">Order placed...</div>

      <div v-else style="color: red">
        {{ serverErrorMessage }}
      </div>
    </section>
  </div>
</template>
