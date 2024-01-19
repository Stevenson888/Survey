<template>
  <div class="dashboard-container" >

    <div  style="padding: 0 0 30px 20px;  ">
      <i class="el-icon-loading" style="background-color:#eae757ff;"></i>
      公告：享问礼小程序管理后台上线啦！（如多条滚动显示）
    </div>

    <el-card class="" style="height:250px">
      <div slot="header" class="" style="padding-left: 50px">
            <el-row style="margin-bottom:-10px;">
              <el-col :span="20"><div class="font-title" style="">昨日核心数据</div></el-col>
              <el-col :span="4"><div class="font-title padding-top-10" style="">核心数据明细<i class="el-icon-bottom"></i></div></el-col>
            </el-row>
            <!-- date -->
            <el-row >
              <div class="timeBox padding-xl" >
                <p class="text-24 text-white" v-html="formatedDate(date)"></p>
              </div>
            </el-row>
<!--        </el-col>-->
<!--        <el-col :span="8" style="float: right; padding-right: 20px;">-->
<!--            <el-row :gutter="20">-->
<!--&lt;!&ndash;              <div class="timeBox padding-xl">&ndash;&gt;-->
<!--&lt;!&ndash;                <p class="text-24 text-white" v-html="formateTimeStamp(date)"></p>&ndash;&gt;-->
<!--&lt;!&ndash;              </div>&ndash;&gt;-->
<!--            </el-row>-->
<!--        </el-col>-->
      </div>

      <div class="text item" style="background-color: #ffffff;">
        <!--    <el-row  justify="space-between">-->
        <el-row :gutter="20" class="container" style="text-align:center" justify="space-between">
          <el-col :span="6">
            <div class="item padding-top-10"  style="">
              <el-row :gutter="20"> 累计问卷数量 </el-row>
              <el-row :gutter="20" class="numberItem" style=""> {{ totalAnswerAmount }} </el-row>
            </div>
          </el-col>

          <el-divider direction="vertical"></el-divider>

          <el-col :span="6">
            <div class="item padding-top-10" style="">
              <el-row :gutter="20"> 累计参与门店 </el-row>
              <el-row :gutter="20"  class="numberItem" style=""> {{totalStoreAmount}} </el-row>
            </div>
          </el-col>

          <el-divider direction="vertical"></el-divider>

          <el-col :span="6">
            <div class="item padding-top-10" style="">
              <el-row :gutter="20"> 昨日新增问卷数量 </el-row>
              <el-row :gutter="20"  class="numberItem" style=""> {{ lastdayAnswerAmount }} </el-row>
            </div>
          </el-col>

          <el-divider direction="vertical"></el-divider>

          <el-col :span="6">
            <div class="item padding-top-10" style="">
              <el-row :gutter="20"> 昨日参与门店 </el-row>
              <el-row :gutter="20"  class="numberItem" style=""> {{lastdayStoreAmount}} </el-row>
            </div>
          </el-col>

        </el-row>
      </div>

    </el-card>

    <br>

    <el-card class="box-card" style="height:400px">
      <div slot="header" class="clearfix" style="padding-left: 50px">
        <el-row :gutter="20" style="margin-bottom:-10px;">
          <el-col :span="16"><div class="font-title">昨日来源数据</div></el-col>
        </el-row>
        <!-- date -->
        <el-row :gutter="20">
          <div class="timeBox padding-left-10">
            <p class="text-24 text-white" v-html="formatedDate(date)"></p>
          </div>
        </el-row>

      </div>

      <div class="text item" style="background-color: #ffffff;">
        <el-row :gutter="24" class="container" style="text-align:left" justify="space-between">

          <el-col :span="12">
            <div class="item padding-top-20 padding-left-60 padding-right-20" style="">
              <el-row :gutter="20" class="font-title"> 昨日新增问卷来源 </el-row>

<!--              <el-divider direction="vertical"></el-divider>-->

              <!-- 2.7 当前管理员下，昨日新增问卷来源 answerTotalLastday -->
              <div v-for="(answer,index) in answerListOfAnswer" :key="index+'a'">
