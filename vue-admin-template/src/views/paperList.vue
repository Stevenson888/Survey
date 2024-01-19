<template>
  <el-main>
    <!-- 模糊查询 -->
    <el-form
      :model="parms"
      ref="searchForm"
      label-width="80px"
      :inline="true"
      size="small"
    >
      <el-form-item label="问卷标题">
        <el-input v-model="parms.title"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="resetBtn" icon="el-icon-close"
          >重置</el-button
        >
        <el-button type="primary" @click="addBtn" icon="el-icon-plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格列表 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
<!--      <el-table-column prop="paperImg" width="80" label="问卷图片" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 50px; height: 50px; border-radius: 50%"
            :src="scope.row.paperImg"
          ></el-image>
        </template>
      </el-table-column>-->
<!--      <el-table-column prop="id" label="问卷id"></el-table-column>-->
      <el-table-column prop="title" label="所属活动"></el-table-column>
      <el-table-column prop="paper.paperId" label="问卷编号"></el-table-column>
      <el-table-column prop="paper.title" label="问卷名称"></el-table-column>
<!--      <el-table-column prop="subTitle" label="问卷子标题"></el-table-column>-->
      <el-table-column prop="createTime" label="问卷创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="100px"></el-table-column>

<!--      <el-table-column prop="endTime" label="问卷结束时间"></el-table-column>-->
<!--      <el-table-column prop="remark" label="问卷描述"></el-table-column>-->
      <!--      <el-table-column prop="category" label="问卷种类"></el-table-column>-->
      <!--      <el-table-column prop="pictureUrl" label="图片url"></el-table-column>-->

<!--      <el-table-column prop="isFinishView" label="问卷状态isFinishView">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.isFinishView == '0'" type="success" size="normal">调查中</el-tag>-->
<!--          <el-tag v-if="scope.row.isFinishView == '1'" type="danger" size="normal">关闭</el-tag>-->
<!--&lt;!&ndash;          {{scope.row}}&ndash;&gt;-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column prop="activity.isActive" label="问卷状态isActive">-->
<!--&lt;!&ndash;        {{activity.isActive}}&ndash;&gt;-->
<!--          <template slot-scope="scope">-->
<!--              <div>-->
<!--&lt;!&ndash;                {{parseExectuionTime(scope.row.activity.isActive)}}&ndash;&gt;-->
<!--&lt;!&ndash;                {{parseExectuionTime(scope.row)}}&ndash;&gt;-->
<!--                {{scope && scope.row && scope.row.activity && scope.row.activity.isActive}}-->
<!--              </div>-->
<!--&lt;!&ndash;              <div v-if="scope.row.activity.isActive == '0'">&ndash;&gt;-->
<!--&lt;!&ndash;                        0&ndash;&gt;-->
<!--&lt;!&ndash;              </div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div v-if="scope.row.activity.isActive == '1'">&ndash;&gt;-->
<!--&lt;!&ndash;                        1&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-tag :key="scope.row.activity.isActive" v-if="scope.row.activity.isActive== '0'">{{scope.row.activity.isActive}}</el-tag>&ndash;&gt;-->
<!--          </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column prop="activity.isActive" label="问卷状态">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.activityList[0].isActive == '0'" type="success" size="normal">调查中</el-tag>-->
<!--          <el-tag v-if="scope.row.activityList[0].isActive == '1'" type="danger" size="normal">关闭</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column prop="activity.isActive" label="问卷状态"> </el-table-column>-->

<!--      <el-table-column prop="" label="问卷覆盖区域"></el-table-column>-->
      <el-table-column
        label="项目覆盖区域"
        align="left"
        width="120px">
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.areaNameList" :key="index+'abc'" >
            <div v-if="scope.row.areaNameList.length>1">{{index+1}}.{{item}}</div>
            <div v-else>{{item}}</div>
          </div>
        </template>
      </el-table-column>

<!--      <el-table-column prop="activity.forUserType" label="问卷发布对象"> </el-table-column>-->

