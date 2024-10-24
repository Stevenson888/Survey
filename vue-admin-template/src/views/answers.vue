<template>
  <el-main>

<!--&lt;!&ndash;    <div style="top:50px;  left: 50%;   height: 140px;  position: fixed; position:absolute; z-index:999">&ndash;&gt;-->
<!--    <span style=" position:fixed; top:70px; width:100%; z-index:999; ">-->

    <span style="  top:0px; width:100%; z-index:999; ">
              <span style="font-size: 1.3em; color:#575454; font-weight: bolder; margin-left: 0%; margin-bottom: 10%" > 问卷进度 </span>
     </span>

    <div >
<!--      <el-row class="padding-top-20" style="">-->
<!--        <el-col :span="10" style="text-align: left"><div >1</div></el-col>-->
<!--        <el-col :span="10" style="text-align: left"><div >2</div></el-col>-->
<!--        <el-col :span="10" style="text-align: left"><div >3</div></el-col>-->
<!--        <el-col :span="10" style="text-align: left"><div >4</div></el-col>-->
<!--      </el-row>-->

        <!--table 1-->
        <div style="margin-top:10px; text-align: center;">
          <!--r0-->
<!--          <el-row>-->
<!--            <el-col :span="24">-->
<!--              <div class="grid-content bg-purple-dark title">-->
<!--                门店基础信息-->
<!--              </div>-->
<!--            </el-col>-->
<!--          </el-row>-->


          <!--r1-->
          <el-row >
            <el-col :span="4"><div class="table_title">归属项目</div></el-col>
            <el-col :span="10"><div class="table_content" >{{projectName}}</div></el-col>
            <el-col :span="10"><div></div></el-col>
          </el-row>
          <el-row >
            <el-col :span="4"><div class="table_title">问卷编号</div></el-col>
            <el-col :span="10"><div class="table_content" >{{activityId}}</div></el-col>
            <el-col :span="10"><div></div></el-col>
          </el-row>
          <el-row >
            <el-col :span="4"><div class="table_title">问卷名称</div></el-col>
            <el-col :span="10"><div class="table_content" >{{activityTitle}}</div></el-col>
            <el-col :span="10"><div></div></el-col>
          </el-row>
          <el-row >
            <el-col :span="4"><div class="table_title">有效问卷完成数量</div></el-col>
            <el-col :span="10"><div class="table_content" >{{answerCount}}</div></el-col>
            <el-col :span="10"><div></div></el-col>
          </el-row>
          <el-row >
            <el-col :span="4"><div class="table_title">问卷结果统计</div></el-col>
            <el-col :span="10">
              <div class="table_content" style="padding-top:0px">
                <el-button type="primary" size="mini" @click="lookBtn">点击查看</el-button>
              </div>
            </el-col>
            <el-col :span="10"><div></div></el-col>
          </el-row>
      </div>
    </div>

    <span style=" position:relative; top:20px; width:100%; z-index:999; ">
              <span style="font-size: 1.3em; color:#575454; font-weight: bolder; margin-left: 0%; " > 答卷清单 </span>
<!--              <el-button type="danger" icon="el-icon-back" size="medium" @click="returnPapersPage" style=" float:right; margin-right: 5%; ">返回</el-button>-->
     </span>


<!--    <div style="color: #000000; font-size: large; font-weight: 600; margin-bottom:20px">-->
<!--      <span>answers页 - activityId:{{ this.activityId }}</span>-->
<!--    </div>-->

<!--    <div style="padding-top:50px; text-align: center">-->
<!--      <el-button type="primary" @click="returnPapersPage()">返回</el-button>-->
<!--    </div>-->

<!--    <div style="margin: 10px 0">-->
<!--      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>-->
<!--&lt;!&ndash;      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>&ndash;&gt;-->
<!--      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>-->
<!--      <el-button type="warning" @click="reset">重置</el-button>-->
<!--    </div>-->

