<template>
  <el-card>
    <div>
      <div class="header">
        <div class="top">
          <span class="collectText">我的收藏</span>
          <span class="titleText">请查看你喜欢的商品~</span>
        </div>
      </div>
    </div>

    <div class="body">
      <div class="main">
        <el-row v-for="(collect, index) in collectList" :key="index" class="orderItem">
          <el-card style="width: 900px; margin-bottom: 0; background-color: #EEEEEE;">

            <!--收藏头部信息-->
            <el-row style="margin-top: 0">
              <el-col :span="3" style="font-size: 12px; margin-left: 20px; margin-top: 20px">{{formatDate(collect.createTime)}}</el-col>
            </el-row>

            <!--订单详情-->
            <el-row style="background-color: #FFFFFF; margin-bottom: 0;">
              <el-col :span="3">
                <div class="pic">
                  <el-image
                      :src="`${SERVER_ADDR}/goods/pic/${collect.goods.picList[0].url}`"
                      style="width: 100px; height: 100px; margin-top: 10px; margin-left: 10px"
                      fit="contain" />
                </div>
              </el-col>
              <el-col :span="4" style="margin-top: 60px; margin-left: 10px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{collect.goods.name}}</el-col>
              <el-col :span="2" style="margin-top: 60px; margin-left: 10px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{collect.goods.color}}</el-col>
              <el-col :span="3" style="margin-top: 60px; margin-left: 10px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">版本：{{collect.goods.version}}</el-col>
              <el-col :span="4" style="margin-top: 60px; margin-left: 10px">价格：{{collect.goods.price}}</el-col>
              <el-col :span="3" style="margin-top: 60px; margin-left: 10px">评分:{{collect.goods.score}}</el-col>
              <el-col :span="2" style="margin-top: 50px; margin-left: 20px"><el-button type="danger" :icon="Delete" circle @click="cancelCollect(collect.id)" /></el-col>
            </el-row>
          </el-card>
        </el-row>

        <!--分页导航-->
        <el-row class="row-bg" justify="center" style="margin-top: 20px;">
          <el-pagination background layout="prev, pager, next"
                         v-model:current-page="pageInfo.pageNum"
                         :page-count="pageInfo.pages"
                         @update:current-page="getCollectList" />
        </el-row>
      </div>
    </div>

  </el-card>
</template>

<script setup>
import {ref} from "vue";
import collectApi from "@/api/collectApi.js";
import userApi from "@/api/userApi.js";
import {Delete} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";


//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//存放该用户的所有收藏
const collectList = ref([]);
//保存用户信息
const user = ref({
  userId: null,
});
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

//设置显示的收藏时间格式
function formatDate(dateString) {
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

//取消收藏
function cancelCollect(id) {
  collectApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          getCollectList()
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//获取当前用户的收藏信息
function getCollectList(pageNum) {
  const condition = {
    userId: user.value.id
  }
  collectApi.selectByPage(condition, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
        collectList.value = resp.data.list;
        console.log(collectList.value)
      })
}

//获取用户信息
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        user.value = resp.data;
        getCollectList();
      })
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

.collectText {
  margin-top: 50%;
  color: #8c939d;
  font-size: 35px;
}

.titleText {
  margin-left: 10px;
  color: #999999;
  font-size: 13px;
}

.body {
  margin-top: 20px;
  margin-left: 30px;
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
</style>