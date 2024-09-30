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
          <el-table-column prop="user" label="用户">
            <template #default="scope">
              {{ scope.row.user.username }}
            </template>
          </el-table-column>
          <el-table-column label="注册日期">
            <template #default="scope">
              {{ new Date(scope.row.createTime).toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }) }}
            </template>
          </el-table-column>
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

          <el-table-column label="操作" width="250px">
            <template #default="scope">
              <el-button size="small" type="success" @click="getOrderDetailList(scope.row.id)">订单列表</el-button>
              <el-button size="small" type="primary" @click="">修改</el-button>
              <el-popconfirm title="你确定要删除该订单吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="">
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


  <!--订单列表开始-->
  <el-dialog v-model="orderDetailListShow" title="订单详情列表">
      <el-table :data="orderDetailList" style="width: 100%">
        <el-table-column prop="goodsId" label="商品" width="180" />
        <el-table-column prop="count" label="数量" width="180" />
        <el-table-column prop="price" label="价格" width="180" />

      </el-table>
  </el-dialog>
  <!--订单列表结束-->


  <!-- 订单详情对话框开始 -->
  <el-dialog v-model="detailDialogShow" title="商品详情">
    <div v-if="detailDialogShow" style="border: 1px solid #ccc">
      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="editorRef"
          :defaultConfig="toolbarConfig"
          :mode="mode"
      />
      <Editor
          style="height: 500px; overflow-y: hidden;"
          v-model="orderUpdate.orderDetailList"
          :defaultConfig="editorConfig"
          :mode="mode"
          @onCreated="handleCreated"
      />
    </div>
    <div class="dialog-footer">
      <el-button @click="detailDialogShow = false">取消</el-button>
      <el-button type="primary" @click="">确认</el-button>
    </div>
  </el-dialog>
  <!--订单详情对话框结束-->


</template>

<script setup>
//分页搜索
import orderApi from "@/api/orderApi.js";
import {ref, shallowRef} from "vue";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";

//是否显示订单详情列表对话框
const orderDetailListShow = ref(false);
//是否显示商品详情对话框
const detailDialogShow = ref(false);
//订单列表
const orderDetailList = ref([]);

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

//被修改的商品信息
const orderUpdate = ref({
  id: '',
  userId: '',
  express: '',
  payType: 0,
  addrId: 0,
  addrDetail: null,
  status: null,
  orderDetailList: []
});


//显示商品详情
function showDetailDialog(order) {
  orderUpdate.value.id = order.id;
  orderUpdate.value.orderDetailList = order.orderDetailList;
  detailDialogShow.value = true;
}


/*----------------------详情开始----------------------*/
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const mode = ref('default');

const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...',
}
const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
/*----------------------详情结束----------------------*/

//获取订单详情列表
function getOrderDetailList(id) {
  orderApi.selectById(id)
      .then(resp => {
        if (resp.code == 10000) {
          orderDetailList.value = resp.data.orderDetailList;
          console.log(orderDetailList.value)
        }
        orderDetailListShow.value = true;
      })
}


//分页查询
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