<!--    <div style="margin: 10px 0">-->
<!--      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>-->
<!--      <el-popconfirm-->
<!--          class="ml-5"-->
<!--          confirm-button-text='确定'-->
<!--          cancel-button-text='我再想想'-->
<!--          icon="el-icon-info"-->
<!--          icon-color="red"-->
<!--          title="您确定批量删除这些数据吗？"-->
<!--          @confirm="delBatch"-->
<!--      >-->
<!--        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>-->
<!--      </el-popconfirm>-->
<!--      &lt;!&ndash; <el-upload action="http://local host:9090/answer/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> &ndash;&gt;-->
<!--    </div>-->



<!--    <el-form-->
<!--      :model="params"-->
<!--      ref="searchForm"-->
<!--      label-width="100px"-->
<!--      :inline="true"-->
<!--      size="small"-->
<!--    >-->
<!--&lt;!&ndash;      <el-form-item label="搜索问卷名称">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="params.name"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button icon="el-icon-search" @click="load">搜索</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--      <el-form-item style="margin-right:50px; float:right;" >-->
<!--        <el-button type="danger" icon="el-icon-back" @click="returnPapersPage">返回</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->


    <div  style="padding-top: 40px " >

    <el-form
      :model="params"
      ref="searchForm"
      label-width="100px"
      :inline="true"
      size="small"
    >
      <el-form-item label="搜索门店名称">
        <el-input v-model="params.name"></el-input>
      </el-form-item>
      <el-form-item label="搜索门店专卖证号" label-width="150px">
        <el-input v-model="params.licenseNo"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="load">搜索</el-button>
        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-bottom" @click="exportAnswerList" >导出</el-button>
        <el-button type="danger" icon="el-icon-back" size="small" @click="returnPapersPage" style=" float:right; margin-left: 400px; ">返回</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData"  border stripe :height="tableHeight"
              :header-cell-class-name="'headerBg'"
              :header-cell-style="{color: '#848484', fontSize: '14px', backgroundColor: '#f5f7fa'}"
              @selection-change="handleSelectionChange" >

<!--      <el-table-column type="selection" width="55"></el-table-column>-->
<!--      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>-->

<!--      <el-table-column align="center" label="答卷清单" style="font-size: 1.5em; color:#575454; font-weight: bolder; text-align: center; " >-->
<!--          <el-table-column prop="title" label="活动"></el-table-column>-->
          <el-table-column prop="answerId" label="答卷编号"></el-table-column>
          <el-table-column prop="licenseNo" label="答卷门店专卖证号" width="150"></el-table-column>
          <el-table-column prop="name" label="答卷门店名称"></el-table-column>
<!--          <el-table-column prop="nickname" label="答卷消费者昵称">-->
<!--              <template slot-scope="scope">-->
<!--                <div v-if="scope.row.userType=='1'"> {{ scope.row.nickname }} </div>-->
<!--                <div v-else>-</div>-->
<!--              </template>-->
<!--          </el-table-column>-->
          <el-table-column prop="nickname" label="答卷消费者昵称">
            <template slot-scope="scope">
              <div> {{ scope.row.nickname }} </div>
            </template>
          </el-table-column>
