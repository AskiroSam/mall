<template>
<div class="category">
  <!--显示所有上架的夫分类-->
  <ul>
    <li @click="selectFirstCategory({})" :class="{active: !firstCategorySelected.id}">全部</li>
    <li v-for="(category, index) in firstCategoryList"
        :key="index"
        :class="{active: category.id == firstCategorySelected.id}"
        @click="selectFirstCategory(category)">{{category.name}}</li>
  </ul>
  <!--显示夫分类下的子分类-->
  <ul v-if="firstCategorySelected.id">
    <li @click="selectSecondCategory({})" :class="{active: !secondCategorySelected.id}">全部</li>
    <li v-for="(category, index) in secondCategoryList"
        :key="index"
        :class="{active: category.id == secondCategorySelected.id}"
        @click="selectSecondCategory(category)">{{category.name}}</li>
  </ul>
</div>
</template>

<script setup>
import {ref, watch} from "vue";
import categoryApi from "@/api/categoryApi.js";

//获取通过路径传递的参数
const props = defineProps({
  categoryId: String
});
//所有需要显示的夫分类
const firstCategoryList = ref([]);
//所有要显示的子分类
const secondCategoryList = ref([]);
//被选中的夫分类
const firstCategorySelected = ref({});
//被选中的子分类
const secondCategorySelected = ref({});

//通过路径传递的参数发生变化进行的处理
watch(props, () => {
  console.log(props.categoryId);

})

//点击夫分类调用的函数
function selectFirstCategory(category) {
  //给选中的夫分类赋值
  firstCategorySelected.value = category;
  //只要选择某个夫分类,就要清空所有要显示的子分类
  secondCategoryList.value = [];

  //给被选中的夫分类下的子分类赋值
  if (category.childList) {
    // secondCategoryList.value = category.childList;
    category.childList
        .filter(c => c.status == 1)
        .forEach(c => secondCategoryList.value.push(c));
  }
}

//点击子分类调用的函数
function selectSecondCategory(category) {
  //给选中的子分类赋值
  secondCategorySelected.value = category;
}

//获取夫分类 上架-推荐
function getParent() {
  const condition = {
    parentId: 0,
    recom: 1,
    status: 1,
  }

  categoryApi.selectByPage(condition)
      .then(resp => {
        firstCategoryList.value = resp.data;
      });
}

getParent();
</script>

<style scoped>
  .category ul li {
    float: left;
    margin: 10px 20px;
    font-size: 14px;
    cursor: pointer;
  }

  .active {
    color: var(--theme-color);
    font-weight: bold;
  }
</style>