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
            <el-select
                v-model="condition.parentId"
                clearable placeholder="请选择夫分类"
                :value-on-clear="null"
                style="width: 200px;"
                @change="selectByPage(1)">
              <el-option label="没有夫分类" value="0" />
              <el-option v-for="(parent, item) in allParent" :key="item" :label="parent.name" :value="parent.id" />
            </el-select>
          </el-form-item>
        </el-form>
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="ID" />
          <el-table-column prop="name" label="名称" />
          <el-table-column label="图片">
            <template #default="scope">
              <el-avatar shape="square" :size="50" :src="SERVER_ADDR + '/category/pic/' + scope.row.pic"></el-avatar>
            </template>
          </el-table-column>
          <el-table-column prop="dscp" label="描述" />
          <el-table-column label="夫分类">
            <template #default="scope">
              <el-text class="mx-1" type="primary" v-if="scope.row.parent">{{scope.row.parent.name}}</el-text>
            </template>
          </el-table-column>
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
          <el-table-column label="是否上架">
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
          <el-table-column label="操作" width="150px">
            <template #default="scope">
              <el-button size="small" type="primary" @click="selectById(scope.row.id)">修改</el-button>
              <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消" width="200px" @confirm="deleteCategory(scope.row.id)">
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

  <!-- 添加分类的对话框开始 -->
  <el-dialog v-model="addDialogShow" title="添加分类" width="500">
    <el-form ref="insertForm" :model="categoryAdd" :rules="rules">
      <el-form-item label="名称:" label-width="18%" prop="name">
        <el-input v-model="categoryAdd.name" placeholder="请输入名称" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="描述:" label-width="18%" prop="dscp">
        <el-input v-model="categoryAdd.dscp" placeholder="请输入描述" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="夫分类" label-width="18%">
        <el-select v-model="categoryAdd.parentId" placeholder="请选择夫分类" clearable :empty-values="[0]" :value-on-clear="0" size="large" style="width: 300px">
          <el-option v-for="(category, index) in allParent" :key="index" :label="category.name"
                     :value="category.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否推荐:" label-width="18%" prop="sgender">
        <el-radio-group v-model="categoryAdd.recom" style="width: 300px">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架:" label-width="18%" prop="sgender">
        <el-radio-group v-model="categoryAdd.status" style="width: 300px">
          <el-radio label="下架" :value="0" size="large" />
          <el-radio label="上架" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片:" label-width="20%" prop="pic">
        <el-upload class="avatar-uploader" :action="SERVER_ADDR + '/category/upload'"
                   name="pic"
                   :headers="headers"
                   :show-file-list="false"
                   :on-success="picAddUploadSuccess"
                   :before-upload="beforeAvatarUpload" >
          <img v-if="categoryAdd.pic"  :src="SERVER_ADDR + '/category/pic/' + categoryAdd.pic" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
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
  <!-- 添加分类的对话框结束 -->

  <!-- 修改分类的对话框开始 -->
  <el-dialog v-model="updateDialogShow" title="修改分类" width="500">
    <el-form ref="updateForm" :model="categoryUpdate" :rules="rules">
      <el-form-item label="名称:" label-width="18%" prop="name">
        <el-input v-model="categoryUpdate.name" placeholder="请输入名称" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="描述:" label-width="18%" prop="dscp">
        <el-input v-model="categoryUpdate.dscp" placeholder="请输入描述" autocomplete="off" style="width: 300px" />
      </el-form-item>
      <el-form-item label="夫分类" label-width="18%">
        <el-select v-model="categoryUpdate.parentId" placeholder="请选择夫分类" clearable :empty-values="[0]" :value-on-clear="0" size="large" style="width: 300px">
          <el-option v-for="(category, index) in allParent" :key="index" :label="category.name"
                     :value="category.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否推荐:" label-width="18%" prop="sgender">
        <el-radio-group v-model="categoryUpdate.recom" style="width: 300px">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架:" label-width="18%" prop="sgender">
        <el-radio-group v-model="categoryUpdate.status" style="width: 300px">
          <el-radio label="下架" :value="0" size="large" />
          <el-radio label="上架" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片:" label-width="20%" prop="=pic">
        <el-upload class="avatar-uploader" :action="SERVER_ADDR + '/category/upload'"
                   name="pic"
                   :headers="headers"
                   :show-file-list="false"
                   :on-success="picUpdateUploadSuccess"
                   :before-upload="beforeAvatarUpload" >
          <img v-if="categoryUpdate.pic" :src="SERVER_ADDR + '/category/pic/' + categoryUpdate.pic" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
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
  <!-- 修改分类的对话框结束 -->


