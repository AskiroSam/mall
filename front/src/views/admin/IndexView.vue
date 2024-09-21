<template>
  <el-container>
    <el-header>
      <el-menu
          :default-active="1"
          class="el-menu-demo"
          mode="horizontal"
          background-color="#000000"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="border-bottom: none"
          :ellipsis="false"
      >
        <el-menu-item index="1">
            <img src="../.././images/MyLogo2.png" style="height: 40px; width: 250px;">
        </el-menu-item>
        <el-sub-menu index="2">
          <template #title>Admin</template>
          <el-menu-item index="2-1">个人中心</el-menu-item>
          <el-menu-item index="2-2">修改密码</el-menu-item>
          <el-menu-item index="2-3">退出</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>

    <el-container>
      <el-aside width="200px">
        <el-menu
            active-text-color="#ffd04b"
            background-color="#261E47"
            class="el-menu-vertical-demo"
            default-active="2"
            text-color="#fff"
        >
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>管理员管理</span>
            </template>
            <el-menu-item index="1-1">管理员列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>商品分类的管理</span>
            </template>
            <el-menu-item index="1-2">分类列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>商品管理</span>
            </template>
            <el-menu-item index="1-3">商品列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="1-4">用户列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="5">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>订单管理</span>
            </template>
            <el-menu-item index="1-5">订单列表</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>Main</el-main>
    </el-container>
  </el-container>
</template>

<script setup>

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