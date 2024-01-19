<template>
  <el-main>

    <!-- 1.DataTable -->
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
      <el-table-column prop="id" label="id"></el-table-column>
      <el-table-column prop="title" label="问卷标题"></el-table-column>
      <el-table-column prop="subTitle" label="问卷子标题"></el-table-column>
      <el-table-column prop="category" label="问卷种类"></el-table-column>
      <el-table-column prop="remark" label="问卷评论"></el-table-column>
      <!--      <el-table-column prop="isRequire" label="状态">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.isRequire == '0'" type="danger" size="normal"
                  >关闭</el-tag
                >
                <el-tag v-if="scope.row.isRequire == '1'" type="success" size="normal"
                  >调查中</el-tag
                >
              </template>
            </el-table-column>-->
      <el-table-column align="center" width="180" label="操作">
        <template slot-scope="scope">
          <el-button
            type="success"
            icon="el-icon-setting"
            size="small"
            @click="addQuestion(scope.row)"
            >设计试题</el-button
          >
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

    <!-- 2.新增弹框 Start -->
    <sys-dialog
      :title="addDialog.title"
      :width="addDialog.width"
      :height="addDialog.height"
      :visible="addDialog.visible"
      :disabled="addDialog.disabled"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <template slot="content">
        <el-container style="height: 100%">
          <!-- 2.1左侧分类 button-->
          <el-aside class="leftcontainer" width="200px">
            <div class="btns">
              <el-button @click="addQuestionData('1')" class="btnitem" size="default" >单选题</el-button>
              <el-button @click="addQuestionData('2')" class="btnitem" size="default" >多选题</el-button>
              <el-button @click="addQuestionData('3')" class="btnitem" size="default" >单文本</el-button>
              <el-button @click="addQuestionData('4')" class="btnitem" size="default" >多文本</el-button>
            </div>
          </el-aside>
          <!-- 2.2右侧试题 -->
          <el-main style="padding-top: 1px">
            <el-form
              :model="customerFormModel"
              ref="customerForm"
              label-width="50px"
              :inline="false"
              size="small"
            >
              <div :key="index" v-for="index in customerFormModel.addModel.length">
                <!-- 2.2：情况：1单选/2多选 -->
                <div
                  v-if="customerFormModel.addModel[index - 1].type == '1' || customerFormModel.addModel[index - 1].type == '2' "
                  class="papetitem"
                >
                  <!-- <el-row>：代表一行 ，里面的子元素会横向排列
                     <el-col ：代表的是列
                     :span 代表所占的分数，总分数是 24
                      -->
                  <!-- row1 单选/多选:-->
                  <el-row>
                    <!-- row1 col1 题干内容-->
                    <el-col :span="17" :offset="0">
                      <el-form-item :label="index + '.'" label-width="35px" size="small">
                        <el-input
                          v-model="customerFormModel.addModel[index - 1].title"
                          size="small"

                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <!-- row1 col2 按钮'新增选项'-->
                    <el-col :span="7" :offset="0">
                      <el-button
                        style="margin-left: 15px"
                        size="small"
                        icon="el-icon-plus"
                        @click="addChoice(index)"

                        >新增选项</el-button
                      >
                      <!-- row1 col3 按钮'删除试题'-->
                      <el-button
                        style="margin-left: 15px"
                        type="danger"
                        size="small"
                        icon="el-icon-delete"
                        @click="deleteQuestion(index)"

                        >删除试题</el-button
                      >
                    </el-col>
                  </el-row>

                  <!-- row2 单选/多选:选项 -->
                  <el-form-item
                    :key="num"
                    v-for="num in customerFormModel.addModel[index - 1].questionOptionList.length"
                    label=""
                    size="small"
                  >
                    <!-- row2 选项-->
                    <el-row>
                      <!-- row2 col1 选项 内容 -->
                      <el-col :span="10" :offset="0">
                        <el-form-item :label="'选项' + num" size="small">
                          <el-input
                            v-model="customerFormModel.addModel[index - 1].questionOptionList[num - 1].content"
                            size="small"

                          ></el-input>
                        </el-form-item>
                      </el-col>
                      <!-- row2 col1 选项 序号 -->
                      <el-col :span="10" :offset="0">
                        <el-form-item label="序号" size="small">
                          <el-input
                            v-model="customerFormModel.addModel[index - 1].questionOptionList[num - 1].sort"
                            size="small"

                          ></el-input>
                        </el-form-item>
                      </el-col>
                      <!-- row2 col3 选项 按钮'删除选项' -->
                      <el-col :span="2" :offset="0">
                        <el-button
                          style="color: #ff7670; margin-left: 15px"
                          size="small"
                          circle
                          icon="el-icon-close"
                          @click="deleteChoice(index, num)"

                        ></el-button>
                      </el-col>
                    </el-row>
                  </el-form-item>
                </div>

                <!-- 2.2：情况3-单文本 -->
                <div
                  v-if="customerFormModel.addModel[index - 1].type == '3'"
                  class="papetitem"
                >
                  <el-row>
                    <!-- 单文本：col1 题干 -->
                    <el-col :span="17" :offset="0">
                      <el-form-item :label="index + '.'" label-width="35px" size="small">
                        <el-input
                          v-model="customerFormModel.addModel[index - 1].title"
                          size="small"

                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <!-- 单文本：col2 按钮'删除试题' -->
                    <el-col class="delBtn" :span="7" :offset="0">
                      <el-button
                        @click="deleteQuestion(index)"
                        style="margin-left: 15px"
                        type="danger"
                        icon="el-icon-delete"
                        size="small"

                        >删除试题</el-button
                      >
                    </el-col>
                  </el-row>
                </div>

                <!-- 2.2：情况4-多文本 -->
                <div
                  v-if="customerFormModel.addModel[index - 1].type == '4'"
                  class="papetitem"
                >
                  <el-row>
                    <!-- 多文本：col1 题干 -->
                    <el-col :span="17" :offset="0">
                      <el-form-item :label="index + '.'" label-width="35px" size="small">
                        <el-input
                          v-model="customerFormModel.addModel[index - 1].title"
                          type="textarea"
                          size="small"

                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <!-- 多文本：col2 按钮'删除试题' -->
                    <el-col class="delBtn" :span="7" :offset="0">
                      <el-button
                        @click="deleteQuestion(index)"
                        style="margin-left: 15px"
                        type="danger"
                        icon="el-icon-delete"
                        size="small"

                        >删除试题</el-button
                      >
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-form>
          </el-main>
        </el-container>
      </template>
    </sys-dialog>
    <!-- 新增弹框 END -->

  </el-main>
