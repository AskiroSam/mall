<template>
  <el-row>
    <el-col :span="24">
      <el-card style="opacity: 0.9;">
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item>
            <el-button type="primary" @click="openUserAddDialog">添加</el-button>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-input v-model="condition.username" placeholder="请输入要搜索的名称" @input="selectByPage(1);" />
          </el-form-item>
          <el-form-item style="float: right;">
            <el-select
                v-model="condition.status"
                clearable placeholder="请选择状态"
                :value-on-clear="null"
                style="width: 200px;"
                @change="selectByPage(1)">
              <el-option label="未认证" value="0" />
              <el-option label="已认证" value="1" />
              <el-option label="禁用" value="2" />
              <el-option label="注销" value="3" />
            </el-select>
          </el-form-item>
        </el-form>
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="ID" width="60px" />
          <el-table-column prop="username" label="名称" />
          <el-table-column label="密码">
            <template #default="scope">
              {{ '******' }} <!-- 所有密码都显示固定长度的星号 -->
            </template>
          </el-table-column>
          <el-table-column label="支付密码">
            <template #default="scope">
              {{ '******' }} <!-- 所有密码都显示固定长度的星号 -->
            </template>
          </el-table-column>
          <el-table-column prop="realname" label="真实姓名" />
          <el-table-column prop="sex" label="性别" width="60px" />
          <el-table-column prop="idCard" label="身份证号" />
          <el-table-column prop="phone" label="电话" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column label="注册日期">
            <template #default="scope">
              {{ new Date(scope.row.regTime).toLocaleDateString() }}
            </template>
          </el-table-column>
          <el-table-column prop="money" label="余额" />
          <el-table-column label="状态">
            <template #default="scope">
              <el-tag type="primary" v-if="scope.row.status == 0">未认证</el-tag>
              <el-tag type="success" v-if="scope.row.status == 1">已认证</el-tag>
              <el-tag type="warning" v-if="scope.row.status == 2">禁用</el-tag>
              <el-tag type="danger" v-if="scope.row.status == 3">注销</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="330px">
            <template #default="scope">
              <el-button size="small" type="primary" @click="selectById(scope.row.id)">修改</el-button>
              <el-popconfirm title="你确定要删除该用户吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteUser(scope.row.id)">
                <template #reference>
                  <el-button size="small" type="danger">删除</el-button>
                </template>
              </el-popconfirm>
              <el-button size="small" type="warning" @click="resetPwd(scope.row.id)">重置密码</el-button>
              <el-button size="small" type="warning" @click="resetPayPwd(scope.row.id)">重置支付密码</el-button>
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


  <!-- 添加用户的对话框开始 -->
  <el-dialog v-model="addDialogShow" title="添加用户" width="500">
    <el-form>
      <el-form-item label="用户名:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.username" placeholder="请输入用户名" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="密码:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.password" placeholder="请输入密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="支付密码:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.payPassword" placeholder="请输入支付密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="真实姓名:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.realname" placeholder="请输入真实姓名" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="性别:" label-width="18%" prop="sgender">
        <el-radio-group v-model="userAdd.sex" style="width: 300px">
          <el-radio label="男" :value='男' size="large" />
          <el-radio label="女" :value='女' size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="身份证号:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.idCard" placeholder="请输入身份证号" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="手机号:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.phone" placeholder="请输入手机号" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="邮箱:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.email" placeholder="请输入邮箱" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="注册时间:" label-width="18%">
        <el-date-picker v-model="userAdd.regTime" type="datetime" disabled placeholder="请选择日期"  style="width: 300px" />
      </el-form-item>
      <el-form-item label="余额:" label-width="18%" prop="sno">
        <el-input v-model="userAdd.money" placeholder="请输入余额" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="状态:" label-width="18%" prop="sgender">
        <el-radio-group v-model="userAdd.status" style="width: 300px">
          <el-radio label="未认证" :value="0" size="large" />
          <el-radio label="已认证" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addDialogShow = false">取消</el-button>
        <el-button type="primary" @click="insert">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 添加用户的对话框结束 -->


  <!-- 修改用户的对话框开始 -->
  <el-dialog v-model="updateDialogShow" title="修改用户" width="500">
    <el-form>
      <el-form-item label="用户名:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.username" placeholder="请输入用户名" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="密码:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.password" type="password"  placeholder="请输入密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="支付密码:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.payPassword" type="password"  placeholder="请输入支付密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="真实姓名:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.realname" placeholder="请输入真实姓名" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="性别:" label-width="18%" prop="sgender">
        <el-radio-group v-model="userUpdate.sex" style="width: 300px">
          <el-radio label="男" :value='男' size="large" />
          <el-radio label="女" :value='女' size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="身份证号:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.idCard" placeholder="请输入身份证号" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="手机号:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.phone" placeholder="请输入手机号" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="邮箱:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.email" placeholder="请输入邮箱" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="注册时间:" label-width="18%">
        <el-date-picker v-model="userUpdate.regTime" type="date" placeholder="请选择日期"  style="width: 300px" />
      </el-form-item>
      <el-form-item label="余额:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.money" placeholder="请输入余额" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="状态:" label-width="18%" prop="sgender">
        <el-select v-model="userUpdate.status" placeholder="请选择状态" style="width: 300px">
          <el-option label="未认证" :value="0" />
          <el-option label="已认证" :value="1" />
          <el-option label="禁用" :value="2" />
          <el-option label="注销" :value="3" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateDialogShow = false">取消</el-button>
        <el-button type="primary" @click="update">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改用户的对话框结束 -->


