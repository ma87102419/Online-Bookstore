<script setup lang="ts"></script>

<style scoped>
footer {
  background: #567388;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 1em;
}

.links {
  position: absolute;
  left: 650px;
}
.links > a {
  margin: 5px;
}

.social-media-icons > a {
  margin: 5px;
  gap: 0.25em;
}
</style>

<template>
  <footer class="container">
    <section class="copyright">
      <i class="fa-regular fa-copyright">&nbsp;</i>2023 My Bookstore
    </section>
    <section class="links">
      <router-link to="/">about</router-link>
      <router-link to="/">contact</router-link>
      <router-link to="/">directions</router-link>
    </section>
    <section class="social-media-icons">
      <router-link to="/" class="social-button"
        ><i class="fa-brands fa-square-facebook fa-2x"></i
      ></router-link>
      <router-link to="/" class="social-button"
        ><i class="fa-brands fa-square-twitter fa-2x"></i
      ></router-link>
    </section>
  </footer>
</template>