<!--      TODO-->
<!--      <el-table-column prop="activity.forUserType" label="问卷发布对象">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.activityList[0].forUserType == '1'" type="primary" size="normal">消费者</el-tag>-->
<!--          <el-tag v-if="scope.row.activityList[0].forUserType == '2'" type="danger" size="normal">商户</el-tag>-->
<!--          <el-tag v-if="scope.row.activityList[0].forUserType == '4'" type="info" size="normal">访问员</el-tag>-->
<!--          <el-tag v-if="scope.row.activityList[0].forUserType == '8'" type="warning" size="normal">客户经理</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="" label="问卷关联物料种类"></el-table-column>-->
      <el-table-column align="center" width="180" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页
    @size-change 页容量改变时触发的事件
    @current-change 页数改变时触发的事件
    :current-page  当前第几页
    :page-sizes
    :page-size  没有查询几条
     :total 总条数
    -->
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page.sync="parms.pageNum"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="parms.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="parms.total"
      background
    >
    </el-pagination>
    <!-- 新增弹框 -->
    <sys-dialog
      :title="addDialog.title"
      :width="addDialog.width"
      :height="addDialog.height"
      :visible="addDialog.visible"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <template slot="content">
        <el-form
          :model="addModel"
          ref="addForm"
          :rules="rules"
          label-width="100px"
          :inline="false"
          size="small"
        >
          <el-form-item prop="paperTitle" label="问卷编号">
            <el-input v-model="addModel.paperId"  ></el-input>
          </el-form-item>
          <el-form-item prop="paperTitle" label="问卷标题">
            <el-input v-model="addModel.title"></el-input>
          </el-form-item>
          <el-form-item prop="subTitle" label="问卷子标题">
            <el-input v-model="addModel.subTitle"></el-input>
          </el-form-item>
          <el-form-item prop="remark" label="问卷描述">
            <el-input type="textarea" v-model="addModel.remark"></el-input>
          </el-form-item>
          <el-form-item prop="category" label="问卷种类">
            <el-input v-model="addModel.category"></el-input>
          </el-form-item>
          <el-form-item prop="pictureUrl" label="问卷图片">
            <el-input v-model="addModel.pictureUrl"></el-input>
          </el-form-item>
          <el-form-item prop="isFinishView" label="问卷状态">
            <el-radio-group v-model="addModel.isFinishView">
              <el-radio :label="0">启用</el-radio>
              <el-radio :label="1">停用</el-radio>
            </el-radio-group>
          </el-form-item>

<!--          <el-form-item prop="paperImg" label="问卷图片">
            &lt;!&ndash; el-upload
            action ：图片上传请求的地址
            :auto-upload 自动上传
             &ndash;&gt;
            <el-upload
              :action="onUpLoad()"
              :on-success="onSuccess"
              :file-list="fileList"
              list-type="picture-card"
              :auto-upload="true"
              :limit="1"
            >
              <i slot="default" class="el-icon-plus"></i>
              <div slot="file" slot-scope="{ file }">
                <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                <span class="el-upload-list__item-actions">
                  <span
                    class="el-upload-list__item-preview"
                    @click="handlePictureCardPreview(file)"
                  >
                    <i class="el-icon-zoom-in"></i>
                  </span>
                  <span
                    v-if="!disabled"
                    class="el-upload-list__item-delete"
                    @click="handleRemove(file)"
                  >
                    <i class="el-icon-delete"></i>
                  </span>
                </span>
              </div>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="" />
            </el-dialog>
          </el-form-item>
          -->

        </el-form>
      </template>
    </sys-dialog>
  </el-main>
</template>

<script>
import {getPaperApi, getPaperRestApi, getPaperListApi, addPaperApi, editPaperApi, deletePaperApi} from "@/api/paper";
import { saveQuestionApi, getQuestionListApi } from "@/api/question";

import SysDialog from "@/components/system/SysDialog.vue";
import { baseUrl, imageUrl } from "@/settings";

