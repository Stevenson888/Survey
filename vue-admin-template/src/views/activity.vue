<template>
  <el-main>

  <div>
<!--    <div style="margin: 10px 0">-->
<!--      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="params.name"></el-input>-->
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
<!--      &lt;!&ndash; <el-upload action="http://local host:9090/activity/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> &ndash;&gt;-->
<!--    </div>-->


    <el-form
      :model="params"
      ref="searchForm"
      label-width="100px"
      :inline="true"
      size="small"
    >
      <el-form-item label="搜索问卷名称">
        <el-input v-model="params.name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="load">搜索</el-button>
        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-if="user.roleFlag === 'ROLE_ADMIN'">创建问卷</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" :height="tableHeight"  border stripe :header-cell-class-name="'headerBg'" height="700px" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
<!--      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>-->
<!--      <el-table-column prop="activityId" label="活动id(FT)"></el-table-column>-->
      <el-table-column prop="project.name" label="归属项目"></el-table-column>
      <el-table-column prop="project.code" label="项目编号"></el-table-column>
      <el-table-column prop="title" label="问卷名称"></el-table-column>
      <el-table-column prop="createTime" label="问卷创建时间"></el-table-column>
      <el-table-column prop="endTime" label="问卷结束时间"></el-table-column>

<!--      <el-table-column prop="isActive" label="问卷状态"></el-table-column>-->
      <el-table-column prop="isActive" label="问卷状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isActive == '0'" type="danger" size="normal">关闭</el-tag>
          <el-tag v-if="scope.row.isActive == '1'" type="success" size="normal">调查中</el-tag>
        </template>
      </el-table-column>

<!--      <el-table-column prop="areaNameList" label="问卷覆盖区域"></el-table-column>-->
      <!-- el-table的一个cell中显示一个列表 -->
      <el-table-column
        label="项目覆盖区域"
        align="left"
        width="180px">
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.areaNameList" :key="index+'abc'" >
            <div v-if="scope.row.areaNameList.length>1">{{index+1}}.{{item}}</div>
            <div v-else>{{item}}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="forUserType" label="问卷发布对象">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.forUserType == '1'" type="primary" size="normal">消费者</el-tag>
          <el-tag v-if="scope.row.forUserType == '2'" type="warning" size="normal">商户</el-tag>
          <el-tag v-if="scope.row.forUserType == '4'" type="info" size="normal">访问员</el-tag>
          <el-tag v-if="scope.row.forUserType == '8'" type="danger" size="normal">客户经理</el-tag>
        </template>
      </el-table-column>

<!--      <el-table-column prop="forUserType" label="指定类型的用户可参与,对应用户类型"></el-table-column>-->

<!--      <el-table-column prop="" label="问卷关联物料种类"></el-table-column>-->


<!--      <el-table-column prop="info" label="活动简介"></el-table-column>-->
<!--      <el-table-column prop="content" label="活动规则描述"></el-table-column>-->
<!--      <el-table-column prop="answerCount" label="答卷数量"></el-table-column>-->
<!--      <el-table-column prop="createUid" label="活动创建人id"></el-table-column>-->
<!--      <el-table-column prop="createTime" label="创建时间"></el-table-column>-->
<!--      <el-table-column prop="updateUid" label="修改人id"></el-table-column>-->
<!--      <el-table-column prop="updateTime" label="修改时间"></el-table-column>-->

<!--      <el-table-column label="操作"  width="180" align="center">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>-->
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
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column align="center" width=230 label="操作">
        <template slot-scope="scope">

          <!--      20231203改之前-->
<!--          <el-button-->
<!--            icon="el-icon-pie-chart"-->
<!--            type="danger"-->
<!--            size="small"-->
<!--            @click="$router.push('/papers')"-->
<!--          >进度</el-button-->
<!--          >-->
          <!--      20231203改之前-->

          <!--      20231203改之后-->
          <el-button
            icon="el-icon-pie-chart"
            type="danger"
            size="small"
            @click="process(scope.row)"
          >进度</el-button
          >
          <!--      20231203改之后-->

          <el-button
            icon="el-icon-edit"
            type="primary"
            size="small"
            @click="handleEdit(scope.row)"
            v-if="user.roleFlag === 'ROLE_DEV' || user.roleFlag === 'ROLE_ADMIN'"
          >编辑</el-button
          >
