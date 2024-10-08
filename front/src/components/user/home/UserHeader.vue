<template>
  <!--头部导航-->
 <div class="nav_bg">
   <div class="center">
     <el-row>
       <el-col :span="20">
         <ul>
           <li><RouterLink to="/user/index">商城首页</RouterLink></li>
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
           <li><RouterLink to="/user/login">登录</RouterLink></li>
           <li><RouterLink to="/user/reg">注册</RouterLink></li>
         </ul>
       </el-col>
     </el-row>
   </div>
 </div>

  <!--头部搜索-->
  <div class="center">
    <el-row>
      <!--分成24等份，span值的合要小于等于24-->
      <el-col :span="16">
        <img src="/src/assets/mi.png" style="height: 90px" />
      </el-col>
      <el-col :span="8">
        <div class="search">
          <!--搜索按钮-->
          <el-button class="search_btn">
            <el-icon><Search /></el-icon>
          </el-button>
          <!--搜索框-->
          <div>
            <el-input class="search_input" type="text" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>

  <!--上架的第一级分类-->
  <div class="center">
    <ul class="category">
      <li v-for="(category, index) in parentList" :key="index"><RouterLink :to="`/user/search/${category.id}`">{{category.name}}</RouterLink></li>
    </ul>
  </div>

</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import categoryApi from "@/api/categoryApi.js";
import {ref} from "vue";

const parentList = ref([]);
//1.获取夫分类 2.上架
function getParent() {
  const condition = {
    parentId: 0,
    recom: 1,
    status: 1,
  }

  categoryApi.selectByPage(condition)
      .then(resp => {
        parentList.value = resp.data;
      })
}
getParent();
</script>



<style scoped>
  .nav_bg {
    background-color: var(--theme-bg-color);
    line-height: 40px;
  }

  .nav_bg ul li {
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

  .search {
    padding-top: 20px;
  }

  .search_input {
    width: 260px;
    float: right;
  }

  .search_btn {
    float: right;
    color: white;
    margin-left:5px;
    border-radius: 0;
    border: 0;
    background-color: var(--theme-color);
  }

  .search_btn:hover {
    background-color: #FEDFE1;
  }

  .category {
    background-color: var(--theme-color);
    line-height: 30px;
  }

  .category li {
    float: left;
    padding: 0 10px;
  }

  .category li a {
    color: #fff;
  }

  .category li:hover {
    background-color: #FEDFE1;
  }

</style>