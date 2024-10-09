<template>
  <el-row style="margin-top: 15px;">
    <el-col :span="12">
      <el-image src="/src/images/3.jpeg" />
    </el-col>
    <el-col :span="12">
      <el-form label-width="auto" class="regForm">
        <h1>用户注册</h1>
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
          <el-button type="primary" class="regBtn" @click="reg">注册</el-button>
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

//用户注册
function reg() {
  userApi.reg(userInfo.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          router.push('/user/login');
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

getCaptcha();
</script>

<style scoped>
.regForm {
  width: 300px;
  margin: 25px 100px;
}

.regForm h1 {
  text-align: center;
  margin-bottom: 20px;
}

.regBtn {
  width: 100%;
}



</style>