<!--          <el-button-->
<!--            icon="el-icon-delete"-->
<!--            type="danger"-->
<!--            size="small"-->
<!--            @click="del(scope.row)"-->
<!--          >删除</el-button-->
<!--          >-->
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="params.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="params.total"
          background>
      </el-pagination>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal="false">
      <el-form label-width="150px" size="small" style="width: 80%; margin: 0 auto">
<!--        <el-form-item label="问卷id">-->
<!--          <el-input v-model="form.activityId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

        <el-form-item label="问卷标题">
          <el-input v-model="form.title" autocomplete="off" style="width:400px"></el-input>
        </el-form-item>

        <!--        <el-form-item label="问卷状态">     &lt;!&ndash;0下线; 1上线&ndash;&gt;-->
        <!--          <el-input v-model="form.isActive" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item  label="问卷状态">
          <el-select v-model="form.isActive" placeholder="请选择问卷状态" style="width:400px">
            <el-option v-for="(activityStatus,index) in activityStatusOptions" :label="activityStatus.name" :key="activityStatus.id" :value="activityStatus.id" ></el-option>
          </el-select>
        </el-form-item>

<!--        <el-form-item label="用户类型">     &lt;!&ndash; 用户类型 1消费者 2 商户 4访问员 8客户经理 &ndash;&gt;-->
<!--          <el-input v-model="form.forUserType" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item  label="问卷发布对象">
          <el-select v-model="form.forUserType" placeholder="请选择用户类型" style="width:400px">
            <el-option v-for="(userType,index) in userTypeOptions" :label="userType.name" :key="userType.id" :value="userType.id" ></el-option>
          </el-select>
        </el-form-item>

<!--        <el-form-item label="问卷简介">-->
<!--          <el-input v-model="form.info" autocomplete="off" :disabled="readOnly"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="问卷内容">-->
<!--          <el-input v-model="form.content" autocomplete="off" :disabled="readOnly"></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="答卷数量">-->
<!--          <el-input v-model="form.answerCount" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="问卷开始时间" >
          <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" style="width:400px"></el-date-picker>
        </el-form-item>
        <el-form-item label="问卷结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" style="width:400px"></el-date-picker>
        </el-form-item>
<!--        <el-form-item label="问卷创建人id">-->
<!--          <el-input v-model="form.createUid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="创建时间">-->
<!--          <el-date-picker v-model="form.createTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="修改人id">-->
<!--          <el-input v-model="form.updateUid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="修改时间">-->
<!--          <el-date-picker v-model="form.updateTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->

        <!-- 省市区三级联动下拉框 element-china-area-data  -->
<!--            <el-form-item label="项目覆盖区域" >-->
<!--              <el-cascader-->
<!--                :props="props"-->
<!--                clearable-->
<!--                filterable-->
<!--                size="large"-->
<!--                :options="options"-->
<!--                v-model="selectedOptions"-->
<!--                @change="addressChange">-->
<!--              </el-cascader>-->
<!--            </el-form-item>            -->
            <el-form-item label="问卷覆盖区域"  v-show="!readOnly">
              <el-cascader
                :props="props"
                clearable
                filterable
                size="large"
                :options="options"
                v-model="form.selectedAreaOptions"
                @change="addressChange">
              </el-cascader>
            </el-form-item>
<!--        selectedOptions: [],form.selectArea-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>

    </el-main>
</template>

<script>
import {deleteProjectApi, getProjectPageApi, insertOrUpdateProjectApi} from "@/api/project";
import {
  getActivityPageApi,
  insertOrUpdateActivityApi,
  addActivityApi,
  editActivityApi,
  deleteActivityApi
} from "@/api/activity";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";
import {getPartnerListByAdminApi} from "@/api/user";
import {getPapersByActivityIdApi} from "@/api/paper";

