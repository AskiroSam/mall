<template>
  <el-row>
    <el-col :span="24">
      <el-card style="opacity: 0.9;">
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item>
            <el-button type="primary" @click="openAdminAddDialog">添加</el-button>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-input v-model="condition.username" placeholder="请输入要搜索的名称" @input="selectByPage(1);" />
          </el-form-item>
        </el-form>
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
              <el-switch
                  v-model="scope.row.status"
                  inline-prompt
                  :inactive-value="0"
                  :active-value="1"
                  inactive-text="禁用"
                  active-text="启用"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  @change = "chgStatus(scope.row.id, scope.row.status)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200px">
            <template #default="scope">
              <el-button size="small" type="primary" @click="restPwd(scope.row.id)">重置密码</el-button>
              <el-popconfirm title="你确定要删除该管理员吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteAdmin(scope.row.id)">
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

  <!-- 添加管理员对话框开始 -->
  <el-dialog v-model="adminAddDiaLogShow" title="添加管理员" width="500">
    <el-form ref="insertForm" :model="adminAdd" :rules="rules">
      <el-form-item label="用户名:" label-width="18%" prop="username">
        <el-input v-model="adminAdd.username" placeholder="请输入用户名" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="手机号:" label-width="18%" prop="phone">
        <el-input v-model="adminAdd.phone" placeholder="请输入手机号" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="邮箱:" label-width="18%" prop="email">
        <el-input v-model="adminAdd.email" placeholder="请输入邮箱" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="真实姓名:" label-width="18%" prop="realname">
        <el-input type="password" v-model="adminAdd.realname" placeholder="请输入真实姓名" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="注册时间:" label-width="18%" v-show="false">
        <el-date-picker v-model="adminAdd.createTime" type="datetime" disabled placeholder="请选择日期"  style="width: 300px" />
      </el-form-item>
      <el-form-item label="状态:" label-width="18%" prop="sgender">
        <el-radio-group v-model="adminAdd.status" style="width: 300px">
          <el-radio label="正常" :value="0" size="large" />
          <el-radio label="禁用" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="adminAddDiaLogShow = false">取消</el-button>
        <el-button type="primary" @click="insert">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 添加管理员对话框结束 -->


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

//添加管理员的信息
const adminAdd = ref({
  id: 0,
  username: null,
  password: null,
  phone: null,
  email: null,
  realname: null,
  createTime: null,
  status: 0,
});

//添加管理员对话框是否显示
const adminAddDiaLogShow = ref(false);

//非空验证规则
// 验证规则
const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '手机号不能为空', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' }
  ],
  realname: [
    { required: true, message: '真实姓名不能为空', trigger: 'blur' }
  ]
};
//验证
const insertForm = ref();


// 打开添加管理员对话框并设置默认创建时间
function openAdminAddDialog() {
  adminAddDiaLogShow.value = true;
  adminAdd.value.createTime = new Date(); // 设置当前时间为默认值
}
//添加管理员
function insert() {
  //validate根据规则进行表单验证，全部通过valid为true，否则为false
  insertForm.value.validate(valid => {
    if (valid) {
      adminApi.insert(adminAdd.value)
          .then(resp => {
            if (resp.code == 10000) {
              ElMessage.success(resp.msg);
              //隐藏对话框
              adminAddDiaLogShow.value = false;
              //清空对话框
              adminAdd.value = {
                id: 0,
                username: null,
                password: null,
                phone: null,
                email: null,
                realname: null,
                status: 0,
              };
              //查询第一页
              selectByPage(1);
            } else {
              ElMessage.error(resp.msg);
            }
          })
    }
  })

}
//停用管理员
function chgStatus(id, status) {
  const admin = {
    id,
    status
  }

  adminApi.chgInfo(admin)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum)
          //刷新部门人数
          // adminApi.selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//重置密码
function restPwd(id) {
  adminApi.restPwd(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum)
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