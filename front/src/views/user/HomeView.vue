<template>
  <el-container>

    <el-header>
      <UserHeader />
    </el-header>

    <el-main>
      <div class="center">
        <RouterView />
      </div>
    </el-main>

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
  height: auto;
}

.el-main {
  background-color: #EEE;
}

.el-footer {
  margin-top: 20px;
  padding: 10px 0;
  background-color: var(--theme-bg-color);
}

</style>