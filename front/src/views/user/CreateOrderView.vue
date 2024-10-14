<template>

  <!--显示所有的地址-->
  <div class="addrList">
    <div class="title">我的地址</div>
    <ul>
      <li v-for="(addr, index) in addrList" :key="index" @click="addrId = addr.id">
        <!--<span :class="{contact: true, selectAddr: addr.id === addrId}">{{ addr.contact }}</span>-->
        <span :class="['contact', addr.id === addrId ? 'selectAddr' : '']">{{ addr.contact }}</span>
        <span class="address">{{ addr.contact }} {{ addr.province }} {{ addr.city }} {{ addr.district }} {{ addr.address }}</span>
      </li>
    </ul>
  </div>

  <!--显示所有选中的购物车-->
  <div class="cartList">
    <div class="title">我的商品</div>
    <ul>
      <li v-for="(cart, index) in cartList" :key="index">
        <el-row>
          <el-col :span="1" style="margin-right: 10px">
            <el-image
                :src="`${SERVER_ADDR}/goods/pic/${cart.goods.picList[0].url}`"
                style="width: 50px; height: 50px"
                fit="contain" />
          </el-col>
          <el-col :span="4">{{ cart.goods.name }}</el-col>
          <el-col :span="5">{{ cart.goods.color }} - {{ cart.goods.version }}</el-col>
          <el-col :span="3">￥{{ cart.goods.price }}元</el-col>
          <el-col :span="1">x{{ cart.count }}</el-col>
          <el-col :span="3">总价：￥{{ cart.goods.price * cart.count }}</el-col>
        </el-row>
      </li>
    </ul>
  </div>

  <!--显示总金额和生成订单的按钮-->
  <div class="footer">
    总金额：￥<span class="total">{{total}}</span>元
    <el-button type="danger" class="createOrder" @click="createOrder">生成订单</el-button>
  </div>

</template>

<script setup>
import {useRoute} from "vue-router";
import addrApi from "@/api/addrApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import cartApi from "@/api/cartApi.js";
import orderApi from "@/api/orderApi.js";
import {useRouter} from "vue-router";

const router =useRouter();
const route = useRoute();
// console.log(route.query.cartIds)

//当前用户的所有地址信息
const addrList = ref([]);
//当前用户选中的购物车
const cartList = ref([]);
//被选中的地址 - 用来生成订单的地址id
const addrId = ref(null);
//总金额
const total = ref(0);
//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

//生成订单
function createOrder() {
  //判断有没有选择地址
  if (!addrId.value) {
    ElMessage.error("请选择收货地址");
    return;
  }

  //已选择的购物车的地址
  let cartIds = route.query.cartIds;
  let orderVo = {
    addrId: addrId.value,
    cartIds: cartIds
  }

  //发送请求生成订单
  orderApi.insert(orderVo)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);

          //去支付页面
          router.push( {
            path: '/user/pay',
            query : {
              orderId: resp.data.orderId
            }
          });
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//获取当前用户的所有的地址
function getAddrList() {
  addrApi.selectByUser()
      .then(resp => {
        if (resp.code == 10000) {
          addrList.value = resp.data;
          for (let addr of addrList.value) {
            if (addr.status == 1) {
              addrId.value = addr.id;
            }
          }
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//查询当前用户已选择的购物车信息
function getCartList() {
  let cartIds = route.query.cartIds;
  // 确保 cartIds 是一个数组，如果是字符串则转为数组
  if (typeof cartIds === 'string') {
    cartIds = [cartIds];  // 转为数组
  }
  cartApi.selectByIds(cartIds)
      .then(resp => {
        if (resp.code == 10000) {
          cartList.value = resp.data;

          //计算总金额
          cartList.value.forEach(cart => {
            total.value += cart.count * cart.goods.price;
          });
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
getCartList();
getAddrList();
</script>

<style scoped>
.addrList, .cartList, .footer{
  margin: 20px 0;
  padding: 20px;
  border: 2px solid #222;
  background-color: #FFF;
}

.footer {
  text-align: right;
}

.footer .createOrder{
  margin-left: 15px;
}

.footer .total {
  font-weight: bold;
  font-size: 18px;
  color: var(--theme-color);
}

.title {
  margin-bottom: 20px;
  font-weight: bold;
  font-size: 18px;
}

.addrList ul li {
  margin: 10px 0 10px 15px;
  height: 30px;
  line-height: 30px;
}

.addrList .contact {
  margin-right: 15px;
  display: inline-block;
  text-align: center;
  border: 2px solid #AAA;
  width: 80px;
  cursor: pointer;
}

.addrList .contact:hover {
  border: 2px solid var(--theme-color);
}

.addrList .selectAddr {
  border: 2px solid var(--theme-color);
}

.cartList ul li {
  margin: 10px 0 10px 15px;
  height: 50px;
  line-height: 50px;
}
</style>