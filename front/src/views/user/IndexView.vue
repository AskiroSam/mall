<template>

  <!--轮播图-->
  <el-carousel height="450px">
    <el-carousel-item v-for="(banner, index) in banners" :key="index">
      <el-image :src="banner"></el-image>
    </el-carousel-item>
  </el-carousel>

  <!--广告位-->
  <div class="ad">
    <ul>
      <li><el-image src="/src/assets/ad.png" /></li>
      <li><el-image src="/src/assets/ad.png" /></li>
      <li><el-image src="/src/assets/ad.png" /></li>
      <li><el-image src="/src/assets/ad.png" /></li>
    </ul>
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
  import {ref} from "vue";
  import categoryApi from "@/api/categoryApi.js";
  import {useRouter} from "vue-router";

  const router = useRouter();

  //服务器地址
  const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
  const banners = ref([
    "/src/assets/banner/banner1.png",
    "/src/assets/banner/banner2.png",
    "/src/assets/banner/banner3.png"
  ])

  //已上架的夫分类
  const parentList = ref([]);
  //1.获取夫分类 2.上架
  function getParent() {
    const condition = {
      parentId: 0,
      status: 1,
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