<!--          <el-table-column prop="mobile" label="答卷消费者手机号" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <div v-if="scope.row.userType=='1'">-->
<!--                {{scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/,"$1****$3"):""}}-->
<!--              </div>-->
<!--              <div v-else>-</div>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column prop="mobile" label="答卷消费者手机号" width="150">
            <template slot-scope="scope">
              <div>{{scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/,"$1****$3"):""}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="location" label="答卷地点"></el-table-column>
          <el-table-column prop="endTime" label="答卷完成时间"></el-table-column>
          <el-table-column prop="duration" label="答卷用时(秒)"></el-table-column>

    <!--      <el-table-column prop="activityId" label="活动id"></el-table-column>-->
    <!--      <el-table-column prop="paperId" label="问卷id"></el-table-column>-->
    <!--      <el-table-column prop="storeId" label="门店id"></el-table-column>-->
    <!--      <el-table-column prop="partnerUid" label="客户经理用户id"></el-table-column>-->
    <!--      <el-table-column prop="userId" label="答题人用户id"></el-table-column>-->
    <!--      <el-table-column prop="beginTime" label="答题开始时间"></el-table-column>-->

    <!--      <el-table-column prop="totalScore" label="答卷总分值"></el-table-column>-->
    <!--      <el-table-column prop="longitude" label="经度"></el-table-column>-->
    <!--      <el-table-column prop="latitude" label="纬度"></el-table-column>-->

    <!--      <el-table-column prop="isIncorrect" label="答卷不准确？1是(不准确) 0否"></el-table-column>-->
    <!--      <el-table-column prop="createTime" label="创建时间"></el-table-column>-->

          <el-table-column label="操作"  width="350" align="center">
            <template slot-scope="scope">
    <!--          <el-button type="primary" @click="checkAnswerDetail(scope.row)">查看答卷<i class="el-icon-edit"></i></el-button>-->
    <!--          <el-button icon="el-icon-edit" size="small" type="primary" @click="checkAnswerDetail(scope.row)">查看答卷</el-button>-->
              <el-button icon="el-icon-edit" size="small" type="primary" @click="checkAnswer(scope.row)">问卷答题结果</el-button>


              <!--          <el-button type="danger" size="small" icon="el-icon-back" @click="returnPapersPage">返回</el-button>-->
    <!--          <el-popconfirm-->
    <!--              class="ml-5"-->
    <!--              confirm-button-text='确定'-->
    <!--              cancel-button-text='我再想想'-->
    <!--              icon="el-icon-info"-->
    <!--              icon-color="red"-->
    <!--              title="您确定删除吗？"-->
    <!--              @confirm="del(scope.row.id)"-->
    <!--          >-->
    <!--            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>-->
    <!--          </el-popconfirm>-->
            </template>
          </el-table-column>
<!--      </el-table-column>-->
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.pageNum"
          :page-sizes="[20, 50, 100]"
          :page-size="params.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="params.total"
          background>
      </el-pagination>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="答卷id">
          <el-input v-model="form.answerId" autocomplete="off" disabled></el-input>
        </el-form-item>
<!--        <el-form-item label="活动id">-->
<!--          <el-input v-model="form.activityId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="问卷id">-->
<!--          <el-input v-model="form.paperId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店id">-->
<!--          <el-input v-model="form.storeId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="客户经理用户id">-->
<!--          <el-input v-model="form.partnerUid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="答题人用户id">-->
<!--          <el-input v-model="form.userId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="经纬度定位">
          <el-input v-model="form.location" autocomplete="off" disabled></el-input>
        </el-form-item>
<!--        <el-form-item label="答题开始时间">-->
<!--          <el-date-picker v-model="form.beginTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->
        <el-form-item label="答题结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" disabled placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="答题用时多少秒">
          <el-input v-model="form.duration" autocomplete="off" disabled></el-input>
        </el-form-item>

<!--        <el-form-item label="答卷总分值">-->
<!--          <el-input v-model="form.totalScore" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="经度">-->
<!--          <el-input v-model="form.longitude" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="纬度">-->
<!--          <el-input v-model="form.latitude" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="答卷不准确？1是(不准确) 0否">-->
<!--          <el-input v-model="form.isIncorrect" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="创建时间">-->
<!--          <el-date-picker v-model="form.createTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
<!--        <el-button type="primary" @click="save">确 定</el-button>-->
      </div>
    </el-dialog>

    <!-- '查看票数'弹框 -->
    <sys-dialog
      :title="totalDialog.title + this.space + title"
      :height="totalDialog.height"
      :width="totalDialog.width"
      :visible="totalDialog.visible"
      :isShow="false"
      @onClose="onClose"
    >
      <template slot="content">
        <div>
          <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">{{ title }}</div>
          <br/><br/><br/>
          <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">1.选择题</div>
          <br/><br/>

          <table style="width: 100%;" border="1">
            <thead >
            <th style="width:30%;">题目</th>
            <th style="width:30%;">选项</th>
            <th style="width:10%;">答案</th>
            </thead>
            <tbody>
            <template v-for="(question, questionIndex) in selectQuestionList">    <!--question为每道题-->
              <!--                <span> 题目{{questionIndex+1}} </span>-->

              <tr v-for="(option, optionIndex) in question.questionOptionList" :key="option.optionId">  <!--option为每个选项-->
                <td style="width:30%; text-align:left;" v-if="optionIndex == 0"  :rowspan="question.questionOptionList.length" >
                  {{ question.title }}
                </td>

                <td style="width:30%;text-align:left;">
                  <!--                    <span style=" " >{{optionIndex+1}}.</span>  {{ option.content }}-->
                  <span style=" " v-html="option.content">  </span>
                </td>

                <td v-if="option.isSelected == true ">✓</td>
                <td v-else></td>

