<template>
  <el-row>
    <el-col :span="12">
      <!--商品的图片 - 轮播图-->
      <el-carousel  v-if="goods.picList && goods.picList.length > 0" height="400px" :loop="goods.picList.length > 1" motion-blur>
        <el-carousel-item v-for="(pic, index) in goods.picList" :key="index">
          <el-image :src="`${SERVER_ADDR}/goods/pic/${pic.url}`" style="width: 600px; height: 400px;" fit="contain" />
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="12">
      <!--商品的基本信息 - 名字,颜色,版本,价格...-->
      <div class="info">
        <div class="name">{{ goods.name }}</div>
        <div class="dscp">{{ goods.dscp }}</div>
        <div class="price">售价 ￥{{ goods.price }}</div>
        <div class="markPrice">原价 ￥{{ goods.markPrice }}</div>
        <div class="color"><span>颜色 {{ goods.color }}</span></div>
        <div class="version"><span>版本 {{ goods.version }}</span></div>
        <div class="count">数量 {{ goods.count }}</div>
        <div class="score">
          <el-rate
              v-model="goods.score"
              disabled
              show-score
              text-color="#FF6800"
              :colors="['#FF6800', '#FF6800', '#FF6800']"
              score-template="{value}"
          />
        </div>
        <div class="goodsBtn">
          <el-button type="primary" size="large" @click="insertCart"><el-icon><ShoppingCart /></el-icon>加入购物车</el-button>
          <el-button type="success" size="large"><el-icon><Money /></el-icon>直接购买</el-button>
          <el-button type="info" size="large" v-if="!collectInfo" @click="collect"><el-icon><StarFilled /></el-icon>收藏</el-button>
          <el-button type="danger" size="large" v-else @click="cancelCollect"><el-icon><Star /></el-icon>取消收藏</el-button>
        </div>
      </div>
    </el-col>
  </el-row>
  <el-row>
    <!--商品的详情,商品的评价-->
    <el-tabs model-value="first" class="demo-tabs" type="border-card">
      <el-tab-pane label="商品详情" name="first" v-html="goods.detail"></el-tab-pane>
      <el-tab-pane label="商品评价" name="second" v-html="goods.detail"></el-tab-pane>
    </el-tabs>
  </el-row>
</template>

<script setup>
import {useRoute} from "vue-router";
import {useRouter} from "vue-router";
import goodsApi from "@/api/goodsApi.js";
import {ref} from "vue";
import {Money, Paperclip, ShoppingCart, Star, StarFilled} from "@element-plus/icons-vue";
import collectApi from "@/api/collectApi.js";
import CartApi from "@/api/cartApi.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {useTokenStore} from "@/stores/token.js";

const tokenStore = useTokenStore();
const route = useRoute();
const router = useRouter();
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

//需要显示的商品
const goods = ref({});
//商品收藏的状态
const collectInfo = ref(null);

//加入购物车
function insertCart() {
  CartApi.insert(goods.value.id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessageBox.confirm(
              '加入购物车成功，是否跳转到购物车页面',
              '提示',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'info',
              }
          ).then(() => {
            //点击成功之后的回调
            router.push("/user/cart");
          })
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//根据商品id查询商品的详情信息
function selectById() {
  let id = route.query.id;

  goodsApi.selectById(id)
      .then(resp => {
        goods.value = resp.data;
        //判断用户是否已经登录
        if (tokenStore.tokenStr != null) {
          //获取当前商品收藏的情况
          getCollectInfo();
        }

      });
}

//获取当前商品收藏的情况
function getCollectInfo() {
  collectApi.selectByGoodsIdAndUserId(goods.value.id)
      .then(resp => {
        collectInfo.value = resp.data;
      })
}

//收藏
function collect() {
  collectApi.insert(goods.value.id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //刷新收藏的状态
          getCollectInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      });
}
//取消收藏
function cancelCollect() {
  collectApi.delete(collectInfo.value.id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //刷新收藏的状态
          getCollectInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

selectById();
</script>

<style scoped>
.el-carousel {
  margin: 15px 0;
  border: 1px solid #AAA;
}

.info {
  margin: 30px 60px;
}

.info div {
  margin-bottom: 15px;
}

.info .name {
  font-weight: bold;
  font-size: 25px;
}

.info .dscp {
  color: #999;
}

.info .price {
  color: var(--theme-color);
  font-size: 18px;
}

.info .markPrice {
  color: #999;
  font-size: 15px;
  text-decoration: line-through; /*删除线*/
}

.info span {
  display: inline-block;
  border: 2px solid var(--theme-color);
  border-radius: 5px;
  background-color: #FFF;
  line-height: 30px;
  padding: 0 10px;
}

.info .count {
  font-size: 15px;
}

.el-icon {
  margin-right: 3px;
}

</style>