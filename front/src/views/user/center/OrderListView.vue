<template>
  <el-card>
    <div>
      <div class="header">
        <div class="top">
          <span class="orderText">我的订单</span>
          <span class="promptText">请谨防钓鱼链接或诈骗电话</span>
        </div>
        <div class="main">
          <el-row v-for="(order, index) in orderList" :key="index" class="orderItem">
            <el-card style="width: 900px; background-color: #EEEEEE;">
              <el-row style="margin-top: 0">
                <el-col :span="3" style="font-size: 12px; margin-left: 20px; margin-top: 20px">{{formatDate(order.createTime)}}</el-col>
                <el-col :span="6" style="font-size: 12px; margin-left: 20px; margin-top: 20px">订单号：{{order.id}}</el-col>
              </el-row>
              <el-row style="background-color: #FFFFFF; margin-bottom: 0" v-for="(orderDetail, index) in order.orderDetailList" :key="index">
                <el-col :span="3">
                  <div class="pic">
                    <el-image
                        :src="`${SERVER_ADDR}/goods/pic/${orderDetail.goods.picList[0].url}`"
                        style="width: 100px; height: 100px; margin-top: 10px; margin-left: 10px"
                        fit="contain" />
                  </div>
                </el-col>
                <el-col :span="4" style="margin-top: 60px; margin-left: 10px">{{ orderDetail.goods.name}}</el-col>
                <el-col :span="3" style="margin-top: 30px; margin-left: 10px"><div class="price">￥{{ orderDetail.goods.markPrice}}</div>
                  ￥{{ orderDetail.goods.price}}
                </el-col>
                <el-col :span="3" style="margin-top: 60px; margin-left: 10px">{{orderDetail.count}}</el-col>
                <el-col :span="3" style="margin-top: 60px; margin-left: 10px">￥{{ orderDetail.price}}
                  <div class="carriage">(含运费:￥0.00)</div>
                </el-col>
                <el-col :span="4" style="margin-top: 60px; margin-left: 80px">{{getOrderStatus(order.status)}}</el-col>
              </el-row>
            </el-card>
          </el-row>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import orderApi from "@/api/orderApi.js";
import {ref} from "vue";
import userApi from "@/api/userApi.js";
import {Delete} from "@element-plus/icons-vue";

//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//存放该用户的所有订单
const orderList = ref([]);
//存放该用户订单中的订单详情
const orderDetailList = ref([]);
//保存用户信息
const user = ref({
  userId: null
});

//获取当前用户的订单信息
function getOrderList() {
  const condition = {
    userId: user.value.id
  }
  orderApi.selectByPage(condition)
      .then(resp => {
        orderList.value = resp.data.list;
        console.log(orderList.value)
        //将订单详情保存
        resp.data.list.forEach(o => {
          o.orderDetailList.forEach(d => {
            orderDetailList.value.push(d);
          })
        })
        console.log(orderDetailList.value);

      })
}

//获取用户信息
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        user.value = resp.data;
        getOrderList();
      })
}

//设置显示的订单时间格式
function formatDate(dateString) {
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

//通过status来决定订单状态
function getOrderStatus(status) {
  const statusMap = {
    0: '未支付',
    1: '已支付',
    2: '已发货',
    3: '已收货',
    4: '退货退款',
    5: '退换货',
    6: '仅退款',
    7: '售后',
    8: '其它'
  };
  return statusMap[status]; // 默认处理未知状态
}

getInfo();
</script>

<style scoped>
.el-card {
  --el-card-padding: 0;
}

.header {
  margin-top: 30px;
  margin-left: 30px;
}

.orderText {
  margin-top: 50%;
  color: #8c939d;
  font-size: 35px;
}

.promptText {
  margin-left: 10px;
  color: #999999;
  font-size: 13px;
}

.main {
  margin-top: 20px;
}

.main ul li {
  margin-top: 30px;
  float: left;
  margin-left: 0;
  margin-right: 40px;
}

.main div {
  margin-top: 2px;
  margin-bottom: 10px;
}

.price {
  margin-left: 5px;
  font-size: 13px;
  color: #8c939d;
  text-decoration: line-through;
}

.carriage {
  color: #8c939d;
  font-size: 15px;
}
</style>