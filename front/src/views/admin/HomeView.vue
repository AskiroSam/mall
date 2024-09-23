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
            <img src="../.././images/MyLogo2.png" style="height: 40px; width: 200px;">
        </el-menu-item>
        <el-sub-menu index="2">
          <template #title>Admin</template>
          <el-menu-item index="center">个人中心</el-menu-item>
          <el-menu-item index="chgPwd">修改密码</el-menu-item>
          <el-menu-item index="logOut">退出</el-menu-item>
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
</template>

<script setup>
import router from "@/router/index.js";
import {HomeFilled} from "@element-plus/icons-vue";

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