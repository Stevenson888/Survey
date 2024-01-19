<template>
    <el-main>
      <el-form
        :model="params"
        ref="searchForm"
        label-width="100px"
        :inline="true"
        size="small"
      >
        <el-form-item label="搜索项目名称">
          <el-input v-model="params.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" @click="load">搜索</el-button>
          <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-if="user.roleFlag === 'ROLE_ADMIN'">创建项目</el-button>
        </el-form-item>
      </el-form>

<!--    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" style="width: 100%" @selection-change="handleSelectionChange">-->
    <el-table :data="tableData" :height="tableHeight" border stripe :header-cell-class-name="'headerBg'" style="width: 100%" >
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
<!--      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>-->
<!--      <el-table-column prop="projectId" label="项目id(FT)" width=100px></el-table-column>-->
      <el-table-column prop="code" label="项目编号" width=130px></el-table-column>
      <el-table-column prop="name" label="项目名称"></el-table-column>
      <el-table-column prop="createTime" label="项目创建时间" width=100px></el-table-column>
      <el-table-column prop="startTime" label="项目启动时间" width=100px></el-table-column>
      <el-table-column prop="endTime" label="项目结束时间" width=100px></el-table-column>

      <!-- el-table的一个cell中显示一个列表 -->
      <el-table-column
        label="项目覆盖区域"
        align="left"
        width="160px">
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.areaNameList" :key="index+'abc'" >
            <div v-if="scope.row.areaNameList.length>1">{{index+1}}.{{item}}</div>
            <div v-else>{{item}}</div>
          </div>
        </template>
      </el-table-column>

      <!-- 数组元素的值动态展示到table的header中-->
      <!--      <el-table-column v-for='(item,i) in dataColum' :key="i" :label="item" show-overflow-tooltip>-->
      <!--        <template slot-scope="scope">-->
      <!--          {{scope.row[i]}}-->
      <!--        </template>-->
      <!--      </el-table-column>-->

      <!-- el-table的一个cell中显示一个列表 -->
      <el-table-column
        label="项目关联活动"
        align="left"
        width="200px"
        >
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.selectedActivityList" :key="index+'ab'" >
            <div v-if="scope.row.selectedActivityList.length>1">{{index+1}}.{{item.title}}</div>
            <div v-else>{{item.title}}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="selectedProjectActivityListSize" label="项目关联活动数量" width="100px"></el-table-column>
<!--      <el-table-column prop="" label="项目关联物料种类" width=120px></el-table-column>-->

      <el-table-column align="center" label="操作" v-if="user.roleFlag === 'ROLE_ADMIN'">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="primary"
            size="small"
            @click="handleEdit(scope.row)"
          >编辑</el-button
          >
<!--          <el-button-->
<!--            icon="el-icon-delete"-->
<!--            type="danger"-->
<!--            size="small"-->
<!--            @click="deleteBtn(scope.row)"-->
<!--            v-if="user.roleFlag === 'ROLE_ADMIN'"-->
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

    <el-dialog title="信息" :visible.sync="dialogFormVisible"  width="60%" :close-on-click-modal="false">
      <el-form  :model="form" :rules="rules" ref="form" label-width="200px" size="large" style="width: 100%; margin: 0 auto;">
<!--        <el-form-item label="项目id">-->
<!--          <el-input v-model="form.projectId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="项目编号">-->
<!--          <el-input v-model="form.code" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" style="width:400px"></el-input>
        </el-form-item>
<!--        <el-form-item label="项目简介">-->
<!--          <el-input v-model="form.info" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="项目描述" prop="content">
          <el-input
                style="width:400px"
                v-model="form.content"
                autocomplete="off"
                type="textarea"
                :rows="4"
                placeholder="请输入内容"
          ></el-input>
        </el-form-item>
<!--        <el-form-item label="物料种类">-->
<!--          <el-input v-model="form.productCategory" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="项目创建人id">-->
<!--          <el-input v-model="form.createMuid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="项目修改人id">-->
<!--          <el-input v-model="form.updateMuid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="项目启动时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="项目结束时间" prop="endTime">
          <el-date-picker default-time="23:59:59" v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>

        <!-- 省市区三级联动下拉框 element-china-area-data  -->