<!--                <el-row class="padding-top-20" style="">-->
<!--                  <el-col :span="12" style="text-align: left"><div >{{index+1}}.{{answer.activity.title}}</div></el-col>-->
<!--                  <el-col :span="10" class="padding-left-10"><div class="css" :style="{width:(answer.total/lastdayAnswerAmount * 100).toFixed(2)+'%'}"></div></el-col>-->
<!--                  <el-col :span="2" ><div style=""></div></el-col>-->
<!--                  <el-col :span="2" ><div style="text-align:right; float: right; font-weight: bolder;">{{ answer.total }}</div></el-col>-->
<!--                </el-row>-->

                    <el-row class="padding-top-20" style="clear:both">
                      <el-col :span="12" style="text-align: left"><div >{{index+1}}.{{answer.activity.title}}</div></el-col>
                      <el-col :span="10" class="padding-left-10"><div class="css" :style="{width:(answer.total/lastdayAnswerAmount * 100).toFixed(2)+'%'}"></div></el-col>
                      <el-col :span="2" ><div style=""></div></el-col>
                      <el-col :span="2" ><div style="text-align:right; float: right; font-weight: bolder;">{{ answer.total }}</div></el-col>
                    </el-row>

              </div>

            </div>
          </el-col>

          <el-divider direction="vertical"></el-divider>

          <el-col :span="12">
            <div class="item padding-top-20 padding-left-60 padding-right-20" style="">
              <el-row :gutter="20" class="font-title"> 昨日参与门店来源 </el-row>

              <!--    2.8 当前管理员下，昨日门店来源 storeTotalLastday 门店数 -->
              <div v-for="(answer,index) in answerListOfStore" :key="index+'b'">
                <el-row class="padding-top-20" style="clear:both">
                  <el-col :span="12" style="text-align: left"><div >{{index+1}}.{{answer.activity.title}}</div></el-col>
<!--                  <el-col :span="2" ></el-col>-->
                  <el-col :span="10" style=""><div class="css" :style="{width:(answer.total/lastdayStoreAmount * 100).toFixed(2)+'%'}"></div></el-col>
<!--                  <el-col :span="12" style=""><div>{{answer.total}} lastdayStoreAmount:{{lastdayStoreAmount}} </div></el-col>-->
                  <el-col :span="2" ><div style=""></div></el-col>
                  <el-col :span="2" ><div style="text-align:right; float: right; font-weight: bolder; ">{{ answer.total }}</div></el-col>
                </el-row>
              </div>

            </div>
          </el-col>

        </el-row>
      </div>

    </el-card>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getAnswerAmountByDevUserIdApi,
  getStoreAmountByDevUserIdApi,
  getLastdayAnswerAmountByDevUserIdApi,
  getLastdayAnswerListStoreAmountByDevUserIdApi,
  getGroupLastdayAnswerListByDevUserIdApi,
  getGroupLastdayAnswerListStoreAmountByDevUserIdApi,

  getAnswerAmountByAdminUserIdApi,
  getStoreAmountByAdminUserIdApi,
  getLastdayAnswerAmountByAdminUserIdApi,
  getLastdayAnswerListStoreAmountByAdminUserIdApi,
  getGroupLastdayAnswerListByAdminUserIdApi,
  getGroupLastdayAnswerListStoreAmountByAdminUserIdApi,

  getAnswerAmountByPartnerUserIdApi,
  getStoreAmountByPartnerUserIdApi,
  getLastdayAnswerAmountByPartnerUserIdApi,
  getLastdayAnswerListStoreAmountByPartnerUserIdApi,
  getGroupLastdayAnswerListByPartnerUserIdApi,
  getGroupLastdayAnswerListStoreAmountByPartnerUserIdApi
} from "@/api/answer";