</template>

<script>
import { getPaperListApi } from "@/api/paper";
import SysDialog from "@/components/system/SysDialog.vue";
import { saveQuestionApi, getQuestionListApi } from "@/api/question";
import JSONbig from 'json-bigint'

export default {
  //注册组件
  components: {
    SysDialog,
  },
  data() {
    return {
      //问卷id
      paperId: "",
      //试题绑定的数据域
      customerFormModel: {
        addModel: [],
      },
      //弹框属性
      addDialog: {
        title: "",
        width: 1080,
        height: 600,
        visible: false,
        disabled: this.isReadOnly,
        // disabled: true,
      },
      //表格的高度
      tableHeight: 0,
      //表格数据
      tableList: [],
      //列表参数
      parms: {
        pageNum: 1,
        pageSize: 10,
        title: "",
        total: 0, //分页用的总条数
      },
      isReadOnly:false,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
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
    // 删除选项
    deleteChoice(index, num) {
      this.customerFormModel.addModel[index - 1].questionOptionList.splice(num - 1, 1);
    },
    //新增选择题的选项
    addChoice(index) {
      console.log(index);
      this.customerFormModel.addModel[index - 1].questionOptionList.push({
        content: "",
        sort: "",
      });
    },
    //删除试题
    deleteQuestion(index) {
      this.customerFormModel.addModel.splice(index - 1, 1);
    },
    //弹框确认事件
    async onConfirm() {
      if (this.customerFormModel.addModel.length < 1) {
        this.isReadOnly = false;
        this.$message.warning("请设计试题!");
        return;
      }
      let res = await saveQuestionApi(this.paperId, this.customerFormModel.addModel);
      if (res && res.code == 200) {
        this.addDialog.visible = false;
        this.$message.success(res.msg);
      }
    },
    //弹框关闭事件
    onClose() {
      this.addDialog.visible = false;
    },
    //设计试题按钮 - 打开问卷所有题目
    async addQuestion(row) {
      console.log("===row===", row);
      //清空数据
      this.customerFormModel.addModel = [];
      //设置问卷id
      this.paperId = row.paperId;
      //先查询该问卷是否存在试题，如果存在，回显；
      //不存在，初始表单数据，默认展示一个单选题
      let res = await getQuestionListApi({ paperId: this.paperId });

      if (res && res.code == 200) {

        this.$nextTick(() => {
          if (res.data && res.data.length > 0) {
            this.isReadOnly = true;
          } else{
            this.isReadOnly = false;
          }
          this.addDialog.disabled = this.isReadOnly;
        });

        console.log("this.addDialog.disabled", this.addDialog.disabled);
        // this.isReadOnly = res.data.length!=0 ? true : false

        console.log("试题");
        console.log("res.data", res.data);

        if (res.data && res.data.length > 0) {
          //设置返回的试题数据
          this.customerFormModel.addModel = res.data;
        } else {
          this.addQuestionData("1");
        }
      } else {
        this.addQuestionData("1");
      }
      //设置弹框属性
      this.addDialog.title = "为【" + row.title + "】设计试题";
      this.addDialog.visible = true;

    },

    //为问卷动态添加一道试题 - 根据数据库字段动态添加: 1单选/2多选/3单文本/4多文本
    addQuestionData(type) {
      //试题的数据格式，根据数据库字段所得
      let objModel = {
        paperId: "",
        questionId: "",
        questionIdStr: "",
        title: "",
        type: "",
        isRequire: "",
        probability: "",
        level: "",
        label: "",
        waitDays: "",
        // questionOrder: "",
        //试题的选项
        questionOptionList: [],
      };
      //如果是单选题或者多选题，需要设置选项
      if (type == "1" || type == "2") {
        let obj = {
          content: "",
          sort: "",
        };
        objModel.questionOptionList.push(obj);
      }
      //设置问卷id
      objModel.paperId = this.paperId;
      //设置试题的类型
      objModel.type = type;
      //设置到表单绑定的数据域
      this.customerFormModel.addModel.push(objModel);
    },
    //重置按钮
    resetBtn() {
      this.parms.title = "";    //paper title
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

<style lang="scss" scoped>
.leftcontainer {
  border-right: 1px solid #e8eaec;
}
.btns {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-right: 10px;
}
.btnitem {
  width: 100%;
  margin-left: 0px;
  margin-bottom: 10px;
}
.delBtn {
  display: flex;
  justify-content: flex-end;
  padding-right: 15px;
}
.papetitem {
  border-bottom: 1px solid #e8eaec;
  padding-top: 20px;
}
</style>
