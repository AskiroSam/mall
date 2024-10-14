<template>
  <el-card v-if="order">
    <el-row class="header" justify="center">
      <div>订单号：{{order.id}}</div>
    </el-row>
    <el-row class="title" justify="center">
      <el-col>
        <div v-if="order.status === 0">未支付</div>
        <div v-if="order.status === 1">已支付</div>
        <div v-if="order.status === 3">已收获</div>
      </el-col>
    </el-row>
    <el-row v-if="order.status === 1 || order.status === 3" class="header" justify="center">
      <div style="margin-left: 10px; margin-top: 10px">谢谢惠顾~</div>
    </el-row>
    <el-row v-else class="header" justify="center">
      <div style="margin-left: 10px; margin-top: 10px">
        请先完成<div style="display: inline-block; color: var(--theme-color); cursor: pointer" @click="toCreateOrderPage(order.id)">支付</div>
      </div>
    </el-row>
    <!--订单详情开始-->
    <el-row
        style="background-color: #EEEEEE; margin-bottom: 0; padding: 10px; border: 2px solid #AAAAAA; margin-top: 10px"
        v-for="(orderDetail, index) in order.orderDetailList"
        :key="index">
      <el-col :span="3">
        <div class="pic">
          <el-image
              :src="`${SERVER_ADDR}/goods/pic/${orderDetail.goods.picList[0].url}`"
              style="width: 100px; height: 100px; margin-top: 10px; margin-left: 10px"
              fit="contain" />
        </div>
      </el-col>
      <el-col :span="4" style="margin-top: 50px; margin-left: 10px">{{ orderDetail.goods.name}}</el-col>
      <el-col :span="3" style="margin-top: 40px; margin-left: 10px"><div class="price">￥{{ orderDetail.goods.markPrice}}</div>
        ￥{{ orderDetail.goods.price}}
      </el-col>
      <el-col :span="3" style="margin-top: 50px; margin-left: 10px">{{orderDetail.count}}</el-col>
      <el-col :span="3" style="margin-top: 40px; margin-left: 10px">￥{{ orderDetail.price}}
        <div class="carriage">(含运费:￥0.00)</div>
      </el-col>
    </el-row>
    <!--订单详情结束-->
  </el-card>
</template>

<script setup>
import orderApi from "@/api/orderApi.js";
import {useRoute} from "vue-router";
import {useRouter} from "vue-router";
import {ref} from "vue";
import {ElMessage} from "element-plus";

const route = useRoute();
const router = useRouter();
//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//存放订单信息
const order = ref();
//总金额
const total = ref(0);

//获取订单信息
function getOrder() {
  const orderId = route.query.orderId;
  //查询
  orderApi.selectById(orderId)
      .then(resp => {
        if (resp.code == 10000) {
          order.value = resp.data;
          for (let orderDetail of order.value.orderDetailList) {
            total.value += orderDetail.count * orderDetail.price
            console.log(order.value)
          }
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//跳转到生成订单的页面
function toCreateOrderPage(id) {
  //获取已选中的购物车的id
  let orderId = id;

  router.push({
    path: "/user/pay",
    query: {
      orderId
    }
  });
}

getOrder();
</script>

<style scoped>
.header div {
  color: #AAAAAA;
  font-size: 15px;
}

.title div {
  margin-top: 10px;
  text-align: center;
  font-size: 40px;
  font-weight: bolder;
  color: var(--theme-color);

}

.price {
  margin-left: 5px;
  font-size: 13px;
  color: #8c939d;
  text-decoration: line-through;
}

.carriage {
  margin-left: -20px;
  color: #8c939d;
  font-size: 15px;
}
</style>