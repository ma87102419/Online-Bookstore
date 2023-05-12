<style scoped>
h1 {
  margin: 1em auto;
  text-align: center;
}
button:hover {
  font-weight: 900;
}

button {
  cursor: pointer;
  border-radius: 10px;
  color: white;
  background: rgb(86, 144, 13, 0.67);
  color: white;
  text-decoration: none;
  padding: 0.3em 0.5em;
  border: 0;
}
</style>

<template>
  <div class="flow-content container light-background">
    <div style="text-align: center">
      <h1>The page you were looking for was not found.</h1>
      <router-link :to="{ name: 'home-view' }"
        ><button>Go to Home page</button></router-link
      >
    </div>
  </div>
</template>
