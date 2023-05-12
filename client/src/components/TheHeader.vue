<script setup lang="ts">
import TheHeaderDropdown from "@/components/TheHeaderDropdown.vue";
import { useCartStore } from "@/stores/cart";
const cartStore = useCartStore();
</script>

<style scoped>
header {
  display: flex;
  padding: 1em;
  background: var(--secondary-background-color);
  justify-content: space-between;
  gap: 1em;
}

.text-logo {
  font-family: var(--title-font-family);
  color: var(--primary-color);
}

header > section {
  flex-basis: 100%;
}

.search-term {
  width: 100%;
  height: 30px;
  border-radius: 25px;
  padding: 0.5rem;
  width: 100%;
  border-right: none;
  border-color: black;
}

.search-button {
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 25px;
  cursor: pointer;
  font-size: 20px;
  border-color: black;
}

.buttons {
  display: flex;
  justify-content: right;
}

.cart-button {
  color: #032238;
  width: 50px;
  height: 50px;
  text-align: center;
  margin-right: 5px;
  background-color: var(--secondary-background-color);
  border: 0;
  position: absolute;
  top: 35px;
  right: 260px;
  cursor: pointer;
}

.login-button {
  background-color: var(--primary-color);
  color: white;
  font-weight: bold;
  border: 0;
  font-size: 18px;
  width: 38px;
  height: 38px;
  text-align: center;
  border-radius: 50%;
  position: absolute;
  top: 37px;
  right: 210px;
  cursor: pointer;
}

.cart-num {
  width: 11px;
  height: 11px;
  font-size: 14px;
  /* border-radius: 50%; */
  background-color: rgb(0, 0, 0, 0);
  color: white;
  font-weight: bold;
  text-align: center;
  position: absolute;
  right: 278px;
  top: 40px;
}

a.text-logo:hover {
  color: var(--primary-color);
}

@media (max-width: 600px) {
  header {
    flex-direction: column;
  }
}
</style>

<template>
  <header class="container">
    <section class="bookstore-logo" style="text-align: left">
      <router-link to="/">
        <img
          src="/site-images/bookstore-logo.png"
          alt="My Bookstore Logo"
          width="200"
          height="auto"
        />
      </router-link>
    </section>
    <section class="title-and-search-bar" style="text-align: center">
      <h1 style="font-size: 2.8em">
        <router-link to="/" class="text-logo" style="text-decoration: none">
          My Bookstore
        </router-link>
      </h1>
      <br />
      <div class="search" style="position: relative">
        <form action="category.html">
          <input
            type="text"
            class="search-term"
            style="position: absolute; top: -5px; left: 0"
          />
          <button
            type="submit"
            class="search-button"
            style="position: absolute; top: -5px; right: 0"
          >
            <i
              class="fa-solid fa-magnifying-glass"
              style="position: absolute; top: 3px; right: 2px"
            ></i>
          </button>
        </form>
      </div>
    </section>
    <section class="header-dropdown-and-cart" style="text-align: right">
      <div class="buttons">
        <router-link to="/cart">
          <div class="shopping-cart" style="color: rgb(0, 0, 0, 0)">
            <button class="cart-button">
              <i class="fa-sharp fa-solid fa-cart-shopping fa-2x"></i>
            </button>
            <div class="cart-num" style="cursor: pointer">
              {{ cartStore.count }}
            </div>
            >
          </div></router-link
        >
        <router-link to=""
          ><button class="login-button">KM</button></router-link
        >
      </div>
      <the-header-dropdown></the-header-dropdown>
    </section>
  </header>
</template>
