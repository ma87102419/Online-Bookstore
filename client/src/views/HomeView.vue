<script setup lang="ts">
function bookImageUrl(imageFileName: string) {
  return new URL(`../assets/book-images/${imageFileName}`, import.meta.url)
    .href;
}
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  text-align: center;
  width: 100%;
}

.light-background {
  background-color: var(--neutral-color);
  color: rgb(0, 0, 0);
}

.welcome-text {
  width: 100%;
  text-align: center;
}

.category-image-items {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 1em;
}

.category-image-items a {
  text-align: center;
  cursor: pointer;
}

.book-image-items {
  display: flex;
  flex-direction: row;
  gap: 20px;
  justify-content: center;
  align-items: center;
  padding: 0.5em 0;
  transform: translateY(-2.25em);
  margin-bottom: -2em;
}

.book-image-items > div {
  margin: 10px;
  cursor: pointer;
}

.CTA-button {
  width: 200px;
  height: auto;
  border-radius: 10px;
  text-align: center;
  font-size: 1.5rem;
  display: inline-block;
  background: #017863;
  color: white;
  text-decoration: none;
  padding: 0.5em 1em;
  cursor: pointer;
  border: 0;
}

button.CTA-button:hover {
  color: rgb(191, 255, 0);
}
</style>
<template>
  <div class="home-page flow-content container light-background">
    <section class="welcome-text">
      <h2 style="font-size: 1.8rem; font-weight: bold">
        Welcome to My Bookstore!
      </h2>
      <br />
      <p style="font-size: 1.25rem">
        We hope to make <strong>My Bookstore</strong> your bookstore!
      </p>
      <br />
      <router-link :to="'/category/Mystery'"
        ><button class="CTA-button">SHOP BOOKS!</button></router-link
      >
      <br /><br />
      <p style="font-size: 1.25rem">
        Why not try something from <strong>Mystery</strong>?
      </p>
    </section>
    <section></section>
    <section></section>
    <section class="book-image-items">
      <div>
        <router-link :to="'/category/Mystery'"
          ><img
            :src="bookImageUrl('book1.jpeg')"
            :alt="'The Silent Patient'"
            style="width: 85%; height: auto"
        /></router-link>
      </div>
      <div>
        <router-link :to="'/category/Mystery'"
          ><img
            :src="bookImageUrl('book2.jpeg')"
            :alt="'A good girl\'s guide to murder'"
            style="width: 85%; height: auto"
        /></router-link>
      </div>
      <div>
        <router-link :to="'/category/Mystery'"
          ><img
            :src="bookImageUrl('book3.jpeg')"
            :alt="'The Perfect Marriage'"
            style="width: 85%; height: auto"
        /></router-link>
      </div>
    </section>
  </div>
</template>
