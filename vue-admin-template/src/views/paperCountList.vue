<template>
  <el-main>
    <!-- 模糊查询 -->
    <el-form :model="parms" ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item label="问卷标题">
        <el-input v-model="parms.title"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="resetBtn" icon="el-icon-close">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-right:50px; float:right;" >
        <el-button type="danger" icon="el-icon-back" size="medium" @click="returnPapersPage">返回</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格列表 -->
    <el-table :data="tableList" :height="tableHeight" border stripe >
<!--      <el-table-column prop="paperImg" width="80" label="问卷图片" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 50px; height: 50px; border-radius: 50%"
            :src="scope.row.paperImg"
          ></el-image>
        </template>
      </el-table-column>-->
      <el-table-column prop="id" label="序号" width="100px"></el-table-column>

<!--      <el-table-column prop="activityId" label="activityId(FT)" width="120px"></el-table-column>-->
<!--      <el-table-column prop="forUserType" label="问卷人群(FT)" width="100px"></el-table-column>-->
      <el-table-column prop="paper.title" label="问卷标题" width="250px"></el-table-column>
      <el-table-column prop="title" label="所属活动" width="250px"></el-table-column>
      <el-table-column prop="project.name" label="所属项目" width="250px"></el-table-column>
<!--      <el-table-column label="问卷标题" align="left" width="400px">-->
<!--        <template slot-scope="scope">-->
<!--          <div v-for="(item,index) in scope.row.activityList" :key="index+'abcd'" >-->
<!--            <div v-if="scope.row.activityList.length>1">{{index+1}}.{{item.title}}</div>-->
<!--            <div v-else>{{item.title}}</div>-->
<!--          </div>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="答卷人群" align="left" width="100px">
        <template slot-scope="scope">
<!--          <div v-for="(item,index) in scope.row.activityList" :key="index+'abcd'" >-->
<!--            <div>{{item.forUserType}}</div>-->
            <el-tag v-if="scope.row.forUserType == '1'" type="primary" size="normal">消费者</el-tag>
            <el-tag v-if="scope.row.forUserType == '2'" type="success" size="normal">零售户</el-tag>
            <el-tag v-if="scope.row.forUserType == '8'" type="danger" size="normal">客户经理</el-tag>
<!--          </div>-->
        </template>
      </el-table-column>

<!--      <el-table-column prop="activity.info" label="问卷副标题"></el-table-column>-->
      <el-table-column prop="createTime" label="创建时间" width="180px"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180px"></el-table-column>
<!--      <el-table-column prop="activity.info" label="问卷副标题"></el-table-column>-->

<!--      TODO-->
<!--      <el-table-column prop="activity.isActive" label="状态">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.activity.isActive == '0'" type="danger" size="normal">关闭</el-tag>-->
<!--          <el-tag v-if="scope.row.activity.isActive == '1'" type="success" size="normal">调查中</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="small" @click="lookBtn(scope.row)">查看票数</el-button>
<!--          <el-button type="success"  icon="el-icon-edit" size="small" @click="lookAsines(scope.row)">分析图</el-button>-->
<!--          <el-button size="small" type="danger" icon="el-icon-back" @click="returnPapersPage">返回</el-button>-->
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
    ></el-pagination>

    <!-- '查看票数'弹框 -->
    <sys-dialog
      :title="totalDialog.title + this.space + title"
      :height="totalDialog.height"
      :width="totalDialog.width"
      :visible="totalDialog.visible"
      :print="totalDialog.print"
      :exportExcel="totalDialog.exportExcel"
      :isShow="true"
      @onExportExcel="onExportExcel"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <template slot="content">
        <div id="printTotal">
          <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">{{ title }}</div>
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

<!-- 填空题 START-->
<!--          <div v-show="this.blankQuestionList!=null && this.blankQuestionList.length>0">-->
<!--            <br/><br/><br/>-->
<!--            <div  style="text-align: center; font-size: 21px;  font-weight: 600;   padding: 0px 0px;">2.填空题</div>-->
<!--            <br/><br/>-->

<!--            <table style="width: 100%" border="1">-->
<!--  &lt;!&ndash;            <thead>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>title</th>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>counter1</th>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>counter2</th>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>counter3</th>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>counter4</th>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>counter5</th>&ndash;&gt;-->
<!--  &lt;!&ndash;              <th>counterSum</th>&ndash;&gt;-->
<!--  &lt;!&ndash;            </thead>&ndash;&gt;-->
<!--              <tbody>-->
<!--                <template v-for="(question, questionIndex) in blankQuestionList">                                        &lt;!&ndash;question为每道题&ndash;&gt;-->

