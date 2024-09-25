<template>
  <el-row>
    <el-col :span="24">
      <el-card style="opacity: 0.9;">
        <!--<el-form :inline="true" class="demo-form-inline">-->
        <!--  <el-form-item>-->
        <!--    <el-button type="primary" @click="addDialogShow = true">添加</el-button>-->
        <!--  </el-form-item>-->
        <!--  <el-form-item style="float: right;">-->
        <!--    <el-input v-model="condition.name" placeholder="请输入要搜索的名称" @input="selectByPage(1);" />-->
        <!--  </el-form-item>-->
        <!--  <el-form-item style="float: right;">-->
        <!--    <el-select-->
        <!--        v-model="condition.status"-->
        <!--        clearable placeholder="请选择是否上架"-->
        <!--        :value-on-clear="null"-->
        <!--        style="width: 200px;"-->
        <!--        @change="selectByPage(1)">-->
        <!--      <el-option label="上架" value="1" />-->
        <!--      <el-option label="下架" value="0" />-->
        <!--    </el-select>-->
        <!--  </el-form-item>-->
        <!--  <el-form-item style="float: right;">-->
        <!--    <el-select-->
        <!--        v-model="condition.parentId"-->
        <!--        clearable placeholder="请选择夫分类"-->
        <!--        :value-on-clear="null"-->
        <!--        style="width: 200px;"-->
        <!--        @change="selectByPage(1)">-->
        <!--      <el-option label="没有夫分类" value="0" />-->
        <!--      <el-option v-for="(parent, item) in allParent" :key="item" :label="parent.name" :value="parent.id" />-->
        <!--    </el-select>-->
        <!--  </el-form-item>-->
        <!--</el-form>-->
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="ID" />
          <el-table-column prop="username" label="名称" />
          <el-table-column label="密码">
            <template #default="scope">
              {{ '******' }} <!-- 所有密码都显示固定长度的星号 -->
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="realname" label="真实姓名" />
          <el-table-column label="创建日期">
            <template #default="scope">
              {{ new Date(scope.row.createTime).toLocaleDateString() }}
            </template>
          </el-table-column>
          <el-table-column label="可用状态">
            <template #default="scope">
              <el-tag type="primary" v-if="scope.row.status == 1">禁用</el-tag>
              <el-tag type="warning" v-if="scope.row.status == 0">正常</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150px">
            <template #default="scope">
              <el-button size="small" type="primary" @click="setStatus(scope.row.id)">停用</el-button>
              <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteAdmin(scope.row.id)">
                <template #reference>
                  <el-button size="small" type="danger">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <el-row class="row-bg" justify="center" style="margin-top: 20px;">
          <el-pagination background layout="prev, pager, next"
                         v-model:current-page="pageInfo.pageNum"
                         :page-count="pageInfo.pages"
                         @update:current-page="selectByPage" />
        </el-row>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import adminApi from "@/api/adminApi.js";
import { ElMessage } from 'element-plus';
import {ref} from "vue";

//搜索的条件
const condition = ref({
  username: null,
  status: null
});
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

//停用管理员
function setStatus(id) {
  adminApi.setStatus(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(1)
          //刷新部门人数
          adminApi.selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//删除管理员
function deleteAdmin(id) {
  adminApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(1)
          //刷新部门人数
          adminApi.selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
//分页查询
function selectByPage(pageNum) {
  adminApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}
selectByPage(1);
</script>

<style scoped>

</style>