</template>

<script setup>
import categoryApi from "@/api/categoryApi.js";
import {computed, ref} from "vue";
import {ElMessage} from "element-plus";
import {useTokenStore} from "@/stores/token.js";
import goodsApi from "@/api/goodsApi.js";

const tokenStore = useTokenStore();
const headers = computed(() => {
  const token = tokenStore.tokenStr;
  return {
    token: token
  }
})


//服务器路径
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

//搜索的条件
const condition = ref({
  name: null,
  parentId: null,
  status: null
});
//夫分类
const allParent = ref([]);
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

// 验证规则
const rules = {
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' }
  ],
  dscp: [
    { required: true, message: '描述不能为空', trigger: 'blur' }
  ],
  pic: [
    { required: true, message: '图片不能为空', trigger: 'blur' }
  ],
};
const insertForm = ref()
const updateForm = ref()


//被添加的分类信息
const categoryAdd = ref({
  name: null,
  dscp: null,
  pic: null,
  parentId: 0,
  recom: 1,
  status: 1
});
//是否显示添加的对话框
const addDialogShow = ref(false);

//被修改的分类信息
const categoryUpdate = ref({
  id: null,
  name: null,
  dscp: null,
  pic: null,
  parentId: 0,
  recom: 1,
  status: 1
});
//是否显示修改的对话框
const updateDialogShow = ref(false);

//根据id查询被修改分类的信息
function selectById(id) {
  categoryApi.selectById(id)
      .then(resp => {
        categoryUpdate.value = resp.data;
        updateDialogShow.value = true;
      });
}
//添加分类
function insert() {
  insertForm.value.validate(valid => {
    if (valid) {
      categoryApi.insert(categoryAdd.value)
          .then(resp => {
            if (resp.code == 10000) {
              ElMessage.success(resp.msg);
              //隐藏对话框
              addDialogShow.value = false;
              //清空对话框
              categoryAdd.value = {
                name: null,
                dscp: null,
                pic: null,
                parentId: 0,
                recom: 1,
                status: 1
              };
              //查询第一页
              selectByPage(1);
            } else {
              ElMessage.error(resp.msg);
            }
          });
    }
  })
}

//删除分类
function deleteCategory(id) {
  categoryApi.delete(id)
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

//分页查询
function selectByPage(pageNum) {
  categoryApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
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
//成功上传之后的回调
function picAddUploadSuccess(resp) {
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    categoryAdd.value.pic = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}

//成功修改图片之后的回调
function picUpdateUploadSuccess(resp) {
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    categoryUpdate.value.pic = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}
//修改
function update() {
  updateForm.value.validate(valid => {
    if (valid) {
      categoryApi.update(categoryUpdate.value)
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
  })
}

//是否推荐
function chgRecom(id, recom) {
  const category = {
    id,
    recom,
  }

  categoryApi.chgRecom(category)
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
  const category = {
    id,
    status,
  }

  categoryApi.chgStatus(category)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum)
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//获取所有的夫分类
function selectAllParent() {
  categoryApi.selectAllParent()
      .then(resp => {
        allParent.value = resp.data;
      })
}
selectAllParent();
selectByPage(1);
</script>

<style scoped>
.avatar-uploader,
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border: 1px dotted #dcdfe6;
  border-radius: 5px;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>