export default {
  name: "Activity",
  data() {
    return {
      tableData: [],
      tableHeight: 0,
      // total: 0,
      // pageNum: 1,
      // pageSize: 10,
      // name: "",
      form: {},
      params: {
        id: "",
        name: "",
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
        // activityId: 0,
        title: "",
      },
      dialogFormVisible: false,
      readOnly:false,
      multipleSelection: [],

      options: regionData,
      // selectedOptions: [],
      props: { multiple: true },

      userTypeOptions: [
        {id: 1, name: "消费者"},
        {id: 2, name: "商户"},
        {id: 8, name: "客户经理"} //20231206 2.发布对象选项增加“客户经理”
      ],

      activityStatusOptions: [  //is_active 活动状态：0下线; 1上线
        {id: 0, name: "关闭"},
        {id: 1, name: "调查中"}
      ],

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    load() {
      this.params.pageNum = 1;
      this.getList();
    },
    async getList(){
      // const rLoading = this.openLoading();
      let res = await getActivityPageApi(this.params);

      console.log(res);
      if (res && res.data && res.code == 200) {
        // rLoading.close();
        console.log("======res.data.records=====",  res.data.records);
        //给表格数据赋值
        this.tableData = res.data.records;
        //总条数
        this.params.total = res.data.total;
      }

      // this.request.get("/activity/page", {
      //   params: {
      //     pageNum: this.pageNum,
      //     pageSize: this.pageSize,
      //     name: this.name,
      //   }
      // }).then(res => {
      //   this.tableData = res.data.records
      //   this.total = res.data.total
      // })

    },
    // save() {
    //   this.request.post("/activity", this.form).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("保存成功")
    //       this.dialogFormVisible = false
    //       this.load()
    //     } else {
    //       this.$message.error("保存失败")
    //     }
    //   })
    // },


    //省市区三级联动下拉框
    // addressChange(arr) {
    //   // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
    //   console.log("111===arr===", arr);
    //   console.log("222===codeToText[arr[0]]===", codeToText[arr[0]] );
    //   console.log("333===codeToText[11]===", codeToText[11] );
    //   console.log("444===", codeToText[arr[0]], codeToText[arr[1]], codeToText[arr[2]]);
    //   var areaCode = arr[2];
    //   console.log("888===areaCode",  areaCode);
    // },

    async save() {
      // console.log("======save() this.form.selectArea======", this.form.selectedArea)

      // return


      let res = await insertOrUpdateActivityApi(this.form);
      if (res.code === 200 ) {
        this.$message.success("保存成功")
        this.dialogFormVisible = false
        this.load()
      } else {
        this.$message.error("保存失败")
      }
    },

    handleAdd() {
      this.$message.success("敬请期待！")
      // this.readOnly = false
      // this.dialogFormVisible = true
      // this.form = {}
      // this.$nextTick(() => {
      //   if(this.$refs.img) {
      //      this.$refs.img.clearFiles();
      //    }
      //    if(this.$refs.file) {
      //     this.$refs.file.clearFiles();
      //    }
      // })
    },
    handleEdit(row) {
      this.readOnly = true
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true

      console.log("======handleEdit() this.form======", this.form)

       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },

    async process(row) {
      console.log("======7888 process() row======", row)
      console.log("======7888 process() row.activityId======", row.activityId)
      console.log("======7888 process() this.params======", this.params)
      // console.log("======7888 process() row.activityId======", row.title)

      this.$router.push('/answers/' + row.activityId)

      // let res = await getPapersByActivityIdApi(row.activityId, this.params);
      // console.log(res);
      //
      // if (res && res.data && res.code == 200) {
      //   // rLoading.close();
      //   console.log("======res.data.records=====",  res.data.records);
      //   //给表格数据赋值
      //   this.tableData = res.data.records;
      //   //总条数
      //   this.params.total = res.data.total;
      // }

    },

    // async getPaperList(){
    //   console.log("======this.params=====",  this.params);
    //
    //   let res = await getPapersByActivityIdApi(this.params);
    //   console.log(res);
    //
    //   if (res && res.data && res.code == 200) {
    //     // rLoading.close();
    //     console.log("======res.data.records=====",  res.data.records);
    //     //给表格数据赋值
    //     this.tableData = res.data.records;
    //     //总条数
    //     this.params.total = res.data.total;
    //   }
    // },

    //删除按钮
    async del(row) {
      //信息确认提示
      let confirm = await this.$myconfirm('确定删除该数据吗?')
      console.log(confirm)
      if(confirm){
        let res = await deleteActivityApi({id:row.id})
        if(res && res.code == 200){
          //信息提示
          this.$message.success(res.msg)
          //刷新表格
          this.getList();
        }
      }
    },

    // del(id) {
    //   this.request.delete("/activity/" + id).then(res => {
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
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/activity/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.params.name = ""
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
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },

    //省市区三级联动下拉框
    addressChange(arr) {
      // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
      console.log("111===arr===", arr);
      console.log("222===codeToText[arr[0]]===", codeToText[arr[0]] );
      console.log("333===codeToText[11]===", codeToText[11] );
      console.log("444===", codeToText[arr[0]], codeToText[arr[1]], codeToText[arr[2]]);
      var areaCode = arr[2];
      console.log("888===areaCode",  areaCode);
    },

  }
}
</script>


<style>

</style>
