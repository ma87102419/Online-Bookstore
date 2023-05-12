<script setup lang="ts">
import { useCategoryStore } from "@/stores/category";
const categoryStore = useCategoryStore();
</script>

<style scoped>
.header-dropdown {
  position: absolute;
  right: 210px;
  top: 90px;
  width: 170px;
}

.categories-button {
  background-color: #567388;
  color: white;
  padding: 0.25em 0.95em;
  font-size: 15px;
  border: 0;
}

.header-dropdown ul {
  background-color: white;
  color: black;
  display: none;
  font-size: 16.5px;
}

.header-dropdown li {
  padding: 0.25em 0.5em;
}

.header-dropdown a {
  color: black;
  text-decoration: none;
}

.header-dropdown li:hover {
  background: lightgray;
}

.header-dropdown:hover ul {
  position: absolute;
  z-index: 1;
  right: 0;
  display: block;
  min-width: 7.8em;
  cursor: pointer;
}

button.categories-button:hover {
  cursor: pointer;
  color: var(--primary-color);
}
</style>

<template>
  <div class="header-dropdown">
    <button class="button categories-button">
      <i class="fa-solid fa-caret-down">&nbsp;&nbsp;</i>CATEGORIES
    </button>
    <ul style="text-align: center">
      <li
        v-for="category in categoryStore.categoryList"
        :key="category.categoryId"
      >
        <router-link :to="'/category/' + category.name">
          {{ category.name }}
        </router-link>
      </li>
    </ul>
  </div>
</template>
