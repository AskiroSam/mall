<template>
 <div class="nav_bg">
   <div class="center">
     <el-row>
       <el-col :span="20">
         <ul>
           <li><RouterLink to="">商城首页</RouterLink></li>
           <li><RouterLink to="">商城首页</RouterLink></li>
           <li><RouterLink to="">商城首页</RouterLink></li>
           <li><RouterLink to="">商城首页</RouterLink></li>
           <li><RouterLink to="">商城首页</RouterLink></li>
           <li><RouterLink to="">商城首页</RouterLink></li>
           <li><RouterLink to="">商城首页</RouterLink></li>
         </ul>
       </el-col>
       <el-col :span="4">
         <ul class="user">
           <li><RouterLink to="">注册</RouterLink></li>
           <li><RouterLink to="">登录</RouterLink></li>
         </ul>
       </el-col>
     </el-row>
   </div>
 </div>
</template>

<script setup>

</script>



<style scoped>
  .nav_bg {
    background-color: #333;
    line-height: 40px;
  }

  ul li {
    float: left;
    margin-right: 20px;
  }
  
  a {
    color: #999;
  }

  .user {
    float: right;
  }
  
  a:hover {
    color: #fff;
  }
</style>