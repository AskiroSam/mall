<template>
  <div class="info">
    <el-row>
      <el-col :span="3" style="text-align: right">
        <el-icon><CircleCheck /></el-icon>
      </el-col>
      <el-col :span="16" style="height: 104px;">
        <div style="line-height: 52px; margin-left: 20px">订单提交成功！去付款喽~</div>
        <!--<div style="line-height: 32px; margin-left: 20px">{{ remainingTime }}</div>-->
        <div style="line-height: 32px; margin-left: 20px">距离订单过期还有{{time}}分钟</div>
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
import {computed, onBeforeUnmount, onMounted, ref} from "vue";
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
//剩余寿命
const time = ref();
// 倒计时相关变量
// const countdown = ref(840); // 14分钟 = 840秒
// const timer = ref(null);

//计算倒计时
// const remainingTime = computed(() => {
//   const minutes = Math.floor(countdown.value / 60);
//   const seconds = countdown.value % 60;
//   return `距离订单过期还有：${minutes}分钟${seconds}秒`;
// });
// 启动倒计时
// function startCountdown() {
//   // 从 localStorage 获取剩余时间
//   const savedCountdown = localStorage.getItem('countdown');
//   if (savedCountdown) {
//     countdown.value = parseInt(savedCountdown);
//   } else {
//     countdown.value = 840; // 初始值
//   }
//   timer.value = setInterval(() => {
//     if (countdown.value > 0) {
//       countdown.value--;
//       // 每次更新时保存到 localStorage
//       localStorage.setItem('countdown', countdown.value);
//     } else {
//       clearInterval(timer.value);
//       // 在这里可以处理超时逻辑，比如取消订单
//       ElMessage.warning("订单已超时，自动取消");
//       router.push("/user/orderList");
//     }
//   }, 1000);
// }

//根据订单id查询相关信息 地址-商品
function getOrder() {
  //获取路径上的订单id
  const orderId = route.query.orderId;

  //查询订单信息
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

//获取订单剩余时间
function checkTime() {
  //获取路径上的订单id
  const orderId = route.query.orderId;

  orderApi.checkTime(orderId)
      .then(resp => {
        time.value = Math.floor(resp.data / 60);
      })

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

// 组件生命周期
// onMounted(() => {
  //先获取
  // getOrder();
  //再启用定时器
  // startCountdown();
// });
//当组件的生命周期结束时，Vue 会自动调用 onBeforeUnmount 中的代码
// onBeforeUnmount(() => {
//   clearInterval(timer.value);
// });

getOrder();
checkTime();
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