</template>

<script setup>
import userApi  from "@/api/userApi.js";
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

const addDialogShow = ref(false);
const updateDialogShow = ref(false);


//被添加的用户信息
const userAdd = ref({
  username: null,
  password: null,
  payPassword: null,
  realname: null,
  sex: '男',
  idCard: null,
  phone: null,
  email: null,
  regTime: null,
  money: 0,
  status: 0,
});

//被修改的用户信息
const userUpdate = ref({
  username: null,
  password: '',
  payPassword: null,
  realname: null,
  sex: '',
  idCard: null,
  phone: null,
  email: null,
  regTime: null,
  money: 0,
  status: 0,
});
//被修改的用户状态信息
const userStatusUpdate = ref({
  status: 0,
});
//根据id查询被修改用户的信息
function selectById(id) {
  userApi.selectById(id)
      .then(resp => {
        userUpdate.value = resp.data;
        updateDialogShow.value = true;
      });
}

//添加用户时间
function openUserAddDialog() {
  addDialogShow.value = true;
  userAdd.value.regTime = new Date(); // 设置当前时间为默认值
}
//添加
function insert() {
  userApi.insert(userAdd.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //隐藏对话框
          addDialogShow.value = false;
          //清空对话框
          userAdd.value = {
            username: null,
            password: null,
            payPassword: null,
            realname: null,
            sex: null,
            idCard: null,
            phone: null,
            email: null,
            regTime: null,
            money: 0,
            status: 0,
          };
          //查询第一页
          selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//重置密码
function resetPwd(id) {
  userApi.selectById(id)
      .then(resp => {
        if (resp.code == 10000) {
          userUpdate.value = resp.data;
          userUpdate.value.password = "123";
          userApi.update(userUpdate.value)
              .then(resp => {
                if (resp.code == 10000) {
                  ElMessage.success("重置密码成功");
                  //清空
                  userUpdate.value = {
                    username: null,
                    password: '',
                    payPassword: null,
                    realname: null,
                    sex: null,
                    idCard: null,
                    phone: null,
                    email: null,
                    regTime: null,
                    money: 0,
                    status: 0,
                  };
                  //查询第一页
                  selectByPage(1);
                } else {
                  ElMessage.error(resp.msg);
                }
              })
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//重置支付密码
function resetPayPwd(id) {
  userApi.selectById(id)
      .then(resp => {
        if (resp.code == 10000) {
          userUpdate.value = resp.data;
          userUpdate.value.payPassword = "123456";
          userApi.update(userUpdate.value)
              .then(resp => {
                if (resp.code == 10000) {
                  ElMessage.success("重置支付密码成功");
                  //清空
                  userUpdate.value = {
                    username: null,
                    password: '',
                    payPassword: null,
                    realname: null,
                    sex: null,
                    idCard: null,
                    phone: null,
                    email: null,
                    regTime: null,
                    money: 0,
                    status: 0,
                  };
                  //查询第一页
                  selectByPage(1);
                } else {
                  ElMessage.error(resp.msg);
                }
              })
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//修改
function update() {
  userApi.update(userUpdate.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //隐藏对话框
          updateDialogShow.value = false;
          //查询第一页
          selectByPage(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//停用管理员
function setStatus(id) {
  userApi.setStatus(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(1)
          //刷新部门人数
          userApi.selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//删除用户
function deleteUser(id) {
  userApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(1)
          //刷新部门人数
          userApi.selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
//分页查询
function selectByPage(pageNum) {
  userApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}
selectByPage(1);
</script>

<style scoped>

</style>