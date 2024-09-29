<template>
  <el-row>
    <el-col :span="24">
      <el-card style="opacity: 0.9;">
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item>
            <el-button type="primary" @click="addDialogShow = true">添加</el-button>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-input v-model="condition.name" placeholder="请输入要搜索的名称" @input="selectByPage(1);" />
          </el-form-item>
          <el-form-item style="float: right;">
            <el-select
                v-model="condition.status"
                clearable placeholder="请选择是否上架"
                :value-on-clear="null"
                style="width: 200px;"
                @change="selectByPage(1)">
              <el-option label="上架" value="1" />
              <el-option label="下架" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-cascader
                v-model="condition.categoryId"
                :options="allParent"
                :props="props"
                placeholder="请选择分类"
                :value-on-clear="null"
                clearable
                @change="selectByPage(1)"
            />
          </el-form-item>
        </el-form>
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="ID" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="dscp" label="描述" />
          <el-table-column prop="price" label="售价" />
          <el-table-column prop="markPrice" label="标价" />
          <el-table-column prop="purchasePrice" label="进价" />
          <el-table-column prop="color" label="颜色" />
          <el-table-column prop="version" label="版本" />
          <el-table-column prop="count" label="数量" />
          <el-table-column label="是否推荐">
            <template #default="scope">
              <el-switch
                  v-model="scope.row.recom"
                  inline-prompt
                  :inactive-value="0"
                  :active-value="1"
                  inactive-text="不推荐"
                  active-text="推荐"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  @change = "chgRecom(scope.row.id, scope.row.recom)"
              />
            </template>
          </el-table-column>
          <el-table-column label="分类">
            <template #default="scope">
              <el-text class="mx-1" type="primary">{{scope.row.category.name}}</el-text>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="评分"  />
          <el-table-column label="是否推荐">
            <template #default="scope">
              <el-switch
                  v-model="scope.row.status"
                  inline-prompt
                  :inactive-value="0"
                  :active-value="1"
                  inactive-text="下架"
                  active-text="上架"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  @change = "chgStatus(scope.row.id, scope.row.status)"
              />
            </template>
          </el-table-column>
          <el-table-column label="图片">
            <template #default="scope">
              <el-popover placement="right" trigger="hover" v-if="scope.row.picList.length > 0">
                <template #reference>
                  <el-image style="width: 40px; height: 40px" :src="SERVER_ADDR + '/goods/pic/' + scope.row.picList[0].url" />
                </template>
                <el-image v-for="(pic, index) in scope.row.picList" :key="index" style="width: 40px; height: 40px; margin-right: 5px" :src="SERVER_ADDR + '/goods/pic/' + pic.url" />
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200px">
            <template #default="scope">
              <el-button size="small" type="success" @click="showDetailDialog(scope.row)">详情</el-button>
              <el-button size="small" type="primary" @click="selectById(scope.row.id)">修改</el-button>
              <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteGoods(scope.row.id)">
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


  <!--添加商品的对话框开始-->
  <el-dialog v-model="addDialogShow" title="添加商品" width="500">
    <el-form>
      <el-form-item label="分类:" label-width="18%" prop="did">
        <el-cascader
            v-model="goodsAdd.categoryId"
            :options="allParent"
            :props="props"
            placeholder="请选择分类"
            :value-on-clear="null"
            clearable
            style="width: 300px"
        />
      </el-form-item>
      <el-form-item label="名称:" label-width="18%" prop="name">
        <el-input v-model="goodsAdd.name" placeholder="请输入名称" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="描述:" label-width="18%" prop="dscp">
        <el-input v-model="goodsAdd.dscp" placeholder="请输入描述" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="售价:" label-width="18%" prop="price">
        <el-input v-model="goodsAdd.price" clearable placeholder="请输入售价" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="标价:" label-width="18%" prop="markPrice">
        <el-input v-model="goodsAdd.markPrice" placeholder="请输入标价" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="进价:" label-width="18%" prop="purchasePrice">
        <el-input v-model="goodsAdd.purchasePrice" placeholder="请输入进价" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="颜色:" label-width="18%" prop="color">
        <el-input v-model="goodsAdd.color" placeholder="请输入颜色" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="版本:" label-width="18%" prop="version">
        <el-input v-model="goodsAdd.version" placeholder="请输入版本" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="数量:" label-width="18%" prop="count">
        <el-input v-model="goodsAdd.count" placeholder="请输入数量" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="是否推荐:" label-width="18%" prop="sgender">
        <el-radio-group v-model="goodsAdd.recom" style="width: 300px">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架:" label-width="18%" prop="sgender">
        <el-radio-group v-model="goodsAdd.status" style="width: 300px">
          <el-radio label="下架" :value="0" size="large" />
          <el-radio label="上架" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片:" label-width="20%">
        <el-upload :action="SERVER_ADDR + '/goods/upload'"
                   v-model:file-list="goodsAdd.picList"
                   name="pic"
                   list-type="picture-card"
                   :headers="headers"
                   :on-success="picAddUploadSuccess"
                   :before-upload="beforeAvatarUpload" >
          <el-icon class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addDialogShow = false">取消</el-button>
        <el-button type="primary" @click="insert">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!--添加商品的对话框结束-->


  <!-- 修改商品的对话框开始 -->*
  <el-dialog v-model="updateDialogShow" title="修改商品" width="500">
    <el-form>
      <el-form-item label="分类:" label-width="18%" prop="did">
        <el-cascader
            v-model="goodsUpdate.categoryId"
            :options="allParent"
            :props="props"
            placeholder="请选择分类"
            :value-on-clear="null"
            clearable
            style="width: 300px"
        />
      </el-form-item>
      <el-form-item label="名称:" label-width="18%" prop="name">
        <el-input v-model="goodsUpdate.name" placeholder="请输入名称" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="描述:" label-width="18%" prop="dscp">
        <el-input v-model="goodsUpdate.dscp" placeholder="请输入描述" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="售价:" label-width="18%" prop="price">
        <el-input v-model="goodsUpdate.price" clearable placeholder="请输入售价" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="标价:" label-width="18%" prop="markPrice">
        <el-input v-model="goodsUpdate.markPrice" placeholder="请输入标价" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="进价:" label-width="18%" prop="purchasePrice">
        <el-input v-model="goodsUpdate.purchasePrice" placeholder="请输入进价" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="颜色:" label-width="18%" prop="color">
        <el-input v-model="goodsUpdate.color" placeholder="请输入颜色" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="版本:" label-width="18%" prop="version">
        <el-input v-model="goodsUpdate.version" placeholder="请输入版本" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="数量:" label-width="18%" prop="count">
        <el-input v-model="goodsUpdate.count" placeholder="请输入数量" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="是否推荐:" label-width="18%" prop="sgender">
        <el-radio-group v-model="goodsUpdate.recom" style="width: 300px">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架:" label-width="18%" prop="sgender">
        <el-radio-group v-model="goodsUpdate.status" style="width: 300px">
          <el-radio label="下架" :value="0" size="large" />
          <el-radio label="上架" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片:" label-width="20%">
        <el-upload :action="SERVER_ADDR + '/goods/upload'"
                   v-model:file-list="goodsUpdate.picList"
                   name="pic"
                   list-type="picture-card"
                   :headers="headers"
                   :on-success="picUpdateUploadSuccess"
                   :before-upload="beforeAvatarUpload" >
          <el-icon class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateDialogShow = false">取消</el-button>
        <el-button type="primary" @click="update">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改商品的对话框结束 -->

  <!-- 商品详情对话框开始 -->
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
            v-model="goodsUpdate.detail"
            :defaultConfig="editorConfig"
            :mode="mode"
            @onCreated="handleCreated"
        />
      </div>
    <div class="dialog-footer">
      <el-button @click="detailDialogShow = false">取消</el-button>
      <el-button type="primary" @click="updateDetail">确认</el-button>
    </div>
  </el-dialog>
  <!--商品详情对话框结束-->


