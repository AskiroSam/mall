<template>
  <el-row style="margin-top: 15px;">
    <el-col :span="12">
      <el-image src="/src/images/3.jpeg" />
    </el-col>
    <el-col :span="12">
      <el-form label-width="auto" class="loginForm">
        <h1>用户登录</h1>
        <el-form-item>
          <el-input  prefix-icon="User" placeholder="请输入用户名" v-model="userInfo.username" />
        </el-form-item>
        <el-form-item>
          <el-input prefix-icon="Key" type="password" placeholder="请输入密码" v-model="userInfo.password" />
        </el-form-item>
        <el-form-item>
          <el-input prefix-icon="Picture" style="width: 175px; margin-right: 5px" placeholder="请输入验证码" v-model="userInfo.captchaInput" />
          <el-image :src="imageBase64Data" @click="getCaptcha"></el-image>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="loginBtn" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script setup>
import userApi from "@/api/userApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {useTokenStore} from "@/stores/token.js";
import {useUserStore} from "@/stores/user.js";

//token
const tokenStore = useTokenStore();
//user
const userStore = useUserStore();
//路由跳转
const router = useRouter();
//保存验证码
const imageBase64Data = ref(null);
//用户注册信息
const userInfo = ref({
  username: '',
  password: '',
  key: '',
  captchaInput: ''
})

//获取验证码
function getCaptcha() {
  userApi.captcha()
      .then(resp => {
        userInfo.value.key = resp.data.key;
        imageBase64Data.value = resp.data.imageBase64Data;
      });
}

//用户登录
function login() {
  userApi.login(userInfo.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //保存token
          tokenStore.update(resp.data);
          //获取用户信息
          getInfo();
          //跳转到前台首页
          router.push('/user/index');
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//获取用户信息
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        if (resp.code == 10000) {
          //将已登录用户的信息保存在Pinia中
          userStore.updateUser(resp.data);
        }

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

.loginBtn {
  width: 100%;
}



</style>