<!--                  <tr>-->
<!--                    <td  style="width:30%;"></td>-->
<!--                    <td style="width:12%;"><b>{{question.minValue}}-{{question.num1}}</b></td>-->
<!--                    <td style="width:12%;"><b>{{question.num1}}-{{question.num2}}</b></td>-->
<!--                    <td style="width:12%;"><b>{{question.num2}}-{{question.num3}}</b></td>-->
<!--                    <td style="width:12%;"><b>{{question.num3}}-{{question.num4}}</b></td>-->
<!--                    <td style="width:12%;"><b>{{question.num4}}-{{question.maxValue}}</b></td>-->
<!--                    <td style="width:10%;"></td>-->
<!--                  </tr>-->

<!--                  <tr>-->
<!--                    <td style="width:30%;">{{question.title}}</td>-->
<!--                    <td style="width:12%;">{{question.c1prop}}%</td>-->
<!--                    <td style="width:12%;">{{question.c2prop}}%</td>-->
<!--                    <td style="width:12%;">{{question.c3prop}}%</td>-->
<!--                    <td style="width:12%;">{{question.c4prop}}%</td>-->
<!--                    <td style="width:12%;">{{question.c5prop}}%</td>-->
<!--                    <td style="width:10%;">{{question.counterSum}}</td>-->
<!--                  </tr>-->

<!--                </template>-->
<!--              </tbody>-->
<!--            </table>-->
<!--          </div>-->
<!-- 填空题 END -->


          <br/><br/><br/>
          <div style="color: royalblue; text-align:center;" >填空题请点击'导出'查看</div>
          <br/>

        </div>
      </template>
    </sys-dialog>

    <!-- 分析图 --> <!-- fullscreen -->
    <el-dialog
      @opened="opens"
      :title="echartsDialog.title"
      :visible.sync="echartsDialog.visible"
      width="1200px"
      top="0vh"
      :before-close="sysOnClose"
    >
      <!-- <span> <div id="myChart" :style="{ width: '300px', height: '300px' }"></div></span> -->
      <div class="container">
        <div class="chartitem">
          <div v-for="(item,index) in chartList" class="chart-item-area">
            <div class="echarts" :id="item.id"></div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer" >
        <el-button @click="sysOnClose">取 消</el-button>
        <el-button type="primary" @click="sysOnConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