export default {
  name: "Home",
  computed: {
    ...mapGetters(["name"]),
  },

  data() {
    return {
      userId: "",
      // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      parms: {
        userId:"",
      },
      totalAnswerAmount: "",
      totalStoreAmount: "",
      lastdayAnswerAmount: "",
      lastdayStoreAmount: "",
      // answerTotalLastday: "",
      // storeTotalLastday: "",
      // answerList: [],
      answerListOfAnswer: [],
      answerListOfStore: [],

      date: new Date().getTime(),

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()
    // this.loadTime()
  },
  methods: {
    load() {
      this.parms.userId = this.user.userId
      const rLoading = this.openLoading();

      // this.$nextTick(() => {
      // });

      if (this.user.roleFlag == "ROLE_DEV"){
          console.log("===ROLE_DEV")
          // 1.1
          this.getAnswerAmountByDevUserId();
          // 1.2
          this.getStoreAmountByDevUserId();
          // 1.3
          this.getLastdayAnswerAmountByDevUserId();
          // 1.4
          this.getLastdayAnswerListStoreAmountByDevUserId();
          // 1.5
          this.getGroupLastdayAnswerListByDevUserId();
          // 1.6
          this.getGroupLastdayAnswerListStoreAmountByDevUserId();

      } else if (this.user.roleFlag == "ROLE_ADMIN"){

          console.log("===ROLE_ADMIN")

          // 2.1
          this.getAnswerAmountByAdminUserId();
          // 2.2
          this.getStoreAmountByAdminUserId();
          // 2.3
          this.getLastdayAnswerAmountByAdminUserId();
          // 2.4
          this.getLastdayAnswerListStoreAmountByAdminUserId();
          // 2.5
          this.getGroupLastdayAnswerListByAdminUserId();
          // 2.6
          this.getGroupLastdayAnswerListStoreAmountByAdminUserId();

      }  else if (this.user.roleFlag == "ROLE_PARTNER") {

          console.log("===ROLE_PARTNER")

          // 3.1
          this.getAnswerAmountByPartnerUserId();
          // 3.2
          this.getStoreAmountByPartnerUserId();
          // 3.3
          this.getLastdayAnswerAmountByPartnerUserId();
          // 3.4
          this.getLastdayAnswerListStoreAmountByPartnerUserId();
          // 3.5
          this.getGroupLastdayAnswerListByPartnerUserId();
          // 3.6
          this.getGroupLastdayAnswerListStoreAmountByPartnerUserId();
      }

      rLoading.close();
    },


    //1.1 累计问卷数量
    async getAnswerAmountByDevUserId() {
      let res = await getAnswerAmountByDevUserIdApi(this.parms);
      if (res && res.code == 200) {
        console.log("===1.1 累计问卷数量 totalAnswerAmount=== res.data", res.data)
        this.totalAnswerAmount =  res.data
      }
    },
    //1.2 累计参与门店
    async getStoreAmountByDevUserId() {
      let res = await getStoreAmountByDevUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===1.2 累计参与门店 totalStoreAmount=== res.data", res.data)
        this.totalStoreAmount =  res.data
      }
    },
    //1.3 当前管理员下，昨日新增问卷数量
    async getLastdayAnswerAmountByDevUserId() {
      let res = await getLastdayAnswerAmountByDevUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===1.3 昨日新增问卷数量 lastdayAnswerAmount=== res.data", res.data)
        this.lastdayAnswerAmount =  res.data
      }
    },
    //1.4 当前管理员下，昨日参与门店
    async getLastdayAnswerListStoreAmountByDevUserId() {
      let res = await getLastdayAnswerListStoreAmountByDevUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===1.4 昨日参与门店 lastdayStoreAmount=== res.data", res.data)
        this.lastdayStoreAmount =  res.data
      }
    },
    //1.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    async getGroupLastdayAnswerListByDevUserId() {
      let res = await getGroupLastdayAnswerListByDevUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===1.5 昨日新增问卷来源 answerListOfAnswer=== res.data", res.data)
        this.answerListOfAnswer =  res.data
      }
    },
    //1.6 当前管理员下，昨日门店来源 storeTotalLastday
    async getGroupLastdayAnswerListStoreAmountByDevUserId() {
      let res = await getGroupLastdayAnswerListStoreAmountByDevUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===1.6 昨日参与门店来源 answerListOfStore=== res.data", res.data)
        this.answerListOfStore =  res.data
      }
    },



    //2.1 累计问卷数量
    // async getAnswerListByAdminUserId() {
    async getAnswerAmountByAdminUserId() {
      // let res = await getAnswerListByAdminUserIdApi(this.parms);
      let res = await getAnswerAmountByAdminUserIdApi(this.parms);
      if (res && res.code == 200) {
        console.log("===2.1 累计问卷数量 totalAnswerAmount=== res.data", res.data)
        this.totalAnswerAmount =  res.data
      }
    },

    //2.2 累计参与门店
    async getStoreAmountByAdminUserId() {
      let res = await getStoreAmountByAdminUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===2.2 累计参与门店 totalStoreAmount=== res.data", res.data)
        this.totalStoreAmount =  res.data
      }
    },

    //2.3 当前管理员下，昨日新增问卷数量
    // async getLastdayAnswerListByAdminUserId() {
    //   let res = await getLastdayAnswerListByAdminUserIdApi(this.parms);
    //   if (res && res.data && res.code == 200) {
    //     console.log("===2.3 昨日新增问卷数量 lastdayAnswerAmount=== res.data", res.data)
    //     this.lastdayAnswerAmount =  res.data
    //   }
    // },

    //2.3 当前管理员下，昨日新增问卷数量
    async getLastdayAnswerAmountByAdminUserId() {
      let res = await getLastdayAnswerAmountByAdminUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===2.3 昨日新增问卷数量 lastdayAnswerAmount=== res.data", res.data)
        this.lastdayAnswerAmount =  res.data
      }
    },

    //2.4 当前管理员下，昨日参与门店
    async getLastdayAnswerListStoreAmountByAdminUserId() {
      let res = await getLastdayAnswerListStoreAmountByAdminUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===2.4 昨日参与门店 lastdayStoreAmount=== res.data", res.data)
        this.lastdayStoreAmount =  res.data
      }
    },

    //2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    async getGroupLastdayAnswerListByAdminUserId() {
      let res = await getGroupLastdayAnswerListByAdminUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===2.5 昨日新增问卷来源 answerListOfAnswer=== res.data", res.data)
        this.answerListOfAnswer =  res.data
      }
    },

    //2.6 当前管理员下，昨日门店来源 storeTotalLastday
    async getGroupLastdayAnswerListStoreAmountByAdminUserId() {
      let res = await getGroupLastdayAnswerListStoreAmountByAdminUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===2.6 昨日参与门店来源 answerListOfStore=== res.data", res.data)
        this.answerListOfStore =  res.data
      }
    },


    //3.1 累计问卷数量
    async getAnswerAmountByPartnerUserId() {
      let res = await getAnswerAmountByPartnerUserIdApi(this.parms);
      if (res && res.code == 200) {
        console.log("===3.1 累计问卷数量 totalAnswerAmount=== res.data", res.data)
        this.totalAnswerAmount =  res.data
      }
    },
    //3.2 累计参与门店
    async getStoreAmountByPartnerUserId() {
      let res = await getStoreAmountByPartnerUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===3.2 累计参与门店 totalStoreAmount=== res.data", res.data)
        this.totalStoreAmount =  res.data
      }
    },
    //3.3 当前管理员下，昨日新增问卷数量
    async getLastdayAnswerAmountByPartnerUserId() {
      let res = await getLastdayAnswerAmountByPartnerUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===3.3 昨日新增问卷数量 lastdayAnswerAmount=== res.data", res.data)
        this.lastdayAnswerAmount =  res.data
      }
    },
    //3.4 当前管理员下，昨日参与门店
    async getLastdayAnswerListStoreAmountByPartnerUserId() {
      let res = await getLastdayAnswerListStoreAmountByPartnerUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===3.4 昨日参与门店 lastdayStoreAmount=== res.data", res.data)
        this.lastdayStoreAmount =  res.data
      }
    },
    //3.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    async getGroupLastdayAnswerListByPartnerUserId() {
      let res = await getGroupLastdayAnswerListByPartnerUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===3.5 昨日新增问卷来源 answerListOfAnswer=== res.data", res.data)
        this.answerListOfAnswer =  res.data
      }
    },
    //3.6 当前管理员下，昨日门店来源 storeTotalLastday
    async getGroupLastdayAnswerListStoreAmountByPartnerUserId() {
      let res = await getGroupLastdayAnswerListStoreAmountByPartnerUserIdApi(this.parms);
      if (res && res.data && res.code == 200) {
        console.log("===3.6 昨日参与门店来源 answerListOfStore=== res.data", res.data)
        this.answerListOfStore =  res.data
      }
    },

    loadTime() {
      var _this = this;
      setInterval(() => {
        _this.date += 1000;
      }, 1000);

      // this.$api.serveTime({}).then(res => {
      //   console.log("服务器时间", res);
      //   var _this = this;
      //   _this.servertime = res;
      //   setInterval(() => {
      //     _this.date += 1000;
      //     console.log(_this.date, '**');
      //   }, 1000);
      // });
    },
    // 转换时间戳
    formateTimeStamp(time) {
      var date = new Date(time);
      var year = date.getFullYear();
      var month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
      var minute =
        date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
      var second =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      var week = ["日", "一", "二", "三", "四", "五", "六"][date.getDay()];
      // return year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second;
      return (
        '<span style="font-size: 66px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">' +
        hour +
        ":" +
        minute + ":" + second +
        "</span><br/>" +
        year +
        "年" +
        month +
        "月" +
        day +
        "日" +
        "&nbsp;&nbsp;&nbsp;星期" +
        week
      );
    },

    // 转换时间戳
    formatedDate(time) {
      var date = new Date(time);
      // console.log("date: ", date)
      // var date = new Date("2023-01-01 11:12:13");

      var year = date.getFullYear();
      var month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var day = date.getDate() < 10 ? "0" + (date.getDate()-1) : (date.getDate()-1);

      // 修改day为00
      if(day="00"){
        var yesterday = new Date(date.getTime() - 24*60*60*1000);

        year = yesterday.getFullYear();
        month =
          yesterday.getMonth() + 1 < 10
            ? "0" + (yesterday.getMonth() + 1)
            : yesterday.getMonth() + 1;
        day = yesterday.getDate() < 10 ? "0" + yesterday.getDate() : yesterday.getDate();
      }

      return (
        '<span style="font-size: 20px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">' +
        year +
        "年" +
        month +
        "月" +
        day +
        "日"
      );
    },


  }

};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.el-row {
  height:30px;
  //margin-bottom: 20px;
  &:last-child {
    //margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.el-divider--vertical {
  height: 8em;
}
.container{
  display: flex;
  height: 140px;
  background-color: #ffffff;
}
.container{
  /* justify-content: flex-start; */
  /* justify-content: flex-end; */
  /* justify-content: center; */
  /* justify-content: space-between; */
  /* justify-content: space-around; */
  justify-content: space-evenly;
}
.item{
  //padding: 0px 0 0 0;
  //padding-top: 10px;
  margin-bottom: -20px;
}
.numberItem{
  padding-top: 20px;
  font-size:30px;
  color: #000000;
  font-weight: 600
}
.padding-top-10{
  padding-top: 10px;
}
.padding-top-20{
  padding-top: 20px;
}
.padding-right-20{
  padding-right: 20px;
}
.padding-left-60{
  padding-left: 60px;
}
.padding-left-10{
  padding-left: 10px;
}
.padding-left-20{
  padding-left: 20px;
}
.css {
  //width: 60%;
  height:15px;
  background-color: #2196F3;
  line-height: 50%;
  //background-color: #8c939d;
}
.font-title {
  color: #000000;
  font-weight: 600;
}
.el-divider--vertical{
  display:inline-block;
  width:1px;
  height:200%;		//更改竖向分割线长度
  margin:0 8px;
  vertical-align:middle;
  position:relative;
}
</style>