<!--                <td  style="width:10%;text-align:center;font-weight: bolder;">  &lt;!&ndash;  {{ option.isSelected }}&ndash;&gt;-->
<!--                  {{option.isSelected==true}} ? 1 : ""-->
<!--                </td>-->

<!--                <td style="width:30%; height:20px;" >-->
<!--                  <el-row style="">-->
<!--                    <el-col :span="18"><div class="css" :style="{width:(option.optionProportion * 1).toFixed(2)+'%'}"></div></el-col>-->
<!--                    <el-col :span="6" ><div style="text-align:right; float: right; font-weight: bolder;">{{option.optionProportion}}%</div></el-col>-->
<!--                  </el-row>-->
<!--                </td>-->
              </tr>
            </template>
            </tbody>
          </table>

        <div v-show="this.blankQuestionList!=null && this.blankQuestionList.length>0">
          <br/><br/><br/>
          <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">2.填空题</div>
          <br/><br/>

          <table  style="width: 100%" border="1">
              <thead>
                <th>题目</th>
                <th>用户填写答案</th>
              </thead>
              <tbody>
              <template v-for="(blankQuestion, questionIndex) in blankQuestionList">                                        <!--question为每道题-->
                <tr>
                  <td style="width:70%; text-align: left"><b>{{blankQuestion.title}}</b></td>
                  <td style="width:30%;" v-html="blankQuestion.answerDetailContent"></td>
                </tr>
              </template>
              </tbody>
          </table>
        </div>
          <br/><br/><br/>
<!--          <div style="color: royalblue; text-align:center;" >填空题请点击'导出'查看</div>-->
          <br/>
        </div>
      </template>
    </sys-dialog>

      <!-- '查看票数'弹框 -->
      <sys-dialog
        :title="oneAnswerTotalDialog.title + this.space + activityTitle"
        :height="oneAnswerTotalDialog.height"
        :width="oneAnswerTotalDialog.width"
        :visible="oneAnswerTotalDialog.visible"
        :print="oneAnswerTotalDialog.print"
        :exportExcel="oneAnswerTotalDialog.exportExcel"
        :isShow="true"
        @onExportExcel="onExportExcelOneAnswer"
        @onClose="onCloseOneAnswer"
        @onConfirm="onConfirmOneAnswer"
      >
        <template slot="content">
          <div id="printTotal">
            <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">{{ activityTitle }}</div>
            <br/><br/><br/>
            <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">1.选择题</div>
            <br/><br/>

            <table style="width: 100%;" border="1">
              <thead >
              <th style="width:30%;">题目</th>
              <th style="width:30%;">选项</th>
              <th style="width:10%;">票数</th>
              <th style="width:30%;">比例</th>
              </thead>
              <tbody>
              <template v-for="(question, questionIndex) in selectQuestionList">    <!--question为每道题-->
                <!--                <span> 题目{{questionIndex+1}} </span>-->

                <tr v-for="(option, optionIndex) in question.questionOptionList" :key="option.optionId">  <!--option为每个选项-->
                  <td
                    style="width:30%; text-align:left;"
                    v-if="optionIndex == 0"
                    :rowspan="question.questionOptionList.length"
                  >{{ question.title }}</td>
                  <td style="width:30%;text-align:left;">
                    <!--                    <span style=" " >{{optionIndex+1}}.</span>  {{ option.content }}-->
                    <span style=" " > {{ option.content }} </span>
                  </td>
                  <td style="width:10%;text-align:center;font-weight: bolder;">{{ option.optionTotal }}</td>
                  <td style="width:30%; height:20px;" >
                    <el-row style="">
                      <el-col :span="18"><div class="css" :style="{width:(option.optionProportion * 1).toFixed(2)+'%'}"></div></el-col>
                      <el-col :span="6" ><div style="text-align:right; float: right; font-weight: bolder;">{{option.optionProportion}}%</div></el-col>
                    </el-row>
                  </td>
                </tr>
              </template>
              </tbody>
            </table>

            <br/><br/><br/>
            <div style="color: royalblue; text-align:center;" >填空题请点击'导出'查看</div>
            <br/>

          </div>
        </template>
      </sys-dialog>

    </div>
  </el-main>
