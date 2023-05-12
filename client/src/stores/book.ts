import { defineStore } from "pinia";
import type { BookItem } from "@/types";
import { apiUrl } from "@/api";
import { useCategoryStore } from "@/stores/category";

export const useBookStore = defineStore("book", {
  state: () => ({
    bookList: [] as BookItem[],
  }),
  actions: {
    async fetchBooks(categoryName: string) {
      const categoryStore = useCategoryStore();

      const selectedCategoryName =
        categoryStore.categoryList?.find(
          (category) => category.name === categoryName
        )?.name || categoryName;

      const response = await fetch(
        `${apiUrl}/categories/name/${selectedCategoryName}/books`
      );
      if (!response.ok) {
        throw new Error("Failed to load the books.");
      }
      this.bookList = (await response.json()) as BookItem[];
    },
  },
  // getters
});
