<template>
Cart
</template>

<script setup>
import {ref} from "vue";
import cartApi from "@/api/cartApi.js";

//当前用户所有的购物车信息
const cartList = ref([]);
//查询当前用户所有的购物车信息
function selectCartList() {
  cartApi.selectCartList()
      .then(resp => {
        cartList.value = resp.data;
      });
}

selectCartList();
</script>

<style scoped>

</style>