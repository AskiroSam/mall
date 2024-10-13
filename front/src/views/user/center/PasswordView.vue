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
            <el-col class="main_text">选择要修改的密码~</el-col>
          </el-row>
        </div>
        <div class="right">
          <el-row><div class="box">账户安全</div></el-row>
          <el-row>
            <div class="pwd" @click="selectById(user.id)">
              <el-icon class="lock"><Lock /></el-icon>
              登录密码
              <el-icon class="arrowRight"><ArrowRight /></el-icon>
            </div>
          </el-row>
          <el-row>
            <div class="pwd" @click="selectByPayId(user.id)">
              <el-icon class="lock"><Lock /></el-icon>
              支付密码
              <el-icon class="arrowRight"><ArrowRight /></el-icon>
            </div>
          </el-row>
        </div>
      </div>
    </div>
  </el-card>

  <!-- 修改用户登录密码对话框开始 -->
  <el-dialog v-model="updatePwdDialogShow" title="登陆密码" width="550">
    <el-form>
      <el-form-item label="原密码:" label-width="18%">
        <el-input v-model="oldPwd" placeholder="请输入原密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="新密码:" label-width="18%">
        <el-input v-model="newPwd" placeholder="请输入新密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="确认新密码:" label-width="18%">
        <el-input v-model="testNewPwd" placeholder="请输入新密码" autocomplete="off" style="width: 300px" @blur="checkLogin" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updatePwdDialogShow = false">取消</el-button>
        <el-button type="primary" @click="updatePwd" :disabled="!isPasswordMatching">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改用户登录密码对话框结束 -->

  <!-- 修改用户支付密码对话框开始 -->
  <el-dialog v-model="updatePayDialogShow" title="支付密码" width="500">
    <el-form>
      <el-form-item label="原密码:" label-width="18%" prop="sno">
        <el-input v-model="oldPay" placeholder="请输入原密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="新密码:" label-width="18%" prop="sno">
        <el-input v-model="newPay" placeholder="请输入新密码" autocomplete="off" style="width: 300px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updatePayDialogShow = false">取消</el-button>
        <el-button type="primary" @click="updatePay">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改用户支付密码对话框结束 -->


</template>

<script setup>


//保存用户信息
import {computed, ref} from "vue";
import userApi from "@/api/userApi.js";
import { ElMessage } from 'element-plus';
import {ArrowRight, Lock} from "@element-plus/icons-vue";

//存放用户信息
const user = ref({});
//存放用户输入的旧登录密码
const oldPwd = ref();
//存放用户输入的旧支付密码
const oldPay = ref();
//存放用户输入的新登录密码
const newPwd = ref();
//存放确认的新登录密码
const testNewPwd = ref();
//存放用户输入的新支付密码
const newPay = ref();
//修改密码对话框是否显示
const updatePwdDialogShow = ref(false);
//修改支付密码对话框是否显示
const updatePayDialogShow = ref(false);

const isPasswordMatching = ref(false);

//被修改的用户信息
const userUpdate = ref({
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

//被修改的用户的密码
const userUpdatePwd = ref({
  id: 0,
  password: ''
});

//被修改的用户的支付密码
const userUpdatePay = ref({
  id: 0,
  payPassword: ''
});

//检查两次登录密码
function checkLogin() {
  if (testNewPwd.value != newPwd.value) {
    isPasswordMatching.value = false;
    ElMessage.error("两次输入的密码不一致，请重新输入")
    testNewPwd.value = null;
  } else {
    isPasswordMatching.value = true;
  }
}

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
        oldPwd.value = null;
        newPwd.value = null;
        testNewPwd.value = null;
        updatePwdDialogShow.value = true;
      });
}

//根据id查询被修改用户的支付密码
function selectByPayId(id) {
  userApi.selectById(id)
      .then(resp => {
        userUpdate.value = resp.data;
        updatePayDialogShow.value = true;
      });
}

//修改登录密码
function updatePwd() {
  userUpdatePwd.value.id = userUpdate.value.id;
  userUpdatePwd.value.password = oldPwd.value;
  userApi.updatePwd(userUpdatePwd.value)
      .then(resp => {
        if (resp.code == 10000) {
          userUpdatePwd.value.password = newPwd.value;
          userApi.updatePwd(userUpdatePwd.value)
              .then(resp => {
                if (resp.code == 10000) {
                  ElMessage.success(resp.msg);
                  //隐藏对话框
                  updatePwdDialogShow.value = false;
                  //清空对话框
                  userUpdatePwd.value = ref({
                    id: 0,
                    password: null
                  });
                  oldPwd.value = null;
                  newPwd.value = null;
                  //查询第一页
                  getInfo();
                } else {
                  ElMessage.error(resp.msg);
                }
              });
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//修改支付密码
function updatePay() {
  userUpdatePay.value.id = userUpdate.value.id;
  userUpdatePay.value.payPassword = oldPay.value;
  userApi.updatePay(userUpdatePay.value)
      .then(resp => {
        if (resp.code == 10000) {
          userUpdatePay.value.payPassword = newPay.value;
          userApi.updatePay(userUpdatePay.value)
              .then(resp => {
                if (resp.code == 10000) {
                  ElMessage.success(resp.msg);
                  //隐藏对话框
                  updatePayDialogShow.value = false;
                  //清空对话框
                  userUpdatePay.value = ref({
                    id: 0,
                    password: null
                  });
                  oldPay.value = null;
                  newPay.value = null;
                  //查询第一页
                  getInfo();
                } else {
                  ElMessage.error(resp.msg);
                }
              });
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
  margin-top: 70px;
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


.right {
  float: right;
  margin-top: 20px;
}

.right .box {
  padding-left: 10px;
  font-size: 18px;
  font-weight: bold;
  border-left: 5px solid var(--theme-color);
}

.right .pwd {
  margin-top: 15px;
  padding: 10px;
  border-radius: 20px;
  width: 300px;
  background-color: #EEEEEE;
}

.right .pwd:hover {
  background-color: var(--theme-color);
  cursor: pointer;
}

.right .arrowRight {
  margin-left: 180px;
}

</style>