<template>

  <el-card style="margin-top: 10px">
    <!--轮播图-->
    <el-carousel height="450px">
      <el-carousel-item v-for="(banner, index) in banners" :key="index">
        <el-image :src="banner"></el-image>
      </el-carousel-item>
    </el-carousel>
  </el-card>


  <!--广告位-->
  <div class="ad">
    <el-carousel :interval="4000" type="card" height="200px">
      <el-carousel-item v-for="item in banners2" :key="item">
        <el-image :src="item"></el-image>
      </el-carousel-item>
    </el-carousel>
  </div>

  <!--推荐的商品-->
  <div class="recom" v-for="(category, index) in parentList" :key="index">
    <div class="categoryName">{{category.name}}</div>
    <div class="goodsList">
      <ul>
        <li v-for="(goods, index) in category.goodsList" :key="index" @click="toGoodsView(goods.id)">
          <div class="pic"><el-image :src="`${SERVER_ADDR}/goods/pic/${goods.picList[0].url}`" style="width: 200px; height: 200px; margin-top: 10px" fit="contain" /></div>
          <div class="name">{{goods.name}}</div>
          <div class="dscp">{{goods.dscp}}</div>
          <div class="price">{{goods.price}}</div>
        </li>
      </ul>
    </div>
  </div>


</template>

<script setup>
import {ref, watch} from "vue";
import categoryApi from "@/api/categoryApi.js";
import {useRouter} from "vue-router";
import {useGoodsStore} from "@/stores/goods.js";
import {useRoute} from "vue-router";

const router = useRouter();
const route = useRoute();

const goodsStore = useGoodsStore();
//查询条件
const categoryName = ref();
//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
const banners = ref([
  "/src/assets/banner/lb1.jpeg",
  "/src/assets/banner/lb2.jpg"
])
const banners2 = ref([
  "/src/assets/lb3.jpg",
  "/src/assets/lb4.jpeg",
  "/src/assets/lb5.jpg",
  "/src/assets/lb6.jpeg"
])

//已上架的夫分类
const parentList = ref([]);
//1.获取夫分类 2.上架
function getParent() {
  categoryName.value = goodsStore.goodsInfo;
  const condition = {
    parentId: 0,
    status: 1,
    name: categoryName.value
  }

  categoryApi.selectByPage(condition)
      .then(resp => {
        parentList.value = resp.data;
      })
}

//跳转到商品详情页面
function toGoodsView(id) {
  router.push({
    path: '/user/goods',//跳转到的位置，值和页面路径中配置的路径相同
    query: {
      id
    }
  });
}

// 监听路由变化
watch(
    () => goodsStore.goodsInfo,
    () => {
      getParent(); // 重新获取数据
    }
);

getParent();
</script>

<style scoped>
  .ad {
    margin: 20px 0;
  }

  .ad ul li {
    float: left;
    width: 285px;
    margin-right: 20px;
  }

  .ad ul li:nth-child(4n) {
    margin-right: 0;
  }

  .recom {
    margin-bottom: 20px;
  }

  .recom .goodsList ul li {
    width: 224px;
    float: left;
    margin-right: 20px;
    margin-bottom: 20px;
    text-align: center;
    background-color: #EEE;
    cursor: pointer;
  }

  .recom .goodsList ul li:hover {
    box-shadow: 0 0 20px #000;
  }

  .recom .goodsList ul li:nth-child(5n) {
    margin-right: 0;
  }

  .recom .categoryName {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 5px;
  }

  .name, .dscp, .price {
    line-height: 25px;
  }

  .recom .name {
    font-size: 16px;
    font-weight: bold;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .recom .dscp {
    color: #AAA;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .recom .price {
    font-size: 12px;
    color: var(--theme-color);
  }

</style>