<template>
  <div v-if="cartList.length > 0">
    <el-row class="title">
      <el-col :span="2">
        <el-checkbox label="全选" v-model="checkAllState"  :indeterminate="halfState" @change="checkAllOrNone" :disabled="cartList.length === 0" />
      </el-col>
      <el-col :span="11">商品</el-col>
      <el-col :span="2">单价</el-col>
      <el-col :span="6">数量</el-col>
      <el-col :span="2">小计</el-col>
      <el-col :span="1">操作</el-col>
    </el-row>
    <el-row v-for="(cart, index) in cartList" :key="index" class="cartItem">
      <el-col :span="2">
        <el-checkbox v-model="cart.checkState" @change="changeState" />
      </el-col>
      <el-col :span="2">
        <el-image :src="`${SERVER_ADDR}/goods/pic/${cart.goods.picList[0].url}`"
                  style="height: 50px; width: 50px;" fit="contain"/>
      </el-col>
      <el-col :span="5">{{cart.goods.name}}</el-col>
      <el-col :span="4">{{cart.goods.color}} - {{cart.goods.version}}</el-col>
      <el-col :span="2">{{cart.goods.price}}</el-col>
      <el-col :span="6">
        <el-input-number v-model="cart.count" :min="1" size="small" @change="updateCount(cart)" />
      </el-col>
      <el-col :span="2">{{cart.goods.price * cart.count}}</el-col>
      <el-col :span="1">
        <el-button type="danger" :icon="Delete" circle @click="deleteById(cart.id)" />
      </el-col>
    </el-row>
    <el-row class="cartFooter">
      <el-col :span="2">
        <el-checkbox label="全选" v-model="checkAllState"  :indeterminate="halfState" @change="checkAllOrNone" :disabled="cartList.length === 0" />
      </el-col>
      <el-col :span="3"><el-link :underline="false" @click="deleteList">删除选中的商品</el-link></el-col>
      <el-col :span="11"><el-link :underline="false"  @click="deleteAll">清空购物车</el-link></el-col>
      <el-col :span="3">
        已选择{{ checkedCount }}件商品
      </el-col>
      <el-col :span="3">
        总价：￥{{ total }}
      </el-col>
      <el-col :span="2">
        <el-button type="danger" @click="toCreateOrderPage">结算</el-button>
      </el-col>
    </el-row>
  </div>
  <div v-else class="none">
    <div class="none_img"><el-image src="/src/images/shopping.png" style="height: 200px; width: 200px"></el-image></div>
    <div class="none_text">购物车空空如也~，快<RouterLink to="/user/index">去购物</RouterLink>吧~</div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import cartApi from "@/api/cartApi.js";
import {Delete} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();
//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//当前用户所有的购物车信息
const cartList = ref([]);
//全选按钮是否选中
const checkAllState = ref(false);
//没有全选的状态
const halfState = ref(false);
//选中商品的数量
const checkedCount = ref(0);
//选中商品的总金额
const total = ref(0);
//选中的商品
const deleteIdList = ref([]);

//跳转到生成订单的页面
function toCreateOrderPage() {
  //获取已选中的购物车的id
  let cartIds = [];

  cartList.value.forEach(cart => {
    if (cart.checkState) {
      cartIds.push(cart.id);
    }
  });

  if (cartIds.length === 0) {
    ElMessage.warning("您还没有选择商品，无法进行结算")
    return;
  }

  router.push({
    path: "/user/createOrder",
    query: {
      cartIds
    }
  });
}

//全选 - 全不选
function checkAllOrNone() {
  // 检查购物车是否为空
  if (cartList.value.length === 0) {
    checkAllState.value = false; // 购物车为空，不能全选
    halfState.value = false;      // 也不能半选
    return; // 直接返回
  }
  //让每个购物车向前面的多选按钮的状态和全选按钮的状态保持一致
  let count = 0;
  cartList.value.forEach(cart => {
    cart.checkState = checkAllState.value
    if (cart.checkState) {
      count++;
    }
  });
  if (count == cartList.value.length) {
    halfState.value = false;
    checkAllState.value = true;
  } else {
    halfState.value = false;
    checkAllState.value = false;
  }
  // cartList.value.forEach(cart => {
  //   cart.checkState = checkAllState.value;
  // });
  getCountAndTotal();
}