</template>

<script>
import SysDialog from "@/components/system/SysDialog.vue";
import {getUserListApi} from "@/api/user";
import {
  exportAnswerListApi,
  getAnswerListByActivityIdApi,
  getAnswersProcessByActivityIdApi,
  getSelectQuestionOptionResultsByAnswerIdApi
} from "@/api/answer";
import {saveQuestionApi} from "@/api/question";
import {getActivityApi, getTotalListByActivityIdApi} from "@/api/activity";

export default {
  name: "Answers",
  //注册组件
  components: {
    SysDialog,
  },
  data() {
    return {
      activityId: "",

      tableData: [],
      tableHeight: 0,

      // total: 0,
      // pageNum: 1,
      // pageSize: 10,
      // name: "",

      form: {},
      dialogFormVisible: false,
      multipleSelection: [],

      selectQuestionList: [],
      blankQuestionList: [],

      totalDialog: {
        title: "答卷",
        height: 550,
        width: 1200,
        visible: false,
        print: true,
        exportExcel: true,
      },

      oneAnswerTotalDialog: {
        title: "票数统计",
        height: 550,
        width: 1200,
        visible: false,
        print: true,
        exportExcel: true,
      },

      params: {
        id: "",
        name: "",   // store name
        licenseNo: "",   // licenseNo
        pageNum: 1, //从第几页开始
        pageSize: 20, //每页查询的条数
        total: 0, //总条数
      },

      paperId: "",
      space: ": ",
      title: "",

      projectName: "",
      activityTitle: "",
      answerCount: "",

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

    }
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    if(this.$route.params.activityId){
      this.activityId = this.$route.params.activityId;
    }
    this.load()
  },
  methods: {
    load() {
      this.params.pageNum = 1;
      this.getList();
    },

    //获取表格列表
    async getList() {
      const rLoading = this.openLoading();
      let res = await getAnswerListByActivityIdApi(this.activityId, this.params);
      console.log(res);
      if (res && res.data && res.code == 200) {
        rLoading.close();
        console.log("666666 ===answers=== res.data", res.data)

        //给表格数据赋值
        this.tableData = res.data.records;
        //总条数
        this.params.total = res.data.total;
        // console.log("787878 ===answers===  this.params", this.params)

      }

      let res2 = await getAnswersProcessByActivityIdApi(this.activityId, this.params);
      console.log("7777777 ====== res2", res2);
      if (res2 && res2.data && res2.code == 200) {
        console.log("7777777 ===answers=== res2.data", res2.data)

        this.projectName = res2.data.project.name,
          this.activityId = res2.data.activityId,
          this.activityTitle = res2.data.title,
          this.answerCount = res2.data.answerCount
      }

    },

    // //查看票数
    // async lookBtn(row) {
    //   this.title = row.title;
    //
    //   // document.getElementById('precent').style.width = '80%'
    //
    //   console.log("===row===", row);
    //   console.log("===row.activityId===", row.activityId);
    //
    //
    //
    //   this.activityId = row.activityId
    //   //查询统计数据
    //   // let res = await getPaperTotalListApi({ paperId: row.paperId });
    //   let res = await getTotalListByActivityIdApi(row.activityId);
    //   if (res && res.code == 200) {
    //
    //     console.log("===res.data===", res.data);
    //     console.log("===res.data.selectQuestionList===", res.data.selectQuestionList);
    //     console.log("===res.data.blankQuestionList===", res.data.blankQuestionList);
    //
    //     this.selectQuestionList = res.data.selectQuestionList;
    //     this.blankQuestionList = res.data.blankQuestionList;
    //     // console.log("===res.data.questionList===", res.data.questionList);
    //     // console.log("===this.questionList===", this.questionList);
    //
    //     console.log("===res.data.title===", res.data.title);
    //
    //   }


    async checkAnswer(row) {
      const rLoading = this.openLoading();
      this.answerId = row.answerId

      console.log("===row===", row);

      // console.log("===row.answerId===", row.answerId);
      //查询统计数据

      let res = await getSelectQuestionOptionResultsByAnswerIdApi( {id:row.answerId} );
      // console.log("===res.data===", res.data);
      if (res && res.data && res.code == 200) {

        rLoading.close();

        // console.log("===res.data.title===", res.data.title);
        // this.title = res.data.title;
        this.title = this.activityTitle;

        // console.log("===res.data.selectQuestionList===", res.data.selectQuestionList);
        // console.log("===res.data.blankQuestionList===", res.data.blankQuestionList);
        this.selectQuestionList = res.data.selectQuestionList;
        this.blankQuestionList = res.data.blankQuestionList;
      }
      // console.log("===this.selectQuestionList===", this.selectQuestionList);
      // console.log("===this.blankQuestionList===", this.blankQuestionList);
      // console.log("=== 888 ===", this.blankQuestionList==null || this.blankQuestionList.length==0 );
      // console.log("=== 888 ===",  this.blankQuestionList!=null && this.blankQuestionList.length>0 );

      this.totalDialog.visible = true;
    },

    checkAnswerDetail(row) {
      // this.$message.error("checkAnswerDetail")
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },

    //查看票数
    async lookBtn() {
      const rLoading2 = this.openLoading();
      // this.title = row.title;
      // document.getElementById('precent').style.width = '80%'

      // console.log("===row===", row);
      // console.log("===row.activityId===", row.activityId);

      // this.activityId = row.activityId
      //查询统计数据
      // let res = await getPaperTotalListApi({ paperId: row.paperId });

      let res = await getTotalListByActivityIdApi(this.activityId);
      if (res && res.data && res.code == 200) {

        // console.log("===res.data===", res.data);
        // console.log("===res.data.selectQuestionList===", res.data.selectQuestionList);
        // console.log("===res.data.blankQuestionList===", res.data.blankQuestionList);

        this.selectQuestionList = res.data.selectQuestionList;

        this.oneAnswerTotalDialog.visible = true;
        rLoading2.close();

      }
      // this.totalDialog.visible = true;
      // rLoading.close();
    },

    returnPapersPage() {
      this.$router.push({ path:  "/papers" });
    },

    // save() {
    //   this.request.post("/answer", this.form).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("保存成功")
    //       this.dialogFormVisible = false
    //       this.load()
    //     } else {
    //       this.$message.error("保存失败")
    //     }
    //   })
    // },
    // handleAdd() {
    //   this.dialogFormVisible = true
    //   this.form = {}
    //   this.$nextTick(() => {
    //     if(this.$refs.img) {
    //        this.$refs.img.clearFiles();
    //      }
    //      if(this.$refs.file) {
    //       this.$refs.file.clearFiles();
    //      }
    //   })
    // },
    // handleEdit(row) {
    //   this.form = JSON.parse(JSON.stringify(row))
    //   this.dialogFormVisible = true
    //    this.$nextTick(() => {
    //      if(this.$refs.img) {
    //        this.$refs.img.clearFiles();
    //      }
    //      if(this.$refs.file) {
    //       this.$refs.file.clearFiles();
    //      }
    //    })
    // },
    // del(id) {
    //   this.request.delete("/answer/" + id).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("删除成功")
    //       this.load()
    //     } else {
    //       this.$message.error("删除失败")
    //     }
    //   })
    // },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    // delBatch() {
    //   if (!this.multipleSelection.length) {
    //     this.$message.error("请选择需要删除的数据")
    //     return
    //   }
    //   let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
    //   this.request.post("/answer/del/batch", ids).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("批量删除成功")
    //       this.load()
    //     } else {
    //       this.$message.error("批量删除失败")
    //     }
    //   })
    // },
    reset() {
      this.params.name = ""
      this.params.licenseNo = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.params.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.params.pageNum = pageNum
      this.load()
    },
    // handleFileUploadSuccess(res) {
    //   this.form.file = res
    // },
    // handleImgUploadSuccess(res) {
    //   this.form.img = res
    // },
    // download(url) {
    //   window.open(url)
    // },
    // exp() {
    //   window.open("http://local host:9090/answer/export")
    // },
    // handleExcelImportSuccess() {
    //   this.$message.success("导入成功")
    //   this.load()
    // }

    //导出
    // onExportExcel() {
    //   window.open("http://local host:8089/api/activity/export/getTotalListByActivityId/"+this.activityId)
    // },
    //弹框确认
    onConfirm() {
      this.totalDialog.visible = false;
    },
    //弹框的关闭
    onClose() {
      this.totalDialog.visible = false;
    },

    //导出
    onExportExcelOneAnswer() {
      // console.log("===000000===onExportExcel===");
      // console.log("===000000===this.activityId===", this.activityId);

      window.open("http://localhost:8089/api/activity/export/getTotalListByActivityId/"+this.activityId)
      // window.open("http://43.140.195.12:8089/api/activity/export/getTotalListByActivityId/"+this.activityId)
      // window.open("https://adminapi.xiangwenli.com/api/activity/export/getTotalListByActivityId/"+this.activityId)

    },

    //导出
    exportAnswerList() {
      // console.log("===000000===onExportExcel===");
      // console.log("===000000===this.activityId===", this.activityId);
      // console.log("===000000===this.params===", this.params);
      // console.log("===000000===this.params.name===", this.params.name);


      // exportAnswerListApi(this.activityId, this.params);

      window.open("http://localhost:8089/api/answer/exportAnswerList/" + this.activityId + "?name=" + this.params.name + "&licenseNo=" + this.params.licenseNo)
      // window.open("http://43.140.195.12:8089/api/answer/exportAnswerList/" + this.activityId + "?name=" + this.params.name + "&licenseNo=" + this.params.licenseNo)
      // window.open("https://adminapi.xiangwenli.com/api/answer/exportAnswerList/" + this.activityId + "?name=" + this.params.name + "&licenseNo=" + this.params.licenseNo)


    },


    //弹框确认
    onConfirmOneAnswer() {
      this.oneAnswerTotalDialog.visible = false;
    },
    //弹框的关闭
    onCloseOneAnswer() {
      this.oneAnswerTotalDialog.visible = false;
    },

  }
}
</script>

