<template>
  <el-container>
    <el-header>
      <el-menu
          default-active="1"
          class="el-menu-demo"
          mode="horizontal"
          background-color="#000000"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="border-bottom: none"
          :ellipsis="false"
      >
        <el-menu-item index="/admin/index" @click="router.push('/admin/index')" style="width: 200px;">
            <img src="../.././images/MyLogo2.png" style="height: 40px; width: 190px;">
        </el-menu-item>
        <el-sub-menu index="2">
          <template #title>{{ admin.username }}</template>
          <el-menu-item index="center" @click="seeInfo">个人中心</el-menu-item>
          <el-menu-item index="chgPwd" @click="chgPwdDialogShow = true">修改密码</el-menu-item>
          <el-menu-item index="logOut" @click="logout">退出</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>

    <el-container>
      <el-aside width="200px">
        <el-menu
            active-text-color="#ffd04b"
            background-color="#261E47"
            class="el-menu-vertical-demo"
            text-color="#fff"
            :router="true"
        >
          <el-menu-item index="/admin/index">
            <el-icon>
              <HomeFilled/>
            </el-icon>
            首台首页</el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <Avatar/>
              </el-icon>
              <span>管理员管理</span>
            </template>
            <el-menu-item index="/admin/adminlist">管理员列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <Files/>
              </el-icon>
              <span>商品分类的管理</span>
            </template>
            <el-menu-item index="/admin/categorylist">分类列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon>
                <Goods/>
              </el-icon>
              <span>商品管理</span>
            </template>
            <el-menu-item index="/admin/goodslist">商品列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <el-icon>
                <User/>
              </el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/userlist">用户列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="5">
            <template #title>
              <el-icon>
                <Tickets/>
              </el-icon>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/admin/orderlist">订单列表</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>
        <RouterView></RouterView>
      </el-main>
    </el-container>
  </el-container>

  <!-- 修改密码的对话框开始 -->
  <el-dialog v-model="chgPwdDialogShow" title="修改密码" width="500">
    <el-form>
      <el-form-item label="原密码:" label-width="18%" prop="sno">
        <el-input type="password" v-model="chgPwdObject.oldPwd" placeholder="请输入原密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="新密码:" label-width="18%" prop="sname">
        <el-input type="password" v-model="chgPwdObject.newPwd" placeholder="请输入新密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="确认密码:" label-width="18%" prop="sname">
        <el-input type="password" v-model="chgPwdObject.newPwd1" placeholder="确认新密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="chgPwdDialogShow = false">取消</el-button>
        <el-button type="primary" @click="chgPwd">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改密码的对话框结束 -->

  <!-- 修改管理员的对话框开始 -->
  <el-dialog v-model="updateDiaLogShow" title="修改管理员个人信息" width="500">
    <el-form>
      <el-form-item label="用户名:" label-width="18%" prop="sno">
        <el-input  v-model="admin.username" placeholder="请输入用户名" disabled autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="电话:" label-width="18%" prop="sname">
        <el-input v-model="admin.phone" placeholder="请输入电话" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="邮箱:" label-width="18%" prop="sname">
        <el-input v-model="admin.email" placeholder="请输入电子邮箱" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="真实姓名:" label-width="18%" prop="sname">
        <el-input v-model="admin.realname" placeholder="请输入真实姓名" autocomplete="off" style="width: 300px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateDiaLogShow = false">取消</el-button>
        <el-button type="primary" @click="chgInfo">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改管理员的对话框结束 -->


</template>

<script setup>
import router from "@/router/index.js";
import {HomeFilled} from "@element-plus/icons-vue";
import { useTokenStore } from "@/stores/token.js";
import adminApi from "@/api/adminApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";

const tokenStore = useTokenStore();
//是否显示修改管理员信息的对话框
const updateDiaLogShow = ref(false);
//管理员信息
const admin =  ref({
  username: null,
  phone: null,
  email: null,
  realname: null
});
//是否显示修改密码的对话框
const chgPwdDialogShow = ref(false);
//用来保存修改密码信息的对象
const chgPwdObject = ref({
  oldPwd: null,
  newPwd: null,
  newPwd1: null
});


//修改密码
function chgPwd() {
  adminApi.chgPwd(chgPwdObject.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          chgPwdObject.value = {
            oldPwd: null,
            newPwd: null,
            newPwd1: null
          }
          //重置token并退出
          logout();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//显示个人信息的方法(未修改时关闭回到初始数据)
function seeInfo() {
  updateDiaLogShow.value = true;
  getInfo();
}
//修改个人信息的方法
function chgInfo() {
  adminApi.chgInfo(admin.value)

      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          updateDiaLogShow.value = false;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//退出
function logout() {
  //将token重置
  tokenStore.$reset();
  //跳转到管理员登录
  router.push("/admin/login");
}

//获取已登录用户的信息
function getInfo() {
  adminApi.getInfo()
      .then(resp => {
        admin.value = resp.data;

      })
}
getInfo();


</script>

<style scoped>
.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-header {
  padding: 0px;
}

.el-menu {
  background-color: #000000;

}
</style>