//购物车向前面的多选按钮状态发生变化时，调用的函数
function changeState() {
  /*
  * 设置全选按钮的状态
  *   1.如果所有的checkbox都被选中，则全选按钮被选中
  *   2.如果所有的checkbox没有全被选中，则全选按钮不被选中
  *  计算选中商品的数量和金额
  * */
  let count  = 0 //被选中的checkbox的数量
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      count++; //计算被选中的checkbox的数量
    }
  });
  if (count == cartList.value.length && cartList.value.length > 0) {
    checkAllState.value = true;
    halfState.value = false;
  } else if (count == 0){
    checkAllState.value = false;
    halfState.value = false;
  } else {
    halfState.value = true;
  }
  getCountAndTotal();
}

//计算选中商品的数量和金额
function getCountAndTotal() {
  checkedCount.value = 0;
  total.value = 0;
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      checkedCount.value += cart.count; //计算选中的商品的数量
      total.value += cart.goods.price * cart.count; //计算选中的商品的金额
    }
  });
}

//查询当前用户所有的购物车信息
// function selectCartList() {
//   cartApi.selectCartList()
//       .then(resp => {
//         cartList.value = resp.data;
//       });
// }
function selectCartList() {
  cartApi.selectCartList()
      .then(resp => {
        // 使用 map 方法遍历 resp.data，给每个对象的 checkState 属性赋值为 false
        cartList.value = resp.data.map(item => {
          return {
            ...item, // 展开原有属性
            checkState: false // 添加或覆盖 checkState 属性
          };
        });
      })
}

//根据id删除
function deleteById(id) {
  cartApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //将被删除的项从cartList中清除
          cartList.value = cartList.value.filter(cart => cart.id != id);
          //设置按钮的状态
          changeState();
          getCountAndTotal();
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//删除选中的商品
function deleteList() {
  // 获取所有选中商品的 id
  let selectedIds = [];
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      selectedIds.push(cart.id);
    }
  });
  // const selectedIds = cartList.value
  //     .filter(item => item.checkState)
  //     .map(item => item.id);

  if (selectedIds.length === 0) {
    ElMessage.warning('请先选择商品');
    return;
  }

  cartApi.deleteIdList(selectedIds)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //将被删除的项从cartList中清除
          cartList.value = cartList.value.filter(cart => !cart.checkState);
          //设置按钮的状态
          changeState();
          getCountAndTotal();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//清空购物车
function deleteAll() {
  let selectAll = [];
  cartList.value.forEach(cart => {
    selectAll.push(cart.id);
  });
  // const selectAll = cartList.value
  //     .map(item => item.id);

  if (selectAll.length === 0) {
    ElMessage.warning("购物车中没有物品，无法清空");
    return;
  }

  cartApi.deleteIdList(selectAll)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success("已清空");
          //重新获取购物车列表
          selectCartList();
          //设置按钮的状态
          changeState();
          getCountAndTotal();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//修改购物车商品的数量
function updateCount(cart) {
  const cartInfo = {
    id: cart.id,
    count: cart.count
  }
  cartApi.update(cartInfo)
      .then(resp => {
        if (resp.code == 10000) {
          //让购物车中商品的数量和数据库中保持一致
          cartApi.selectById(cartInfo.id)
              .then(resp => {
                cartList.value.forEach(cart => {
                  if (cart.id == cartInfo.id) {
                    cart.count = resp.data.count;
                  }
                })
                getCountAndTotal();
              });
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

selectCartList();
</script>

<style scoped>
 .title, .cartFooter {
   margin: 20px 0;
   padding-left: 10px;
   font-size: 14px;
   background-color: #FFF;
   height: 40px;
   line-height: 40px;
 }

 .cartItem {
   margin-bottom: 5px;
   padding-left: 10px;
   font-size: 14px;
   background-color: #FFF;
   height: 50px;
   line-height: 50px;
 }

 .none {
   line-height: 300px;
   height: 300px;
   background-color: #FFFFFF;
   text-align: center;
 }

 .none_img {
   float: left;
   margin-left: 30%;
   margin-top: 40px;
 }

 .none_text {
   float: right;
   margin-right: 32%;
 }
</style>