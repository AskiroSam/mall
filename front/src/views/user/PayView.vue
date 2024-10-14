<template>
  <div class="info">
    <el-row>
      <el-col :span="3" style="text-align: right">
        <el-icon><CircleCheck /></el-icon>
      </el-col>
      <el-col :span="16" style="height: 104px;">
        <div style="line-height: 52px; margin-left: 20px">订单提交成功！去付款喽~</div>
        <div style="line-height: 32px; margin-left: 20px">请在0个小时14分钟内完成支付，超时后自动取消订单</div>
      </el-col>
      <el-col :span="5" style="height: 104px; line-height: 104px; text-align: right">
        应付总额<span class="total">￥{{total.toFixed(2)}}</span>元
      </el-col>
    </el-row>
    <div>
      <el-collapse>
        <el-collapse-item title="订单详情" name="1">
          <el-row>
            <el-col :span="2">订单号：</el-col>
            <el-col :span="10" style="color: var(--theme-color)">{{order.id}}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2">收货信息：</el-col>
            <el-col :span="10">{{order.addrDetail}}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2">商品名称：</el-col>
            <el-col :span="10">
              <div v-for="(orderDetail, index) in order.orderDetailList" :key="index">
                {{orderDetail.goods.name}} {{orderDetail.goods.color}} {{orderDetail.goods.version}}
              </div>
            </el-col>
          </el-row>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
  <div class="pay">
    <div>
      <div class="title">支付工具</div>
      <ul>
        <li>余额支付</li>
        <li>支付宝支付</li>
        <li>微信支付</li>
        <li>银行卡支付</li>
      </ul>
    </div>
    <div>
      <el-input placeholder="请输入支付密码" v-model="payPwd" type="password" style="margin: 10px 10px; width: 200px"></el-input>
      <el-button type="danger" size="large" @click="pay">支付</el-button>
    </div>
  </div>
</template>

<script setup>
import {useRoute} from "vue-router";
import {useRouter} from "vue-router";
import orderApi from "@/api/orderApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {CircleCheck} from "@element-plus/icons-vue";

const route = useRoute();
const router = useRouter();
//保存订单信息
const order = ref({});
//总金额
const total = ref(0);
//支付密码
const payPwd = ref("");


//根据订单id查询相关信息 地址-商品
function getOrder() {
  //获取路径上的订单id
  const orderId = route.query.orderId;

  //查询
  orderApi.selectById(orderId)
      .then(resp => {
        if (resp.code == 10000) {
          order.value = resp.data;
          for (let orderDetail of order.value.orderDetailList) {
            total.value += orderDetail.count * orderDetail.price
          }

        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//支付
function pay() {
  orderApi.pay({
    orderId: route.query.orderId,
    payPwd: payPwd.value
  }).then(resp => {
    if (resp.code == 10000) {
      ElMessage.success(resp.msg);
      //跳转到用户的订单详情页
      router.push("/user/orderList");
    } else {
      ElMessage.error(resp.msg);
    }
  })
}

getOrder();
</script>

<style scoped>
.info, .pay {
  margin-top: 30px;
  background-color: #FFFFFF;
  padding: 30px;
}

.info .el-icon {
  font-size: 100px;
  color: #83C44EFF;
}

.info .total {
  font-size: 18px;
  font-weight: bold;
  color: var(--theme-color);
}

.pay .title {
  font-weight: bold;
  font-size: 18px;
}

.pay ul li {
  margin: 10px 10px;
  padding: 0 10px;
  float: left;
  border: 2px solid #AAA;
  height: 40px;
  line-height: 40px;
}

</style>