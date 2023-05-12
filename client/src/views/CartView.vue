<script setup lang="ts">
import CartTable from "@/components/CartTable.vue";
</script>
<style scoped>
h1 {
  margin: 1em auto;
  text-align: center;
}
</style>

<template>
  <div class="flow-content container light-background">
    <h1>Cart Page</h1>
    <cart-table></cart-table>
  </div>
</template>