<!--        <el-form-item label="项目覆盖区域">-->
<!--          <el-cascader-->
<!--            size="large"-->
<!--            :options="options"-->
<!--            v-model="selectedOptions"-->
<!--            @change="addressChange">-->
<!--          </el-cascader>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="项目关联活动" v-show="selectVisible">-->
        <el-form-item label="项目关联活动" prop="selectedActivityIdList" v-if="!isReadOnlyForSelect" v-show="selectVisible">
          <el-select v-model="form.selectedActivityIdList" multiple placeholder="请选择" @change="changeSelect($event)" style="width:600px;">
            <el-option
                v-for="item in allUnselectedActivityVoList"
                :key="item.id"
                :label="item.title"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目关联活动" v-else >
          <div v-for="(item,index) in selectedActivityList" :key="index+'abcd'" >     <!--style="border-style:solid;border-bottom: none;border-top: none"-->
            <div v-if="selectedActivityList.length>1">{{index+1}}.{{item.title}}</div>
            <div v-else>{{item.title}}</div>
          </div>
        </el-form-item>
      </el-form>

<!--      <div style="height:130px;"></div>-->

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save"  :disabled="isReadOnly">确 定</el-button>
      </div>
    </el-dialog>
    </el-main>
</template>

<script>

// const defaultTimeFormat = computed(() => {
//   const [start, end] = ["00:00:00", "23:59:59"].map(time => {
//     const [hour, minute, second] = time.split(":").map(Number);
//     return new Date(0, 0, 0, hour, minute, second);
//   });
//   const dateRange: [Date, Date] = [start, end]
//   return dateRange;
// });

import {
  getProjectApi,
  getProjectPageApi,
  getProjectListApi,
  editProjectApi,
  deleteProjectApi,
  getSelectedActivityListByProjectIdApi,
  insertOrUpdateProjectApi,
  getSelectedActivityIdListByProjectIdApi
} from "@/api/project";
import { getAllActivityListApi, getAllActivityVoListApi, getAllUnselectedActivityVoListApi} from "@/api/activity";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";
import debounceFirst, {debounce} from "@/utils/debounce";


