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
            <el-col class="main_text">账户余额：{{user.money}}</el-col>
          </el-row>
        </div>
        <div class="right">
          <el-row><div class="box">账户金额管理</div></el-row>
          <el-row>
            <div class="pwd" @click="selectById(user.id)">
              <el-icon class="lock"><money /></el-icon>
              充值入口
              <el-icon class="arrowRight"><ArrowRight /></el-icon>
            </div>
            <div class="pwd" @click="selectByOutId(user.id)">
              <el-icon class="lock"><money /></el-icon>
              提现入口
              <el-icon class="arrowRight"><ArrowRight /></el-icon>
            </div>
          </el-row>
        </div>
      </div>
    </div>
  </el-card>

  <!-- 充值对话框开始 -->
  <el-dialog v-model="updateMoneyDialogShow" title="余额管理" width="500">
    <el-form>
      <el-form-item label="充值金额:" label-width="18%" prop="sno">
        <el-input v-model="inMoney" placeholder="请输入充值金额" autocomplete="off" style="width: 300px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateMoneyDialogShow = false">取消</el-button>
        <el-button type="primary" @click="updateInMoney">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 充值对话框结束 -->

  <!-- 提现对话框开始 -->
  <el-dialog v-model="updateMoneyOutDialogShow" title="余额管理" width="500">
    <el-form>
      <el-form-item label="提现金额:" label-width="18%" prop="sno">
        <el-input v-model="outMoney" placeholder="请输入提现金额" autocomplete="off" style="width: 300px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateMoneyOutDialogShow = false">取消</el-button>
        <el-button type="primary" @click="updateOutMoney">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 提现对话框结束 -->

</template>

<script setup>
import {ArrowRight, Money} from "@element-plus/icons-vue";
import {ref} from "vue";
import userApi from "@/api/userApi.js";
import {ElMessage} from "element-plus";

//存放用户信息
const user = ref({});
//充值的对话框是否显示
const updateMoneyDialogShow = ref(false);
//提现的对话框是否显示
const updateMoneyOutDialogShow = ref(false);
//存放充值金额
const inMoney = ref();
//存放提现金额
const outMoney = ref();

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
//被修改的用户的金额
const userUpdateMoney = ref({
  id: 0,
  money: ''
});

//根据id查询充值信息
function selectById(id) {
  userApi.selectById(id)
      .then(resp => {
        userUpdate.value = resp.data;
        updateMoneyDialogShow.value = true;
      });
}

//根据id查询提现信息
function selectByOutId(id) {
  userApi.selectById(id)
      .then(resp => {
        userUpdate.value = resp.data;
        updateMoneyOutDialogShow.value = true;
      });
}

//充值金额
function updateInMoney() {
  userUpdateMoney.value.id = userUpdate.value.id;
  userUpdateMoney.value.money = inMoney.value;
  userApi.updateInMoney(userUpdateMoney.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //隐藏对话框
          updateMoneyDialogShow.value = false;
          //清空对话框
          userUpdateMoney.value = ref({
            id: 0,
            money: ''
          });
          inMoney.value = null;
          //查询第一页
          getInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//提现金额
function updateOutMoney() {
  userUpdateMoney.value.id = userUpdate.value.id;
  userUpdateMoney.value.money = outMoney.value;
  userApi.updateOutMoney(userUpdateMoney.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success("提现成功");
          //隐藏对话框
          updateMoneyOutDialogShow.value = false;
          //清空对话框
          userUpdateMoney.value = ref({
            id: 0,
            money: ''
          });
          outMoney.value = null;
          //查询第一页
          getInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

//获取用户信息
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        user.value = resp.data;
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