<style>
.user-input-content {
  color: blue;
  text-decoration: underline;
  font-weight: bold;
}
</style>

<style lang="scss" scoped>

.user-input-content {
  color: blue;
  text-underline: #0E79C1;
  font-weight: bold;
}

.container {
  overflow-x: inherit;
  overflow-y: auto;
  height: 550px;
}
.chartitem {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.echarts {
  margin-bottom: 10px;
  width: 800px;
  height: 400px;
  background-color: #eeeeee;
  border: 1px solid #e5e5e5;
  padding: 20px;
}
table {
  // width: 300px;
  text-align: center;
  border-collapse: collapse; /* 为table设置这个属性 */
}
.land-scape-box-value {
  height: 66px;
  width: 100%;
  border-left: 1px solid #ccc;

  .land-scape-box {
    background: #4786F5;
    height: 20px;
    border-radius: 10px;

  }

  .unit-value {
    margin-left: 4px
  }
}
#loading-status {
  width: 300px;
  border: 1px #669CB8 solid;
  -webkit-box-shadow: 0px 2px 2px #D0D4D6;
  height: 15px;
  -webkit-border-radius: 10px;
  background: -webkit-gradient(linear, 0 0, 0 100%, from(#E1E9EE), to(white));
  padding: 1px;
}
//#process {
//  background: -webkit-gradient(linear, 0 0, 0 100%, from(#7BC3FF), color-stop(0.5,#42A9FF), to(#7BC3FF));
//  width: 0%;
//  height: 100%;
//  -webkit-border-radius: 10px;
//}
#process {
  background: -webkit-gradient(linear, 0 0, 0 100%, from(#7BC3FF), color-stop(0.5,#42A9FF), to(#7BC3FF));
  width: 0%;
  height: 100%;
  -webkit-border-radius: 10px;
  -webkit-transition: width 1s ease-in-out;
}
.content {
  padding: 43px 0 0 0px;

  .chart-rising-list {
    display: flex;
    align-items: center;
    margin-bottom: 12px;

    .chart-rising-title {
      width: 62px;
      text-align: right;
    }

    .chart-rising-title, .chart-rising-text {
      font-size: 8px;
      font-weight: 400;
      color: rgba(255, 255, 255, 0.7);
      margin: 0 5px;
    }

    .chart-rising-external {
      display: flex;
      align-items: center;
      width: 179px;
      height: 8px;
      border: 1px solid;
      border-image: linear-gradient(270deg, rgba(14, 132, 222, 1), rgba(6, 77, 186, 1)) 1 1;

      .chart-rising-inside {
        height: 8px;
        background: linear-gradient(270deg, #0E79C1 0%, #064DBA 100%);
      }
    }
  }
}


.el-dialog__wrapper {
  ::v-deep .el-dialog {
    border-top-left-radius: 7px;
    border-top-right-radius: 7px;
    .el-dialog__header {
      border-top-left-radius: 7px;
      border-top-right-radius: 7px;
      background-color: #1890ff;
      .el-dialog__title {
        color: #fff;
        font-size: 16px;
        font-weight: 600;
      }
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__body {
      padding: 10px;
    }
    .el-dialog__footer {
      border-top: 1px solid #e8eaec;
      padding: 10px;
    }
  }
}

* {box-sizing: border-box}


.headerBg {
  background: #eee!important;
}


.fixed {
  position: fixed;
  top: 10px;
  left: 10px;
  background-color: #fff;
  padding: 10px;
  border: 1px solid #ccc;
}

.table_title{
  border : 1px solid #5d5a5a;
  height:30px;
  padding-top: 10px;
  text-align: center;
  font-size: small;
  font-weight: bold;
}
.table_content {
  border: 1px solid #5d5a5a;
  height:30px;
  padding-top: 10px;
  //padding-left: 10px;
  text-align: center;
  font-size: small;
  font-weight: bold;
}

//.el-row{
//  border-bottom : 1px solid #5d5a5a;
//  border-top : 1px solid #5d5a5a;
//  height:50px;
//  text-align: center;
//}
//.el-col {
//  border-left: 1px solid #5d5a5a;
//  border-right: 1px solid #5d5a5a;
//  height:50px;
//}
.header {
  background-color: lightgrey;
  text-align: center;
  height:50px;
  font-weight: bolder;
}
.body {
  font-weight: lighter;
  height:50px;
  text-align: center;
}
.table {
  font-weight: lighter;
  font-size: 1em;
}
.title {
  text-align: center;
  font-size: 1.2em;
  height:50px;
}

.css {
  width: 60%;
  height:30px;
  background-color: #2196F3;
}

</style>
