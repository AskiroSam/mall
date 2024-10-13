<template>
  <el-card>
    <div>
      <div class="header">
        <div class="left">
          <el-image src="/src/images/tx.jpg" class="leftImg" />
        </div>
        <div class="main">
          <el-row>
            <el-col class="main_userName">{{user.username}}</el-col>
            <el-col class="main_text">欢迎光临~</el-col>
            <el-col class="main_router"><a @click="selectById(user.id)">修改个人信息></a></el-col>
          </el-row>
        </div>
        <div class="right">
          <el-row>
            <el-col class="right_text">
              账户状态：<span style="color:var(--theme-color);">{{user.status === 0 ? '未认证' : user.status === 1 ? '已认证' : user.status === 2 ? '禁用' : '注销'}}</span>
            </el-col>
            <el-col class="right_text">
              绑定手机：<span>{{user.phone}}</span>
            </el-col>
            <el-col class="right_text">
              绑定邮箱：<span>{{user.email}}</span>
            </el-col>
            <el-col class="right_text">
              余额：<span>{{user.money}}</span>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="main">

      </div>
    </div>
  </el-card>

  <!-- 修改用户的对话框开始 -->
  <el-dialog v-model="updateDialogShow" title="详细信息" width="500">
    <el-form>
      <el-form-item label="用户名:" label-width="18%" prop="sno">
        <el-input v-model="userUpdate.username" placeholder="请输入用户名" autocomplete="off" style="width: 300px" />
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
        <el-select v-model="userUpdate.status" placeholder="请选择状态" style="width: 300px" disabled>
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
import userApi from "@/api/userApi.js";
import { ElMessage } from 'element-plus';
import {ref} from "vue";

//保存用户信息
const user = ref({
  username: '',
  status: null,
  phone: '',
  email: ''
});
//修改个人信息对话框是否显示
const updateDialogShow = ref(false);

//被修改的用户信息
const userUpdate = ref({
  username: null,
  password: 123,
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

//获取用户信息
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        user.value = resp.data;
      })
}

//根据id查询被修改用户的信息
function selectById(id) {
  userApi.selectById(id)
      .then(resp => {
        userUpdate.value = resp.data;
        updateDialogShow.value = true;
      });
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
          getInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}


getInfo();
</script>

<style scoped>
.header {
  display: flex;
}

.left {
  margin-top: 20px;
  margin-left: 50px;
  float: left;
  align-items: center;
  justify-content: space-between;
}

.leftImg {
  width: 160px;
  height: 160px;
  border: 3px double #8c939d;
  border-radius: 50%;
}

.main_userName {
  margin-top: 50px;
  margin-left: 20px;
  font-size: 25px;
  font-weight: bold;
}

.main_text {
  margin-top: 5px;
  margin-left: 20px;
  font-size: 15px;
  color: #999999;
}

.main_router {
  margin-top: 15px;
  margin-left: 20px;
  font-size: 12px;
  color: var(--theme-color);
}

.right {
  float: right;
  margin-top: 20px;
}

.right_text {
  margin-top: 20px;
  margin-left: 80px;
  font-size: 14px;
  color: #999999;
}

a:hover{
  cursor: pointer;
}
</style>