</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css'
import categoryApi from "@/api/categoryApi.js";
import goodsApi from "@/api/goodsApi.js";
import {ref, onBeforeUnmount, shallowRef, onMounted, computed} from "vue";
import {ElMessage} from "element-plus";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import { useTokenStore } from "@/stores/token.js";



const tokenStore = useTokenStore();
const headers = computed(() => {
  const token = tokenStore.tokenStr;
  return {
    token: token
  }
})

//服务器路径
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

//夫分类
const allParent = ref([]);

//级联选择器的配置选项
const props = {
  //给级联选择器绑定数据和官方不一致时，需要设置
  label: "name", //页面显示的信息
  value: "id", //选中标签的值
  children: "childList", //子类
  emitPath: false
}

//搜索条件
const condition = ref({
  name: null,
  categoryId: null,
  status: null,

})

//被添加的商品信息
const goodsAdd = ref({
  name: '',
  dscp: '',
  price: null,
  markPrice: null,
  purchasePrice: null,
  color: null,
  version: null,
  count: null,
  recom: null,
  categoryId: null,
  status: null,
  picList: [] //url - 保存图片在服务器上的名字
});

//被修改的商品信息
const goodsUpdate = ref({
  id: 0,
  name: '',
  dscp: '',
  detail: '',
  price: null,
  markPrice: null,
  purchasePrice: null,
  color: null,
  version: null,
  count: null,
  recom: null,
  categoryId: null,
  status: null,
  picList: []
});

//是否显示添加的对话框
const addDialogShow = ref(false);
//是否显示修改的对话框
const updateDialogShow = ref(false);
//是否显示商品详情对话框
const detailDialogShow = ref(false);
//显示商品详情
function showDetailDialog(goods) {
  goodsUpdate.value.id = goods.id;
  goodsUpdate.value.detail = goods.detail;
  goodsUpdate.value.picList = goods.picList;
  console.log(goodsUpdate.value)
  detailDialogShow.value = true;
}

