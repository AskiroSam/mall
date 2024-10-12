<template>
  <el-card>
    <div>
      <div class="header">
        <div class="left">
          <el-image src="/src/images/tx.jpg" class="leftImg" />
        </div>
        <div class="main">
          <el-row>
            <el-col class="main_userName">{{user.username}}</el-col>
            <el-col class="main_text">欢迎光临~</el-col>
            <el-col class="main_router">修改个人信息></el-col>
          </el-row>
        </div>
        <div class="right">
          <el-row>
            <el-col class="right_text">
              账户状态：<span style="color:var(--theme-color);">{{user.status === 0 ? '未认证' : user.status === 1 ? '已认证' : user.status === 2 ? '禁用' : '注销'}}</span>
            </el-col>
            <el-col class="right_text">
              绑定手机：<span>{{user.phone}}</span>
            </el-col>
            <el-col class="right_text">
              绑定邮箱：<span>{{user.email}}</span>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="main">

      </div>
    </div>
  </el-card>
</template>

<script setup>
import userApi from "@/api/userApi.js";
import {ref} from "vue";

//保存用户信息
const user = ref({
  username: '',
  status: null,
  phone: '',
  email: ''
});

//获取用户信息
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        user.value = resp.data;
      })
}

getInfo();
</script>

<style scoped>
.header {
  display: flex;
}

.left {
  margin-top: 20px;
  margin-left: 50px;
  float: left;
  align-items: center;
  justify-content: space-between;
}

.leftImg {
  width: 160px;
  height: 160px;
  border: 3px double #8c939d;
  border-radius: 50%;
}

.main_userName {
  margin-top: 50px;
  margin-left: 20px;
  font-size: 25px;
  font-weight: bold;
}

.main_text {
  margin-top: 5px;
  margin-left: 20px;
  font-size: 15px;
  color: #999999;
}

.main_router {
  margin-top: 15px;
  margin-left: 20px;
  font-size: 12px;
  color: var(--theme-color);
}

.right {
  float: right;
  margin-top: 25px;
}

.right_text {
  margin-top: 20px;
  margin-left: 80px;
  font-size: 14px;
  color: #999999;
}
</style>