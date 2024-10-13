<template>
  <div class="index">
    <el-card style="opacity: 0.9; float: left; width: 300px; height: 250px; margin-top: 10px; margin-right: 20px">
      <div id="introduce" style="width: 500px; height: 200px;">
        <el-image src="/src/images/mi.png" style="width: 250px; height: 200px"></el-image>
      </div>
    </el-card>
    <el-card style="opacity: 0.9; float: left; width: 550px; height: 250px; margin-top: 10px;">
      <div id="chart01" style="width: 500px; height: 200px;"></div>
    </el-card>
    <el-card style="opacity: 0.9; float: left; margin-left: 20px; width: 550px; height: 250px; margin-top: 10px;">
      <div id="chart02" style="width: 500px; height: 200px;"></div>
    </el-card>
    <el-card style="opacity: 0.9; float: left; margin-left: 0; width: 1445px; height: 400px; margin-top: 10px;">
      <div id="chart03" style="width: 1300px; height: 400px;"></div>
    </el-card>
  </div>
</template>

<script setup>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";
import goodsApi from "@/api/goodsApi.js";
import userApi from "@/api/userApi.js";
import orderApi from "@/api/orderApi.js";

//存放商品信息
const goods = ref([]);
//存放上架的商品
const upGoods = ref([]);
//存放用户信息
const user = ref([]);
//存放订单信息
const order = ref([]);
//存放订单详情
const orderDetail = ref([]);


//获取商品信息
function selectGoods() {
  goodsApi.selectAll()
      .then(resp => {
        goods.value = resp.data;
        //将上架的物品保存
        goods.value.forEach(g => {
          if (g.status === 0) {
            upGoods.value.push(g);
          }
        })
        chart01();
      });

}

//获取用户信息
function selectUser() {
  userApi.selectALL()
      .then(resp => {
        user.value = resp.data;
        chart02();
      })
}

//获取订单信息
function selectOrder() {
  orderApi.selectAll()
      .then(resp => {
        order.value = resp.data;

        order.value.forEach(orderItem  => {
          // 初始化总数量和总价格
          let totalCount = 0;
          let totalPrice = 0;

          orderItem.orderDetailList.forEach(detail => {
            // 提取 count 和 price
            totalCount += detail.count;
            totalPrice += detail.price * detail.count;

            // 将总数量和总价格添加到 orderItem
            orderItem.totalCount = totalCount;
            orderItem.totalPrice = totalPrice;
          })
        });
        chart03();
        // 打印处理后的订单数据
        console.log(order.value);

      })
}

//上架商品个数图表
function chart01() {
  // 获取上架物品的数量
  const itemCount = upGoods.value.length;
  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(document.getElementById('chart01'));
  // 绘制图表
  let option = {
    title: {
      text: `上架商品的个数：${itemCount}`, // 在顶部显示物品的个数
      left: 'center', // 标题位置居中
      top: '5%', // 距离顶部的百分比
      textStyle: {
        fontSize: 18, // 标题字体大小
        fontWeight: 'bold',
      }
    },
    xAxis: {
      type: 'value' // 将 x 轴设置为数值型
    },
    yAxis: {
      type: 'category', // 将 y 轴设置为分类型
      axisLabel: {
        show: false // 隐藏 Y 轴的标签
      }
    },
    series: [
      {
        data: [itemCount],
        type: 'bar', // 仍然是柱状图
        itemStyle: {
          color: '#4CAF50', // 设置柱子的颜色
        }
      }
    ],
  };
  // 渲染图表
  myChart.setOption(option);
}

//显示用户个数的图表
function chart02() {
  // 获取上架物品的数量
  const userCount = user.value.length;
  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(document.getElementById('chart02'));
  // 绘制图表
  let option = {
    title: {
      text: `注册用户的个数：${userCount}`, // 在顶部显示物品的个数
      left: 'center', // 标题位置居中
      top: '5%', // 距离顶部的百分比
      textStyle: {
        fontSize: 18, // 标题字体大小
        fontWeight: 'bold',
      }
    },
    xAxis: {
      type: 'value' // 将 x 轴设置为数值型
    },
    yAxis: {
      type: 'category', // 将 y 轴设置为分类型
      axisLabel: {
        show: false // 隐藏 Y 轴的标签
      }
    },
    series: [
      {
        data: [userCount],
        type: 'bar', // 仍然是柱状图
        itemStyle: {
          color: '#F596AA', // 设置柱子的颜色
        }
      }
    ],
  };
  // 渲染图表
  myChart.setOption(option);
}

//订单交易额
function chart03() {
  // 从订单中提取日期数据和交易额（假设订单对象有amount和date属性）
  const dates = order.value.map(item => {
    const date = new Date(item.createTime); // 解析日期
    return date.toLocaleDateString(undefined, { month: '2-digit', day: '2-digit' }); // 格式化为月-日
  });

  // 提取交易额
  const money = order.value.map(item => {
    return item.totalPrice;
  })

  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(document.getElementById('chart03'));
  // 绘制图表
  let option = {
    title: {
      text: `商城订单交易额`, // 在顶部显示物品的个数
      left: '5%', // 标题位置居中
      top: '2%', // 距离顶部的百分比
      textStyle: {
        fontSize: 15, // 标题字体大小
        fontWeight: 'bold',
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: money,
        type: 'line',
        areaStyle: {},
        label: { // 显示节点上的数据
          show: true, // 显示标签
          position: 'top', // 标签位置在上方
          formatter: '{c}', // 标签格式为数据值
          fontSize: 12, // 字体大小
          color: '#333', // 字体颜色
        },
      }
    ]
  };
  // 渲染图表
  myChart.setOption(option);
}

selectGoods();
selectUser();
selectOrder();
//等待页面渲染完再掉用方法
// onMounted(() => {
//   chart01();
// })
</script>

<style scoped>
.hd_bat {
  padding: 0;
}

.hd_style {
  background-color: #000000;
  line-height: 50px;
}

.hd_style ul li {
  float: left;
  margin-left: 30px;
  margin-right: 40px;
}

a {
  color: #999;
}

</style>