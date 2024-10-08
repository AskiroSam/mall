<template>
goods
</template>

<script setup>
import {useRoute} from "vue-router";
import goodsApi from "@/api/goodsApi.js";
import {ref} from "vue";

const route = useRoute();

//需要显示的商品
const goods = ref({});
function selectById() {
  let id = route.query.id;

  goodsApi.selectById(id)
      .then(resp => {
        console.log(resp.data);
        goods.value = resp.data;
      })
}

selectById();
</script>

<style scoped>

</style>