export default {
  name: "Project",
  data() {
    return {
      allActivityList: [],
      allActivityVoList: [],
      allUnselectedActivityVoList: [],

      selectedActivityIdList: [],
      selectedActivityList: [],

      readOnly:false,
      isReadOnly:false,
      isReadOnlyForSelect:false,

      selectVisible:false,

      // id: "",
      projectId: "",

      // user:{},
      roles: [],

      tableData: [],
      tableHeight: 0,

      // form: {},
      // form: {
      //   selectedActivityIdList:[],  // 这个里面是roleId 数组，后台获取到list后页面会直接显示
      // },

      dialogFormVisible: false,
      // multipleSelection: [],

      params: {
        id: "",
        name: "",
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },

      options: regionData,
      selectedOptions: [],

      //新增表单绑定的数据域
      form: {
        name: "",
        content: "",
        startTime: "",
        endTime: "",
        selectedActivityIdList:[],  // 这个里面是roleId 数组，后台获取到list后页面会直接显示
      },

      //新增表单的验证规则
      rules: {
        name: [
          {
            trigger: "change",
            message: "请填写项目名称",
            required: true,
          },
        ],
        // content: [
        //   {
        //     trigger: "blur",
        //     message: "请填写项目描述",
        //     required: true,
        //   },
        // ],
        startTime: [
          {
            trigger: "change",
            message: "请填写项目启动时间",
            required: true ,
          },
        ],
        endTime: [
          {
            trigger: "change",
            message: "请填写项目结束时间",
            required: true,
          },
        ],
        // selectedActivityIdList: [
        //   {
        //     trigger: "change",
        //     message: "请选择项目关联活动",
        //     required: true,
        //   },
        // ],
      },

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
      this.getAllActivityVoList();
      this.getAllUnselectedActivityVoList();
    },

    async getList() {
      let res = await getProjectPageApi(this.params);

      console.log(res);
      if (res && res.data && res.code == 200) {
        console.log("======res.data.records=====",  res.data.records);
        //给表格数据赋值
        this.tableData = res.data.records;
        //总条数
        this.params.total = res.data.total;
      }
    },

    async getAllActivityList() {
      let response = await getAllActivityListApi(this.params);
      console.log(response);
      if (response && response.code == 200) {
        this.allActivityList = response.data;
      }
    },

    async getAllActivityVoList() {
      let response = await getAllActivityVoListApi(this.params);
      console.log(response);
      if (response && response.code == 200) {
        //activity下拉框
        this.allActivityVoList = response.data;
        console.log("======this.allActivityVoList=====",  this.allActivityVoList);
      }
    },

    async getAllUnselectedActivityVoList() {
      let response = await getAllUnselectedActivityVoListApi(this.params);
      console.log(response);
      if (response && response.code == 200) {
        //activity下拉框
        this.allUnselectedActivityVoList = response.data;
        console.log("======this.allUnselectedActivityVoList=====",  this.allUnselectedActivityVoList);
      }
    },

    // //加号
    // addList() {
    //   this.activitySelectField.push({activityTitle: ''})
    // },
    // //减号
    // subList(index) {
    //   this.activitySelectField.splice(index, 1)
    // },
    addItem(){
      // 1。这里为什么改变list的大小就能实现动态增加呢？因为 el-form-item 遍历的是 list,list 中的每一项都是一个 el-form-item
      // 也就是说因为刚开始 list:[{"oneId":''}] 中,只有一个对象,所以才会只出现一个 el-form-item
      // 不信可以自己在初始化时 list 中多加入几个对象进行尝试(一定要理解，这里 list 集合的大小与 el-form-item 之间的关系)
      // 2、第二个问题:el-form-item 是动态增加了,但是如果 el-select 那里写的是 v-model="oneId" 呢？会发生什么？
      // 结果你会发现,只要增加一项 el-form-item ,每一项绑定的值都是你所选中的那一个值.
      // 为什么呢？因为每一项的 el-option的 :value 值都绑定在 el-select 的 v-model 上,
      // 但这是一个全局唯一值,当下一个 el-form-item 产生后,它里面的 el-select 中绑定的 v-model 还是那个 oneId 的值,因此才会出现这样的问题.
      // 好了,我们既然找到了原因,那就要来解决一下了,怎么解决呢？
      // 很简单:因为我前面说了,每一个 list 的遍历对象,都是一项 el-form-item,即 el-form-item 项数是和 list 的下标(里面存的对象的索引下标)相关联的,
      // 而这个下标,在每一个 el-form-item 中肯定是不一样的,因此我们只需要将 oneId 与这个 下标(即此处的 index) 发生关系即可,
      // 因此我们这里将 oneId 声明为了一个数组,当你每选中一个 option 时,都将这个 option 的value放入 oneId[当前el-form-item项数下标] 数组中
      this.list.push({"oneId": ''});
    },
    // 删除时,我们带两个参数,这个 it 可用可不用,因为我当时只是想看到删除的这个对象的信息,故而带上了;
    // index 是 list 中该对象对应的下标,也是 el-form-item 的项数
    removeItem(it, index){
      // 根据这个 index 下标删除 list 中 的该对象
      if(index != 0){
        this.list.splice(index, 1);
      }
    },

    changeSelect(event){ // 当每选一个 option 时,我们需要将这个 选中的 oneId 放入 对应的 list 中即可,最后都选中完后,我们只要获取这个 list,即可拿到所有的数据
      console.log('000===changeSelect event===', event)
      this.selectedActivityIdList = event
      console.log('000===changeSelect this.selectedActivityIdList===', this.selectedActivityIdList)
    },

    async save() {
      this.$refs.form.validate(async (valid) => {
            if (valid) {
              // this.list = JSON.parse(JSON.stringify(this.list))
              console.log("======save() this.selectedActivityIdList======", this.selectedActivityIdList)
              this.$set(this.form, 'selectedActivityIdList', this.selectedActivityIdList)
              console.log("======save() this.form======", this.form)

              let res = await insertOrUpdateProjectApi(this.form);
              // let res = await this.$debounce( insertOrUpdateProjectApi(this.form), 3000 )()

              this.isReadOnly = true

              if (res.code === 200 ) {
                this.$message.success("保存成功")
                this.dialogFormVisible = false
                this.load()
              } else {
                this.$message.error("保存失败")
              }
            }
        })
      },


    handleAdd() {
      // this.$refs.form.clearValidate(props = [this.rules.name,this.rules.startTime,this.rules.endTime,this.rules.selectedActivityIdList]);
      // this.$refs.form.clearValidate(props = [this.rules]);

      this.$set(this.rules,
        'name',
        [
          { required: true,
            message: '请填写项目名称',
            trigger:['blur']
          }
        ]
      );
      this.$set(this.rules,
        'startTime',
        [
          { required: true,
            message: '请填写项目启动时间',
            trigger:['blur']
          }
        ]
      );
      this.$set(this.rules,
        'endTime',
        [
          { required: true,
            message: '请填写项目结束时间',
            trigger:['blur']
          }
        ]
      );
      // this.$set(this.rules,
      //   'selectedActivityIdList',
      //   [
      //     { required: true,
      //       message: '请选择项目关联活动',
      //       trigger:['blur']
      //     }
      //   ]
      // );

      // this.$resetForm('form',this.form)

      // name: [
      //   {
      //     trigger: "blur",
      //     message: "请填写项目名称",
      //     required: true,
      //   },
      // ],
      //   startTime: [
      //   {
      //     trigger: "blur",
      //     message: "请填写项目启动时间",
      //     required: true ,
      //   },
      // ],
      //   endTime: [
      //   {
      //     trigger: "blur",
      //     message: "请填写项目结束时间",
      //     required: true,
      //   },
      // ],
      //   selectedActivityIdList: [
      //   {
      //     trigger: "blur",
      //     message: "请选择项目关联活动",
      //     required: true,
      //   },
      // ],

      // this.$set(this.rules,'name');
      // this.$set(this.rules,'startTime');
      // this.$set(this.rules,'endTime');

      this.isReadOnly = false
      this.isReadOnlyForSelect = false

      this.getAllUnselectedActivityVoList()

      //有未关联的活动可以关联项目
      if (!this.allUnselectedActivityVoList || this.allUnselectedActivityVoList.length < 1){
        setTimeout(() => {
          this.$message.error("暂时没有未关联的活动可以关联项目")
        }, 200)
      }
      // this.readOnly = false
      this.selectVisible = false
      this.dialogFormVisible = true
      this.form = {}
      this.selectedActivityIdList = []
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
      },

    async handleEdit(row) {
      this.$delete(this.rules,'name');
      this.$delete(this.rules,'startTime');
      this.$delete(this.rules,'endTime');
      // this.$delete(this.rules,'selectedActivityIdList');

      console.log('000===row===', row)

      this.isReadOnly = false
      // this.selectVisible = true
      this.isReadOnlyForSelect = true

      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true

      console.log('000=== row.projectId ===', row.projectId)
      // let res = await getSelectedActivityIdListByProjectIdApi(row.projectId);
      let res = await getSelectedActivityListByProjectIdApi(row.projectId);
      if (res.code === 200 ) {
        console.log('123123 ===res.data===', res.data)

        this.selectedActivityList = res.data
      }

     this.$nextTick(() => {
       if(this.$refs.img) {
         this.$refs.img.clearFiles();
       }
       if(this.$refs.file) {
        this.$refs.file.clearFiles();
       }
     })
      this.$forceUpdate()
    },

    async getSelectedActivityIdListByProjectId(){
      await getSelectedActivityIdListByProjectIdApi(row.projectId);
    },

    //删除按钮
    async deleteBtn(row) {
      //信息确认提示
      let confirm = await this.$myconfirm('确定删除该数据吗?')
      console.log(confirm)
      if(confirm){
        let res = await deleteProjectApi({id:row.id})
        if(res && res.code == 200){
          //信息提示
          this.$message.success(res.msg)
          //刷新表格
          this.getList();
        }
      }
    },

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
      this.request.post("/project/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },

    //重置按钮
    reset(){
      this.params.name = '';
      this.load();
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
    // addressChange(arr) {
    //   // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
    //   console.log("111===arr===", arr);
    //   console.log("222===codeToText[arr[0]]===", codeToText[arr[0]] );
    //   console.log("333===codeToText[11]===", codeToText[11] );
    //   console.log("444===", codeToText[arr[0]], codeToText[arr[1]], codeToText[arr[2]]);
    //   var areaCode = arr[2];
    //   console.log("888===areaCode",  areaCode);
    // },

  }
}
</script>


<style scoped>
.headerBg {
  background: #eee!important;
}

/deep/ .el-dialog {
  height: 70vh;
  overflow: auto;
}

/*.el-option {*/
/*  height: 50px;*/
/*}*/

/*.select_item {*/
/*  height: 20px;*/
/*  line-height: 20px;*/
/*  font-size: 12px;*/
/*}*/

/*.el-select-dropdown {*/
/*  max-height: 150px;*/
/*  overflow-y: auto;*/
/*}*/

/*.el-select-dropdown {*/
/*  max-height: 50px;*/
/*  overflow-y: scroll;*/
/*}*/


/*.el-select-dropdown__wrap {*/
/*  max-height: 150px;*/
/*}*/

/*.dialog-footer {*/
/*  !*margin-top: 20px;*!*/
/*  !*padding-bottom: 20px;*!*/
/*  !*padding-top: 20px;*!*/
/*  !*margin-top: 20px;*!*/
/*  !*margin-bottom: 10px;*!*/
/*  !*bottom: 10px;*!*/
/*  background-color: red;*/
/*}*/



</style>

