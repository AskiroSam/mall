<template>
  <el-container>

    <el-header>
      <UserHeader />
    </el-header>

    <el-main>Main</el-main>

    <el-footer>
      <UserFooter />
    </el-footer>

  </el-container>
</template>

<script setup>

import UserHeader from "@/components/user/home/UserHeader.vue";
import UserFooter from "@/components/user/home/UserFooter.vue";
</script>

<style scoped>
.el-header, .el-main, .el-footer {
  padding: 0;
}
</style>