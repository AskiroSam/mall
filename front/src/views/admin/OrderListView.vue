<template>
  <el-row>
    <el-col :span="24">
      <el-card style="opacity: 0.9;">
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item style="float: right;">
            <el-select
                v-model="condition.status"
                clearable placeholder="请选择状态"
                :value-on-clear="null"
                style="width: 200px;"
                @change="selectByPage(1)">
              <el-option label="未支付" value="0" />
              <el-option label="已支付" value="1" />
              <el-option label="已发货" value="2" />
              <el-option label="已收货" value="3" />
              <el-option label="退货退款" value="4" />
              <el-option label="退换货" value="5" />
              <el-option label="仅退款" value="6" />
              <el-option label="售后" value="7" />
              <el-option label="其它" value="8" />
            </el-select>
          </el-form-item>
        </el-form>
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="快递单号" width="200px" />
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
          <!--<el-table-column prop="express" label="快递单号" />-->
          <el-table-column label="支付方式">
            <template #default="scope">
              <el-tag type="primary" v-if="scope.row.payType == 0">余额支付</el-tag>
              <el-tag type="success" v-if="scope.row.payType == 1">支付宝</el-tag>
              <el-tag type="warning" v-if="scope.row.payType == 2">微信</el-tag>
              <el-tag type="danger" v-if="scope.row.payType == 3">银联</el-tag>
              <el-tag type="danger" v-if="scope.row.payType == 4">其它</el-tag>
            </template>
          </el-table-column>
          <!--<el-table-column prop="addr.address" label="地址" />-->
          <el-table-column prop="addrDetail" label="地址" width="500px" />
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
              <el-button size="small" type="success" @click="getOrderDetailList(scope.row.id)">订单详情</el-button>
              <!--<el-popconfirm title="你确定要删除该订单吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteOrder(scope.row.id)">-->
              <!--  <template #reference>-->
              <!--    <el-button size="small" type="danger">删除</el-button>-->
              <!--  </template>-->
              <!--</el-popconfirm>-->
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
  <el-dialog v-model="orderDetailListShow" title="订单详情" width="40%">
      <el-table :data="orderDetailList" style="width: 100%">
        <el-table-column prop="goods.name" label="商品" width="180" />
        <el-table-column prop="" label="商品图片" width="120">
          <template #default="scope">
            <div v-if="scope.row.goods.picList.length > 0">
              <img :src="`${SERVER_ADDR}/goods/pic/${scope.row.goods.picList[0].url}`" alt="商品图片" style="width: 100%; height: 100px;" />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="count" label="数量" width="180" />
        <el-table-column prop="price" label="价格" width="180" />

      </el-table>
  </el-dialog>
  <!--订单列表结束-->


</template>

<script setup>
//分页搜索
import orderApi from "@/api/orderApi.js";
import {ref, shallowRef} from "vue";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import {ElMessage} from "element-plus";


//服务器地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//是否显示订单详情列表对话框
const orderDetailListShow = ref(false);
//订单列表
const orderDetailList = ref([]);
//是否显示订单修改对话框
// const updateOrderDialog = ref(false);

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
// const orderUpdate = ref({
//   id: '',
//   userId: '',
//   express: '',
//   payType: 0,
//   addrId: 0,
//   addrDetail: null,
//   status: null,
//   orderDetailList: []
// });




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

//删除订单
// function deleteOrder(id) {
//   orderApi.delete(id)
//       .then(resp => {
//         if (resp.code == 10000) {
//           ElMessage.success(resp.msg);
//           //查询第一页
//           selectByPage(1);
//         } else {
//           ElMessage.error(resp.msg);
//         }
//       })
// }


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
        console.log(pageInfo.value)
      });
}
selectByPage(1);
</script>

<style scoped>

</style>