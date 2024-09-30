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

  <!--显示搜索到的商品-->
  <div class="goodsList">
    <ul>
      <li v-for="(goods, index) in pageInfo.list" :key="index">
        <div class="pic"><el-image :src="`${SERVER_ADDR}/goods/pic/${goods.picList[0].url}`" style="width: 200px; height: 200px; margin-top: 10px" fit="contain" /></div>
        <div class="name">{{goods.name}}</div>
        <div class="dscp">{{goods.dscp}}</div>
        <div class="price">{{goods.price}}</div>
      </li>
    </ul>
    <el-row class="row-bg" justify="center" style="margin-top: 20px;">
      <el-pagination background layout="prev, pager, next"
                     v-model:current-page="pageInfo.pageNum"
                     :page-count="pageInfo.pages"
                     @update:current-page="search" />
    </el-row>
  </div>

</template>

<script setup>
import {ref, watch} from "vue";
import categoryApi from "@/api/categoryApi.js";
import goodsApi from "@/api/goodsApi.js";


//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
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
//被选中的分类id - 可能是夫分类的id，也可能是子分类的id
const categoryId = ref(null);
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

//通过路径传递的参数发生变化进行的处理
watch(props, () => {
  getParent();

})

//点击夫分类调用的函数
function selectFirstCategory(category) {
  //给选中的夫分类赋值
  firstCategorySelected.value = category;
  //只要选择某个夫分类,就要清空所有要显示的子分类
  secondCategoryList.value = [];
  //给被选中的分类id赋值
  if (firstCategorySelected.value.id) {
    categoryId.value = firstCategorySelected.value.id;
  } else {
    categoryId.value = null;
  }

  //给被选中的夫分类下的子分类赋值
  if (category.childList) {
    // secondCategoryList.value = category.childList;
    category.childList
        .filter(c => c.status == 1)
        .forEach(c => secondCategoryList.value.push(c));
  }

  //查询商品
  search(1);
}

//点击子分类调用的函数
function selectSecondCategory(category) {
  //给选中的子分类赋值
  secondCategorySelected.value = category;
  //给被选中的分类id赋值
  if (secondCategorySelected.value.id) {
    categoryId.value = secondCategorySelected.value.id;
  } else {
    categoryId.value = firstCategorySelected.value.id;
  }

  //查询商品
  search(1);
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
        //假设没有通过路径传递参数或者传递的id在夫分类中不存在
        let flag = true;
        firstCategoryList.value.forEach(c => {
          if (props.categoryId == c.id) {
            flag = false;
            selectFirstCategory(c)
          }
        });

        //假设成立
        if (flag == true) {
          //夫分类就设置选择全部
          selectFirstCategory({});
        }
      });
}

//搜索
function search(pageNum) {
  const condition = {
    status: 1,
    categoryId: categoryId.value
  };
  goodsApi.selectByPage1(condition, pageNum, 20)
      .then(resp => {
        pageInfo.value = resp.data;
      })
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

  .goodsList ul li {
    width: 224px;
    float: left;
    margin-right: 20px;
    margin-bottom: 20px;
    text-align: center;
    background-color: #EEE;
    cursor: pointer;
  }

  .goodsList ul li:hover {
    box-shadow: 0 0 20px #000;
  }

  .goodsList ul li:nth-child(5n) {
    margin-right: 0;
  }

  .name, .dscp, .price {
    line-height: 25px;
  }

  .name, .dscp {
    font-size: 16px;
    font-weight: bold;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }


</style>