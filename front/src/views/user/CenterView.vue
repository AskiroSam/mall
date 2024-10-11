<template>
  <el-container>
    <el-aside>
      <div class="title">个人中心</div>
      <ul class="menuList">
        <li><RouterLink to="/">我的订单</RouterLink></li>
        <li><RouterLink to="/">我的收藏</RouterLink></li>
        <li><RouterLink to="/">个人信息</RouterLink></li>
        <li><RouterLink to="/">修改密码</RouterLink></li>
        <li><RouterLink to="/">地址管理</RouterLink></li>
        <li><RouterLink to="/">账户充值</RouterLink></li>
      </ul>
    </el-aside>
    <el-main>
      <RouterView />
    </el-main>
  </el-container>
</template>

<script setup>

</script>

<style scoped>
.el-aside {
  padding: 20px;
  width: 150px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.menuList li {
  margin: 10px 0;
}

.menuList li a{
  color: #333;
  font-size: 14px;
}
</style>