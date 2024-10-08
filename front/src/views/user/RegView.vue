<template>
  <el-row style="margin-top: 15px;">
    <el-col :span="12">
      <el-image src="/src/images/ad.png" />
    </el-col>
    <el-col :span="12">
      <el-form label-width="auto" class="loginForm">
        <h1>用户注册</h1>
        <el-form-item>
          <el-input  prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input prefix-icon="Key" type="password" />
        </el-form-item>
        <el-form-item>
          <el-input prefix-icon="Picture" style="width: 175px; margin-right: 5px" />
          <el-image :src="imageBase64Data"></el-image>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="regBtn">注册·</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script setup>
import userApi from "@/api/userApi.js";
import {ref} from "vue";

//保存验证码
const imageBase64Data = ref(null);

//获取验证码
function getCaptcha() {
  userApi.captcha()
      .then(resp => {
        imageBase64Data.value = resp.data.imageBase64Data;
      });
}


getCaptcha();
</script>

<style scoped>
.loginForm {
  width: 300px;
  margin: 25px 100px;
}

.loginForm h1 {
  text-align: center;
  margin-bottom: 20px;
}

.regBtn {
  width: 100%;
}

</style>