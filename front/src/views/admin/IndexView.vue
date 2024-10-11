<template>
  <el-header class="hd_bat">
    <!--头部导航-->
    <div class="hd_style">
      <el-image src="/src/images/MyLogo2.png" style="float: left; width: 290px; height: 40px; margin-top: 4px; margin-left: 10px"></el-image>
      <div class="center">
        <el-row>
          <el-col :span="4">

          </el-col>
          <el-col :span="16">
            <ul>
              <li><RouterLink to="/admin/adminlist">管理员管理</RouterLink></li>
              <li><RouterLink to="/admin/categorylist">商品分类管理</RouterLink></li>
              <li><RouterLink to="/admin/goodslist">商品管理</RouterLink></li>
              <li><RouterLink to="/admin/orderlist">用户管理</RouterLink></li>
              <li><RouterLink to="/admin/userlist">订单管理</RouterLink></li>
            </ul>
          </el-col>
          <el-col :span="4"></el-col>
        </el-row>
      </div>
    </div>
  </el-header>
  <el-main>
    <div id="main" style="width: 600px;height:400px;"></div>
  </el-main>
  <el-footer></el-footer>
</template>

<script setup>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";
import {ShoppingCart} from "@element-plus/icons-vue";

//显示图表
//初始化函数
function init() {
  // 基于准备好的dom，初始化echarts实例
  let Chart = echarts.init(document.getElementById("main"));
  // 绘制图表
  let options = {
    title: {
      text: "ECharts 入门示例",
    },
    tooltip: {},
    xAxis: {
      data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
    },
    yAxis: {},
    series: [
      {
        name: "销量",
        type: "bar",
        data: [5, 20, 36, 10, 10, 20],
      },
    ],
  };

  // 渲染图表
  Chart.setOption(options);
}


//等待页面渲染完再掉用方法
onMounted(() => {
  init();
})
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