export default {
  //注册组件
  components: {
    SysDialog,
  },
  data() {
    return {
      fileList: [],
      dialogImageUrl: "",
      dialogVisible: false,
      disabled: false,
      //表单验证规则
      rules: {
        paperImg: [
          {
            trigger: "change",
            required: true,
            message: "请上传图片",
          },
        ],
        title: [
          {
            trigger: "change",
            required: true,
            message: "请填写问卷标题",
          },
        ],
        remark: [
          {
            trigger: "change",
            required: true,
            message: "请填写问卷描述",
          },
        ],
        isFinishView: [
          {
            trigger: "change",
            required: true,
            message: "请选择问卷状态",
          },
        ],
        paperId: [
          {
            trigger: "change",
            required: true,
            message: "请选择问卷序号",
          },
        ],
      },
      //新增或编辑表单绑定的数据域
      addModel: {
        id: "",
        editType: "", //0 新增 1 编辑
        paperId: "",
        title: "",
        subTitle: "",
        remark: "",
        category: "",
        pictureUrl: "",
        isFinishView: "",
        paperImg: "",
        // paperOrder: "",
      },
      //弹框属性
      addDialog: {
        title: "",
        width: 760,
        height: 410,
        visible: false,
      },
      //表格的高度
      tableHeight: 0,
      //表格数据
      tableList: [],
      //列表参数
      parms: {
        pageNum: 1,
        pageSize: 10,
        paperTitle: "",
        total: 0, //分页用的总条数
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    };
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  methods: {
    //图片上传成功的回调
    onSuccess(response, file, fileList) {
      console.log(response);
      console.log(file);
      console.log(fileList);
      this.addModel.paperImg = baseUrl + response;
      this.$message.success("图片上传成功!");
      console.log(this.addModel);
    },
    //图片删除
    handleRemove() {
      this.fileList = [];
      this.addModel.paperImg = "";
    },
    //图片上传请求方法
    onUpLoad() {
      return imageUrl;
    },
    //点击预览
    handlePictureCardPreview(file) {
      console.log(file);
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //新增按钮
    addBtn() {
      //清空表单
      this.$resetForm("addForm", this.addModel);
      //设置编辑类型  新增 还是 编辑
      this.addModel.editType = "0";
      // 清空图片
      this.fileList = [];
      //设置弹框属性
      this.addDialog.title = "新增问卷";
      this.addDialog.visible = true;
    },
    //删除按钮
    async deleteBtn(row) {
      console.log(row);
      //信息提示
      let confirm = await this.$myconfirm("确定删除该数据吗？");
      if (confirm) {
        let res = await deletePaperApi({ id: row.id });
        // console.log("999===id===", id);
        if (res && res.code == 200) {
          //信息提示
          this.$message.success(res.msg);
          //刷新表格
          this.getList();
        }
      }
    },
    //编辑按钮
    editBtn(row) {
      //清空表单
      this.$resetForm("addForm", this.addModel);
      //设置编辑类型  新增 还是 编辑
      this.addModel.editType = "1";
      //设置弹框属性
      this.addDialog.title = "编辑问卷";
      this.addDialog.visible = true;
      // 清空图片
      this.fileList = [];
      //表单数据赋值
      this.$objCoppy(row, this.addModel);
      //图片回显处理
      if (row.paperImg) {
        let obj = {};
        obj.url = row.paperImg;
        this.fileList.push(obj);
      }
    },
    //get one paper
    async getBtn(row) {
      let res = await getPaperRestApi({id:row.id});
    },
    //弹框确认
    onConfirm() {
      //表单验证
      this.$refs.addForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          if (this.addModel.editType == "0") {
            //新增
            res = await addPaperApi(this.addModel);
          } else {
            console.log("===this.addModel===", this.addModel);
            res = await editPaperApi(this.addModel);
          }
          if (res && res.code == 200) {
            //刷新列表
            this.getList();
            //信息提示
            this.$message.success(res.msg);
            this.addDialog.visible = false;
          }
        }
      });
    },
    //弹框关闭
    onClose() {
      this.addDialog.visible = false;
    },
    //重置按钮
    resetBtn() {
      this.parms.title = "";
      this.getList();
    },
    //搜索按钮
    searchBtn() {
      this.getList();
    },
    //页数改变时触发
    currentChange(val) {
      this.parms.pageNum = val;
      this.getList();
    },
    //页容量改变时触发
    sizeChange(val) {
      this.parms.pageSize = val;
      this.parms.pageNum = 1;
      this.getList();
    },
    //获取列表
    async getList() {
      let res = await getPaperListApi(this.parms);
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.tableList = res.data.records;
        //分页总条数
        this.parms.total = res.data.total;
      }
      console.log(res);
    },
  },
};
</script>

<style lang="scss" scoped></style>