import {getPaperListApi, getPaperTotalListApi, getPaperSysListApi} from "@/api/paper";
import SysDialog from "@/components/system/SysDialog.vue";
import {getActivityPageApi, getAllActivityListApi, getTotalListByActivityIdApi} from "@/api/activity";
import {getStoreByIdApi} from "@/api/storeshop";
export default {
  //注册组件
  components: {
    SysDialog,
  },
  // 在Vue实例中定义计算属性
  computed: {
  },
  data() {
    return {
      sysId: '',
      activityId: '',
      title: "",
      //统计表格的数据
      questionList: [],
      selectQuestionList: [],
      blankQuestionList: [],
      totalDialog: {
        title: "票数统计",
        height: 550,
        width: 1200,
        visible: false,
        print: true,
        exportExcel: true,
      },
      echartsDialog: {
        title: "分析图",
        width: 1100,
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
        title: "",
        total: 0, //分页用的总条数
      },
      msg: 'Welcome to Your Vue.js App',
      chartList: [],
      space: ": "
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  methods: {
    async opens() {
      await this.drawLine();
    },
    async drawLine() {

      let _this = this;
      let parm = { paperId: this.sysId }
      let res = await getPaperSysListApi(parm)
      let result = res.data;

      console.log("===000=== _this", _this)
      console.log("===000=== parm", parm)
      console.log("===000=== res", res)
      console.log("===000=== result", result)

      //生成echarts容器
      for (let g = 0; g < result.length; g++) {
        if (result[g].type == '1' || result[g].type == '2') {
          let item = {
            barChart: '',       // chart 对象实例
            id: 'id' + g,       // 为了标示 id 不同
          }
          this.chartList.push(item);
        }
      }

      this.$nextTick(() => {
        for (let i = 0; i < _this.chartList.length; i++) {
          var myChart = _this.$echarts.init(document.getElementById(_this.chartList[i].id))
          console.log("===myChart===", myChart)
          //配置option属性
          var option = null;
          if (result[i].type == '1') {
            option = {
              title: {
                text: '',
                left: 'left',
                // top: '5%'
              },
              tooltip: {
                trigger: 'item'
              },
              legend: {
                orient: 'vertical',
                left: 'left',
                top: '30%'
              },
              series: [
                {
                  name: '票数',
                  type: 'pie',
                  radius: '50%',
                  data: [],
                  emphasis: {
                    itemStyle: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                  }
                }
              ]
            };
            //设置试题的标题
            option.title.text = i + 1 + "." + result[i].title;
            //生成选项
            if (result[i].questionOptionResultList && result[i].questionOptionResultList.length > 0) {
              for (var j = 0; j < result[i].questionOptionResultList.length; j++) {
                var obj = { value: 0, name: '' }
                obj.value = result[i].questionOptionResultList[j].total
                obj.name = result[i].questionOptionResultList[j].content
                option.series[0].data.push(obj);
              }
            }
          } else {
            option = {
              title: {
                text: '',
                left: 'left',
                top: '5%'
              },
              xAxis: {
                type: 'category',
                data: []
              },
              yAxis: {
                type: 'value'
              },
              series: [{
                data: [],
                type: 'bar'
              }]
            };
            option.title.text = i + 1 + "." + result[i].title;
            if (result[i].questionOptionResultList && result[i].questionOptionResultList.length > 0) {
              for (var j = 0; j < result[i].questionOptionResultList.length; j++) {
                option.xAxis.data.push(result[i].questionOptionResultList[j].content)
                option.series[0].data.push(result[i].questionOptionResultList[j].total)
              }
            }
          }
          console.log("i = ", i)
          console.log("option ", option)
          myChart.setOption(option);
        }
      })
    },
    //分析图
    async lookAsines(row) {
      this.chartList = [];
      this.sysId = row.paperId
      this.echartsDialog.title = "【" + row.title + "】分析图"
      this.echartsDialog.visible = true
    },
    sysOnConfirm() {
      this.echartsDialog.visible = false
    },
    sysOnClose() {
      this.echartsDialog.visible = false
    },
    //导出
    onExportExcel() {
      // console.log("===000000===onExportExcel===");
      // console.log("===000000===this.activityId===", this.activityId);

      window.open("http://localhost:8089/api/activity/export/getTotalListByActivityId/"+this.activityId)
      // window.open("http://43.140.195.12:8089/api/activity/export/getTotalListByActivityId/"+this.activityId)
      // window.open("https://adminapi.xiangwenli.com/api/activity/export/getTotalListByActivityId/"+this.activityId)

    },
    //弹框确认
    onConfirm() {
      this.totalDialog.visible = false;
    },
    //弹框的关闭
    onClose() {
      this.totalDialog.visible = false;
    },
    //查看票数
    async lookBtn(row) {
      const rLoading = this.openLoading();
      this.title = row.title;
      // document.getElementById('precent').style.width = '80%'

      console.log("===row===", row);
      console.log("===row.activityId===", row.activityId);

      this.activityId = row.activityId
      //查询统计数据
      // let res = await getPaperTotalListApi({ paperId: row.paperId });

      let res = await getTotalListByActivityIdApi(row.activityId);
      if (res && res.data && res.code == 200) {

        console.log("===res.data===", res.data);
        console.log("===res.data.selectQuestionList===", res.data.selectQuestionList);
        // console.log("===res.data.blankQuestionList===", res.data.blankQuestionList);

        this.selectQuestionList = res.data.selectQuestionList;
        // this.blankQuestionList = res.data.blankQuestionList;
        // console.log("===res.data.questionList===", res.data.questionList);
        // console.log("===this.questionList===", this.questionList);

        this.totalDialog.visible = true;
        rLoading.close();

      }
      // this.totalDialog.visible = true;
      // rLoading.close();
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
      console.log("888 this.parms", this.parms)
      let res = await getPaperListApi(this.parms);
      console.log("res", res)
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.tableList = res.data.records;
        //分页总条数
        this.parms.total = res.data.total;
      }
      console.log(res);
    },

    returnPapersPage() {
      this.$router.push({ path:  "/papers" });
    },

  },
};
</script>

<style lang="scss" scoped>
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
//.container2 {
//  width: 100%;
//  background-color: #ddd;
//}
//.skills {
//  text-align: right;
//  padding-right: 20px;
//  line-height: 30px;
//  color: white;
//}

.css {
  width: 60%;
  height:30px;
  background-color: #2196F3;
}

//.html {width: 90%; background-color: #4CAF50;}
//.js {width: 65%; background-color: #f44336;}
//.php {width: 60%; background-color: #808080;}

</style>
