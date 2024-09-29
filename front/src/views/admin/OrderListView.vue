<template>
  <el-row>
    <el-col :span="24">
      <el-card style="opacity: 0.9;">
        <!--<el-form :inline="true" class="demo-form-inline">-->
        <!--  <el-form-item>-->
        <!--    <el-button type="primary" @click="openUserAddDialog">添加</el-button>-->
        <!--  </el-form-item>-->
        <!--  <el-form-item style="float: right;">-->
        <!--    <el-input v-model="condition.username" placeholder="请输入要搜索的名称" @input="selectByPage(1);" />-->
        <!--  </el-form-item>-->
        <!--  <el-form-item style="float: right;">-->
        <!--    <el-select-->
        <!--        v-model="condition.status"-->
        <!--        clearable placeholder="请选择状态"-->
        <!--        :value-on-clear="null"-->
        <!--        style="width: 200px;"-->
        <!--        @change="selectByPage(1)">-->
        <!--      <el-option label="未认证" value="0" />-->
        <!--      <el-option label="已认证" value="1" />-->
        <!--      <el-option label="禁用" value="2" />-->
        <!--      <el-option label="注销" value="3" />-->
        <!--    </el-select>-->
        <!--  </el-form-item>-->
        <!--</el-form>-->
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="ID" width="200px" />
          <el-table-column prop="user" label="用户" />
          <el-table-column prop="createTime" label="创建时间" />
          <el-table-column prop="express" label="快递单号" />
          <el-table-column prop="payType" label="支付方式" />
          <el-table-column prop="addrId" label="地址" />
          <el-table-column prop="addrDetail" label="当时地址" />
          <el-table-column label="状态">
            <template #default="scope">
              <el-tag type="primary" v-if="scope.row.status == 0">未支付</el-tag>
              <el-tag type="success" v-if="scope.row.status == 1">已支付</el-tag>
              <el-tag type="warning" v-if="scope.row.status == 2">已发货</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 3">已收货</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 4">退货退款</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 5">退换货</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 6">仅退款</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 7">售后</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 8">其它</el-tag>
            </template>
          </el-table-column>
          <!--<el-table-column label="操作" width="250px">-->
          <!--  <template #default="scope">-->
          <!--    <el-button size="small" type="primary" @click="selectById(scope.row.id)">修改</el-button>-->
          <!--    <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteUser(scope.row.id)">-->
          <!--      <template #reference>-->
          <!--        <el-button size="small" type="danger">删除</el-button>-->
          <!--      </template>-->
          <!--    </el-popconfirm>-->
          <!--  </template>-->
          <!--</el-table-column>-->
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
//分页搜索
import orderApi from "@/api/orderApi.js";
import {ref} from "vue";

//搜索条件
const condition = ref({
  name: null,
  addrDetail: null,
  status: null,

})
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

function selectByPage(pageNum) {
  orderApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      });
}
selectByPage(1);
</script>

<style scoped>

</style>