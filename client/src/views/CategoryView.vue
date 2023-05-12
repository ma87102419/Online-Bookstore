<script setup lang="ts">
import TheCategoryNav from "@/components/TheCategoryNav.vue";
import TheCategoryBookList from "@/components/TheCategoryBookList.vue";
import { useRoute } from "vue-router";
import { useBookStore } from "@/stores/book";
import { useCategoryStore } from "@/stores/category";
import { watch } from "vue";
import router from "@/router";

const route = useRoute();
const categoryStore = useCategoryStore();
const bookStore = useBookStore();

watch(
  () => route.params.name,
  (newName) => {
    bookStore.fetchBooks(newName as string).catch(() => {
      router.push({ name: "notfound-view" });
    });
  },
  { immediate: true }
);
</script>

<style scoped>
.light-background {
  background-color: var(--neutral-color);
  color: rgb(0, 0, 0);
}
</style>

<template>
  <div class="light-background">
    <the-category-nav></the-category-nav>
    <the-category-book-list> </the-category-book-list>
  </div>
</template>
