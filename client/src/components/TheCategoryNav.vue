<script setup lang="ts">
import { useCategoryStore } from "@/stores/category";
const categoryStore = useCategoryStore();
</script>

<style scoped>
.category-buttons {
  display: flex;
  flex-direction: row;
  text-align: center;
  background-color: var(--card-background-color);
  justify-content: space-between;
  align-items: center;
  color: #017863;
  padding: 0px 5px;
}

.button.category-button.router-link-active {
  background-color: var(--select-category-color);
}

.button.category-button {
  background-color: rgb(0, 0, 0, 0);
  line-height: 14px;
  border-radius: 25px;
  border-color: #017863;
  height: 30px;
  border-style: solid;
  color: black;
  font-size: 16px;
}

.button.category-button:hover,
.button.category-button:active {
  background-color: var(--select-category-color);
  color: #017863;
}

.category-nav {
  background-color: var(--card-background-color);
}

a.text-logo:hover {
  color: var(--primary-color);
}
</style>

<template>
  <nav class="category-nav">
    <ul class="category-buttons">
      <li
        v-for="category in categoryStore.categoryList"
        :key="category.categoryId"
      >
        <router-link
          :to="'/category/' + category.name"
          class="button category-button"
        >
          {{ category.name }}
        </router-link>
      </li>
    </ul>
  </nav>
</template>
