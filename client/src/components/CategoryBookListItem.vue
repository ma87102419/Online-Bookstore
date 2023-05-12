<script setup lang="ts">
import { defineProps } from "vue";
import type { BookItem } from "@/types";
import { useCartStore } from "@/stores/cart";
const cartStore = useCartStore();
const props = defineProps<{
  book: BookItem;
}>();
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
</script>
<style scoped>
#book-boxes {
  display: flex;
  flex-wrap: wrap;
  padding: 1.1em;
  gap: 1em;
  justify-content: space-between;
}

.book-box {
  display: flex;
  flex-direction: row;
  background-color: var(--card-background-color);
  padding: 1em;
  gap: 0.25em;
  width: 300px;
  height: 200px;
  margin-bottom: 1em;
  justify-content: center;
}

.book-info {
  display: flex;
  flex-direction: column;
  position: relative;
  top: 8px;
  left: 5px;
}

.book-info > button {
  width: 90px;
  background-color: #017863;
  border-radius: 10px;
  border-style: none;
  padding: 3px;
  font-size: 20px;
  position: relative;
  top: 22px;
  left: -10px;
}

.book-image {
  width: 100px;
  height: 200px;
  position: relative;
  left: -10px;
  top: 8px;
}

.read-now {
  padding: 0.8em;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background-color: rgb(158, 255, 0);
  color: black;
  font-size: 10px;
  font-weight: bolder;
  line-height: 10px;
  text-align: center;
  position: relative;
  /* top: -130px; */
  bottom: 173px;
  right: 40px;
  cursor: pointer;
}

.book-title {
  font-weight: bold;
  font-size: 20px;
}

.book-author {
  font-style: italic;
}

.book-price {
  font-weight: bold;
  color: #017863;
  font-size: 20px;
  position: absolute;
  top: 70px;
}

button.button:hover {
  width: 93px;
  /* height: auto; */
  /* font-size: 20.5px; */
  /* color: rgb(191, 255, 0); */
}

.book-add-msg {
  display: none;
}
.book-added {
  display: inline;
  color: rgb(158, 255, 0);
}

.book-not_added {
  display: none;
  animation: txt1 1.5s ease-in-out forwards;
}

@keyframes txt1 {
  0% {
    opacity: 1;
  }
  20%,
  100% {
    opacity: 0;
  }
}
</style>

<script lang="ts">
export default {
  data: function () {
    return {
      added: false,
      not_added: false,
    };
  },
  methods: {
    addButton() {
      if (this.added) {
        return;
      }
      this.added = true;
      this.not_added = true;
      setTimeout(
        function (this: any) {
          this.added = false;
          this.not_added = false;
        }.bind(this),
        1000
      );
    },
  },
};
</script>

<template>
  <section class="category-book-list">
    <ul id="book-boxes">
      <li class="book-box">
        <div class="book-image">
          <img
            :src="bookImageUrl(bookImageFileName(props.book))"
            :alt="book.title"
          />
        </div>
        <div class="book-info">
          <div class="book-title">{{ book.title }}</div>
          <div class="book-author">{{ book.author }}</div>
          <br />
          <div class="book-price">${{ (book.price / 100).toFixed(2) }}</div>
          <button
            class="button"
            @click="
              addButton();
              cartStore.addToCart(book);
            "
            :disabled="added"
          >
            <span class="cart-msg" :class="{ 'book-not_added': not_added }"
              ><i class="fa-sharp fa-solid fa-cart-plus">&nbsp;&nbsp;</i
              >Add</span
            >
            <span class="book-add-msg" :class="{ 'book-added': added }"
              >Added!</span
            >
          </button>
          > >
          <div class="read-now" v-if="book.isPublic">READ NOW</div>
        </div>
      </li>
    </ul>
  </section>
</template>