//添加商品
function insert() {
  //处理图片 realName -> url, 目的：让url属性保存图片名
  for (let i = 0; i < goodsAdd.value.picList.length; i++) {
    goodsAdd.value.picList[i].url = goodsAdd.value.picList[i].realName;
  }
  goodsApi.insert(goodsAdd.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //隐藏对话框
          addDialogShow.value = false;
          //清空对话框
          goodsAdd.value = {
            name: '',
            dscp: '',
            price: null,
            markPrice: null,
            purchasePrice: null,
            color: null,
            version: null,
            count: null,
            recom: null,
            categoryId: null,
            status: null,
            picList: []
          };
          //查询第一页
          selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      });
}
//上传图片之前的回调
function beforeAvatarUpload(rawFile) {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('图片仅支持jpg格式')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片不能超过2M')
    return false
  }
  return true
}
// 成功上传之后的回调
function picAddUploadSuccess(resp, uploadFile, uploadFiles) { //resp - 服务器的响应    uploadFile - 1.当前上传的图片 2.和pageList中对应的元素是绑定的
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    uploadFile.url = `${SERVER_ADDR.value}/goods/pic/${resp.data}`;
    uploadFile.realName = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}
//成功修改图片之后的回调
function picUpdateUploadSuccess(resp, uploadFile, uploadFiles) { //resp - 服务器的响应    uploadFile - 1.当前上传的图片 2.和pageList中对应的元素是绑定的
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    uploadFile.url = `${SERVER_ADDR.value}/goods/pic/${resp.data}`;
    uploadFile.realName = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}

//根据id查询被修改商品的信息
function selectById(id) {
  goodsApi.selectById(id)
      .then(resp => {
        goodsUpdate.value = resp.data;
        //处理图片
        for (let i = 0; i < goodsUpdate.value.picList.length; i++) {
          //保存图片真实的名字
          goodsUpdate.value.picList[i].realName = goodsUpdate.value.picList[i].url;
          //回显图片
          goodsUpdate.value.picList[i].url = `${SERVER_ADDR.value}/goods/pic/${goodsUpdate.value.picList[i].url}`;
        }

        updateDialogShow.value = true;
        console.log(goodsUpdate.value)
      });
}

//修改商品详情
function updateDetail() {
  goodsApi.update(goodsUpdate.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //隐藏对话框
          detailDialogShow.value = false;
          //重置
          goodsUpdate.value =  {
            id: 0,
            name: '',
            dscp: '',
            detail: '',
            price: null,
            markPrice: null,
            purchasePrice: null,
            color: null,
            version: null,
            count: null,
            recom: null,
            categoryId: null,
            status: null,
            picList: []
          };
          //查询第一页
          selectByPage(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
//修改
function update() {
  //处理图片
  for (let i = 0; i < goodsUpdate.value.picList.length; i++) {
    goodsUpdate.value.picList[i].url = goodsUpdate.value.picList[i].realName;
  }
  goodsApi.update(goodsUpdate.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //隐藏对话框
          updateDialogShow.value = false;
          //重置
          goodsUpdate.value =  {
            id: 0,
            name: '',
            dscp: '',
            detail: '',
            price: null,
            markPrice: null,
            purchasePrice: null,
            color: null,
            version: null,
            count: null,
            recom: null,
            categoryId: null,
            status: null,
            picList: []
          };
          //查询第一页
          selectByPage(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//是否推荐
function chgRecom(id, recom) {
  const goods = {
    id,
    recom
  }

  goodsApi.chgRecom(goods)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum)
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//是否上架
function chgStatus(id, status) {
  const goods = {
    id,
    status
  }

  goodsApi.chgStatus(goods)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum)
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//根据id删除商品
function deleteGoods(id) {
  goodsApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //查询第一页
          selectByPage(1);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}


/*----------------------详情开始----------------------*/
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const mode = ref('default');

const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: `${SERVER_ADDR.value}/goods/upload`,
      fieldName: 'pic',
      headers: {
        token: tokenStore.tokenStr
      },
      customInsert(resp, insertFn) {
        // res 即服务端的返回结果
        let url = `${SERVER_ADDR.value}/goods/pic/${resp.data}`;
        // url - 图片公国后台访问的地址
        console.log("插入的图片 URL:", url);
        insertFn(url)

      },
    }
  }
}
const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
/*----------------------详情结束----------------------*/

//获取所有的夫分类
function selectAllParent() {
  categoryApi.selectAllParent()
      .then(resp => {
        allParent.value = resp.data;
      })
}

//分页搜索
function selectByPage(pageNum) {
  goodsApi.selectByPage(condition.value, pageNum, 10)
      .then(resp => {
        pageInfo.value = resp.data;
      });
}
selectAllParent();
selectByPage(1);
</script>

<style scoped>

</style>