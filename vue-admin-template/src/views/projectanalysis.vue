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
<!--        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>-->
      </el-form-item>
    </el-form>


    <!--    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" style="width: 100%" @selection-change="handleSelectionChange">-->
    <el-table :data="tableData" :height="tableHeight"  border stripe :header-cell-class-name="'headerBg'" >
      <!--      <el-table-column type="selection" width="55"></el-table-column>-->
      <!--      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>-->
<!--      <el-table-column prop="projectId" label="项目id(FT)" width=180px></el-table-column>-->
      <el-table-column prop="code" label="项目编号" width=120px></el-table-column>
      <el-table-column prop="name" label="项目名称" width=300px></el-table-column>
      <el-table-column prop="createTime" label="项目创建时间" width=200px></el-table-column>
<!--      <el-table-column prop="startTime" label="项目启动时间" width=100px></el-table-column>-->
      <el-table-column prop="endTime" label="项目结束时间" width=200px></el-table-column>

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

      <!-- 数组元素的值动态展示到table的header中-->
      <!--      <el-table-column v-for='(item,i) in dataColum' :key="i" :label="item" show-overflow-tooltip>-->
      <!--        <template slot-scope="scope">-->
      <!--          {{scope.row[i]}}-->
      <!--        </template>-->
      <!--      </el-table-column>-->

      <el-table-column prop="selectedProjectActivityListSize" label="项目关联问卷模板数量" width=100px></el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">

<!--         action="http://localhost:8089/api/file/upload" -->
<!--         action="http://43.140.195.12:8089/api/file/upload" -->
<!--          action="https://adminapi.xiangwenli.com/api/file/upload"-->
          <el-upload
              action="http://localhost:8089/api/file/upload"
              accept=".png,.jpg,.jpeg,.doc,.docx,.txt,.xls,.xlsx"
              :show-file-list="false"
              :on-success="handleSuccess"
              :data="{projectId: scope.row.projectId}"
              :headers="headers"
              style="display: inline-block; padding-right: 10px;"
          >
            <el-button type="success" icon="el-icon-top" size="small" >上传报告 </el-button>
          </el-upload>

<!--          <el-upload-->
<!--            ref="upload"-->
<!--            accept=".png,.jpg,.jpeg,.doc,.docx,.txt,.xls,.xlsx"-->
<!--            action="http://local host:8089/api/file/upload"-->
<!--            multiple-->
<!--            :limit="5"-->
<!--            :headers="headers"-->
<!--            :data="{projectId: scope.row.projectId}"-->
<!--            :auto-upload="false"-->
<!--            :file-list="fileList"-->
<!--            :on-change="handleChange"-->
<!--            :on-remove="removeFile"-->
<!--            :on-exceed="limitCheck"-->
<!--            style="display: inline-block; padding-right: 10px;"-->
<!--          >-->
<!--            <el-button  type="danger" icon="el-icon-upload2" size="small" >上传报告</el-button>-->
<!--            <div slot="tip" class="el-upload__tip">-->
<!--              <p>只支持.png / .jpg / .jpeg / .doc / .docx / .txt / .xls / .xlsx文件</p>-->
<!--              <p>最多上传5个文件</p>-->
<!--            </div>-->
<!--          </el-upload>-->

<!--          <el-upload-->
<!--            action="http://local host:8089/api/file/upload"-->
<!--            :show-file-list="false"-->
<!--            :data="{projectId: scope.row.projectId}"-->
<!--            :headers="headers"-->
<!--            :on-success="handleFileUploadSuccess"-->
<!--            style="display: inline-block; padding-right: 10px;"-->
<!--          >-->
<!--            <el-button  icon="el-icon-document" type="primary" size="small" @click="checkFiles(scope.row)">查看报告</el-button>-->
<!--          </el-upload>-->

          <el-button  icon="el-icon-bottom" type="primary" size="small"
                      @click="openFilesDialog(scope.row)">查看/下载报告</el-button>
<!--          <el-button icon="el-icon-download" type="success" size="small"  @click="downloadFiles(scope.row)">下载报告</el-button>-->
        </template>
      </el-table-column>



<!--      <el-form-item label="项目报告">-->
<!--        <template>-->
<!--          <div style="display:flex">-->
<!--            <div style="margin: 30px;">-->
<!--              系统操作手册-->
<!--            </div>-->
<!--            <div class="uploads">-->
<!--              <el-upload ref="upload"-->
<!--                         :action="upload.url"-->
<!--                         :name="upload.name"-->
<!--                         :data="uploadData"-->
<!--                         :limit="1"-->
<!--                         :on-preview="handlePreview"-->
<!--                         :on-remove="handleRemove"-->
<!--                         :on-success="handleSuccess"-->
<!--                         :on-error="handleError"-->
<!--                         :before-upload="beforeUpload"-->
<!--                         :on-exceed="handleExceed"-->
<!--                         :on-change="handleFileChange"-->
<!--                         :auto-upload="true"-->
<!--                         :file-list="fileList"-->
<!--                         style="margin: 20px;">-->
<!--                <el-button plain  class="view-option"> 上传文件 </el-button>-->
<!--                <div slot="tip"  class="el-upload__tip">-->
<!--                  温馨提示：操作手册用于供企业用户阅读并了解系统如何操作使用，支持文件格式：doc .docx .pdf，最多1个文件-->
<!--                </div>-->
<!--              </el-upload>-->
<!--            </div>-->
<!--          </div>-->
<!--        </template>-->
<!--      </el-form-item>-->



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

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal="false" style="font-size:1.3em; font-weight:bolder;">
      <el-form label-width="150px" size="small" style="width: 100%; margin: 0 auto; ">
        <!--        <el-form-item label="项目id">-->
        <!--          <el-input v-model="form.projectId" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="项目编号">-->
        <!--          <el-input v-model="form.code" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item label="项目名称：" style="font-weight: bolder; ">
          {{form.name}}
        </el-form-item>
<!--        <el-form-item label="项目名称">-->
<!--          <el-input v-model="form.name" autocomplete="off" :disabled=true></el-input>-->
<!--        </el-form-item>-->
        <!--        <el-form-item label="项目简介">-->
        <!--          <el-input v-model="form.info" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
<!--        <el-form-item label="项目描述">-->
<!--          <el-input v-model="form.content" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <!--        <el-form-item label="物料种类">-->
        <!--          <el-input v-model="form.productCategory" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="项目创建人id">-->
        <!--          <el-input v-model="form.createMuid" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="项目修改人id">-->
        <!--          <el-input v-model="form.updateMuid" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->
<!--        <el-form-item label="项目启动时间">-->
<!--          <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="项目结束时间">-->
<!--          <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->


        <!-- 省市区三级联动下拉框 element-china-area-data  -->
        <!--        <el-form-item label="项目覆盖区域">-->
        <!--          <el-cascader-->
        <!--            size="large"-->
        <!--            :options="options"-->
        <!--            v-model="selectedOptions"-->
        <!--            @change="addressChange">-->
        <!--          </el-cascader>-->
        <!--        </el-form-item>-->

<!--        <el-form-item label="项目关联活动" >-->
<!--          <el-select v-model="selectedActivityIdList" :disabled="readOnly" multiple placeholder="请选择" @change="changeSelect($event)" style="width: 600px">-->
<!--            &lt;!&ndash;            {{selectedActivityIdList}} {{allActivityVoList}} {{allSelectedActivityVoList}}  &ndash;&gt;-->
<!--            <el-option-->
<!--              v-for="item in allSelectedActivityVoList"-->
<!--              :key="item.id"-->
<!--              :label="item.title"-->
<!--              :value="item.id">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->

        <el-form-item label="项目报告：">
<!--          <template slot-scope="scope">-->
<!--            <div>{{form.fileList.length}}</div>-->

<!--            <el-row v-for="(item,index) in form.fileList" >-->
<!--              <img :src="item.url" style="width:100px; height:100px;">-->
<!--            </el-row>-->

            <!-- 附件查看及下载 -->
              <div v-if="fileList==null || fileList.length=='0'">无上传报告</div>
              <div v-else v-for="(item,index) in form.fileList" :key="item.id" >
                <el-row style="padding-top: 10px">
                    <el-col :span="15" style="font-size:1.2em; font-weight:bolder; " class="t-ellipsis">{{item.name}}</el-col>
<!--                    <el-col :span="3">-->
<!--                              <el-button icon="el-icon-view" type="success" size="small" style="padding-left: 20px" round @click="downloadFile(item)">预览</el-button>-->
<!--                    </el-col>-->
                    <el-col :span="3">
                              <el-button icon="el-icon-bottom" type="primary" size="small" style="" round @click="downloadFile(item)">下载</el-button>
                    </el-col>
                    <el-col :span="3">
                              <el-button icon="el-icon-delete" type="danger" size="small" style="" round @click="deleteFile(item)">删除</el-button>
                    </el-col>
                </el-row>
              </div>



<!--                  <ul>-->
<!--                      <li>-->
<!--                          <span style="font-size:1.3em; font-weight:bolder; ">{{item.name}}-->

<!--&lt;!&ndash;                            <span>&ndash;&gt;-->
<!--&lt;!&ndash;                                <el-upload&ndash;&gt;-->
<!--&lt;!&ndash;                                   action="http://local host:8089/api/file/upload"&ndash;&gt;-->
<!--&lt;!&ndash;                                   accept=".png,.jpg,.jpeg,.doc,.docx,.txt,.xls,.xlsx"&ndash;&gt;-->
<!--&lt;!&ndash;                                   :on-preview="handlePreview"&ndash;&gt;-->
<!--&lt;!&ndash;                                   :data="{projectId: scope.row.projectId}"&ndash;&gt;-->
<!--&lt;!&ndash;                                   :headers="headers"&ndash;&gt;-->
<!--&lt;!&ndash;                                   style="display: inline-block; padding-right: 10px;"&ndash;&gt;-->
<!--&lt;!&ndash;                                 >&ndash;&gt;-->
<!--&lt;!&ndash;                                  <el-button type="danger" icon="el-icon-upload2" size="small" >上传报告 </el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                                </el-upload>&ndash;&gt;-->
<!--&lt;!&ndash;                            </span>&ndash;&gt;-->



<!--&lt;!&ndash;                              <a :href="item.url" :download="item.name" style="padding-left:30px; color: rgba(60,94,176,0.84)" @click="previewFile(item)">预览</a>&ndash;&gt;-->
<!--&lt;!&ndash;                              <a :href="item.url" :download="item.name" style="padding-left:10px; color: rgba(60,94,176,0.84)" @click="downloadFile(item)" >下载</a>&ndash;&gt;-->
<!--                              <el-button icon="el-icon-view" type="success" size="small" style="margin-left:40px;" round @click="downloadFile(item)">预览</el-button>-->
<!--                              <el-button icon="el-icon-bottom" type="primary" size="small" style="" round @click="downloadFile(item)">下载</el-button>-->
<!--                              <el-button icon="el-icon-delete" type="danger" size="small" style="" round @click="deleteFile(item)">删除</el-button>-->

<!--                          </span>-->
<!--                      </li>-->
<!--                  </ul>-->




<!--    :href="item.url"-->
<!--    :download="item.name"-->
<!--    @click="downloadFile(item.url,item.name, item.id, item.type)"-->

<!--          </template>-->
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">

<!--        <el-upload-->
<!--          action="http://local host:8089/api/file/upload"-->
<!--          accept=".png,.jpg,.jpeg,.doc,.docx,.txt,.xls,.xlsx"-->
<!--          :show-file-list="true"-->
<!--          :on-preview="handlePreview"-->
<!--          :on-success="handleSuccess"-->
<!--          :file-list="this.fileList"-->
<!--          :headers="headers"-->
<!--          style="display: inline-block; padding-right: 10px; float:left; "-->
<!--        >-->
<!--          <el-button type="danger" icon="el-icon-upload2"  >上传报告 </el-button>-->
<!--        </el-upload>-->

        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>

import {
  getProjectApi,
  getProjectPageApi,
  getProjectListApi,
  editProjectApi,
  deleteProjectApi,
  getSelectedActivityListByProjectIdApi,
  insertOrUpdateProjectApi,
  getSelectedActivityIdListByProjectIdApi,
  getFileListByProjectIdPageApi,
  deleteProjectFileApi
} from "@/api/project";
import { getAllActivityListApi, getAllActivityVoListApi, getAllUnselectedActivityVoListApi} from "@/api/activity";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";
import {getToken} from "@/utils/auth";
import axios from 'axios'


export default {
  name: "Projectanalysis",
  // props: ['info'],

  watch: {
    // fileList: {
    //   handler: 'init',
    //   deep: true
    // }
    fileList:{	//监测data中的属性
      immediate:true, //初始化时让handler调用一下

      //handler什么时候调用？当isHot发生改变时。
      handler(newValue,oldValue){
        console.log('fileList 被修改了',newValue,oldValue)
      }
    }
  },

  data() {
    return {

      // upload: {
      //   url: '//file.geeker.com.cn/uploadOSS',
      //   key: '',
      //   // key: 'WN1EX00HLL8Y0O0K',  // 有效osskey
      //   path: 'cloud-platform',
      //   name: 'Filedata'
      // },
      // // 上传的文件列表
      // fileList: [],



      // headers:{},
      token:"",
      // uploadData:{projectId: ""},
      // uploadData:{projectId: scope.row.projectId},

      // 上传附件
      fileList: [],
      headers: {
        'Content-Type': 'multipart/form-data'
      },

      allActivityList: [],
      allActivityVoList: [],
      allSelectedActivityVoList: [],

      // selectedActivityList: [],
      selectedActivityIdList: [],

      readOnly:false,

      // id: "",
      projectId: "",

      // user:{},
      roles: [],

      tableData: [],
      tableHeight: 0,

      // form: {},
      form: {
        selectedActivityIdList:[],  // 这个里面是roleId 数组，后台获取到list后页面会直接显示
      },

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

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  computed: {
    // downloadFile: {{item.name}}+'.'+{{item.type}}
    // downloadFile(){
    //   return {{item.name}}+'.'+{{item.type}}
    // }


  //   uploadData() {
  //     return {
  //       body: JSON.stringify(this.form)
  //     }
  //   }
  },
  // mouted() {
  //   this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
  //   this.token = this.user.token
  //   this.headers = {"X-Token": this.token}
  //   console.log("===this.headers===", this.headers)
  // },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  created() {
    // config.headers['X-Token'] = getToken()
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.token = this.user.token
    this.headers = {"X-Token": this.token}
    console.log("===this.headers===", this.headers)

    this.load()
  },
  methods: {
    load() {
      this.getList();
      this.getAllActivityVoList();
      this.getAllUnselectedActivityVoList();
    },

    async getList() {
      // const rLoading = this.openLoading();
      let res = await getProjectPageApi(this.params);
      console.log(res);

      if (res && res.data && res.code == 200) {
        // rLoading.close();
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
        this.allSelectedActivityVoList = response.data;
        console.log("======this.allSelectedActivityVoList=====",  this.allSelectedActivityVoList);
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

      this.dialogFormVisible = false

      // // this.list = JSON.parse(JSON.stringify(this.list))
      // console.log("======save() this.selectedActivityIdList======", this.selectedActivityIdList)
      // this.$set(this.form, 'selectedActivityIdList', this.selectedActivityIdList)
      // console.log("======save() this.form======", this.form)
      //
      // let res = await insertOrUpdateProjectApi(this.form);
      // if (res.code === 200 ) {
      //   this.$message.success("保存成功")
      //   this.dialogFormVisible = false
      //   this.load()
      // } else {
      //   this.$message.error("保存失败")
      // }
    },

    handleAdd() {
      //有未关联的活动可以关联项目
      if (!this.allSelectedActivityVoList || this.allSelectedActivityVoList.length < 1){
        setTimeout(() => {
          this.$message.error("暂时没有未关联的活动可以关联项目")
        }, 200)
      }
      this.readOnly = false
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

    // async handleEdit(row) {
    //   this.readOnly = true
    //   this.form = JSON.parse(JSON.stringify(row))
    //   this.dialogFormVisible = true
    //   let res = await getSelectedActivityIdListByProjectIdApi(row.projectId);
    //   if (res.code === 200 ) {
    //     this.selectedActivityIdList = res.data
    //   }
    //   this.$nextTick(() => {
    //     if(this.$refs.img) {
    //       this.$refs.img.clearFiles();
    //     }
    //     if(this.$refs.file) {
    //       this.$refs.file.clearFiles();
    //     }
    //   })
    //   this.$forceUpdate()
    // },

    //删除按钮
    async deleteFile(file) {
      let projectId = file.projectId

      //信息确认提示
      let confirm = await this.$myconfirm('确定删除该数据吗?')
      console.log("file", file)
      if(confirm){
        let res = await deleteProjectFileApi({id:file.id})
        console.log("res", res)
        if(res && res.code == 200){
          //信息提示
          this.$message.success("删除成功")
          this.dialogFormVisible = false
          //刷新表格
          this.load();
          // this.$forceUpdate()
          // location.reload()
          // this.updateDialog()

          // this.dialogFormVisible = true


          // let res = await getFileListByProjectIdPageApi(projectId);
          // if (res.code === 200 ) {
          //   this.fileList = res.data
          //   console.log("999999 ===getFileListByProjectId===", this.fileList)
          // }

          // this.$nextTick(() => {
          //   // this.load();
          //   console.log("=== 000000 deleteFile === this.fileList", this.fileList)
          //   console.log("=== 000000 deleteFile === file.projectId", file.projectId)
          //   this.fileList = this.getFileListByProjectId(file.projectId)
          //   console.log("=== 111111 deleteFile === this.fileList", this.fileList)
          // })

          // this.$nextTick(() => {
          //   this.$forceUpdate()
          // })

        }
      }
    },

    //删除按钮
    async previewBtn(file) {
      // window.open(file.url)
      console.log("file", file)

      // window.open(window.origin + file.name,'_blank');

      // // let docUrl = 'https://aaaaaa.com/file/download?filename=file.obj_id'
      let url = encodeURIComponent(file.url)
      let officeUrl = 'http://view.officeapps.live.com/op/view.aspx?src='+url
      // // 在新窗口打开编码后 的链接
      // window.open(officeUrl,'_target')


      // // let docUrl = 'https://aaaaaa.com/file/download?filename=file.obj_id'
      // // let url = encodeURIComponent(file.url)
      // let officeUrl = 'https://view.xdocin.com/view?src='+file.url
      // // window.open("https://view.xdocin.com/view?src="
      // //   + encodeURIComponent("https://view.xdocin.com/demo/view.docx")
      // //   + "&watermark=" + encodeURIComponent("view.xdocin.com"));
      // // 在新窗口打开编码后 的链接
      // window.open(officeUrl,'_target')


      // // let result= "http://xxx.oss-cn-shanghai.aliyuncs.com/report/work.docx?OSSAccessKeyId=LTAI4GGvJTbm7fTKs&Expires=1636425239&Signature=c6xf8qQ5rV78Y%3D"
      // let url= 'http://view.officeapps.live.com/op/view.aspx?src='+ file.url


      window.open(url,'_target')

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
    // handleFileUploadSuccess(res) {
    //   this.form.file = res
    // },
    handleSuccess(response, file, fileList) {
      this.$message.success("上传成功")

      console.log('response 服务器返回的响应数据:', response);
      console.log('file 服务器返回的响应数据:', file);
      console.log('fileList 当前已上传的文件列表:', fileList);

      this.load()
      // this.form.file = res
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



    // 文件状态改变时的钩子
    handleChange(file, fileList) { // 文件数量改变
      this.fileList = fileList
      const isLt2M = (file.size / 1024 / 1024 < 2)
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
        this.fileList.pop()
      }
      return isLt2M
    },
    // 文件超出个数限制时的钩子
    limitCheck() {
      this.$message.warning('每次上传限制最多五个文件')
    },
    // 文件删除的钩子
    removeFile(file, fileList) {
      this.fileList = fileList
    },


    //按钮:查看报告
    async previewFiles(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      //open and preview Files
    },
    //按钮:下载报告
    async openFilesDialog(row) {
        this.readOnly = true
        this.form = JSON.parse(JSON.stringify(row))
        this.dialogFormVisible = true

        // let res = await getFileListByProjectIdPageApi(row.projectId);
        // if (res.code === 200 ) {
        //   this.fileList = res.data
        // }
        this.fileList = this.getFileListByProjectId(row.projectId)

        this.$nextTick(() => {
          if(this.$refs.img) {
            this.$refs.img.clearFiles();
          }
          if(this.$refs.file) {
            this.$refs.file.clearFiles();
          }
        })
        // this.$forceUpdate()
    },


    async getFileListByProjectId(projectId) {
      let res = await getFileListByProjectIdPageApi(projectId);
      if (res.code === 200 ) {
        this.fileList = res.data
        // console.log("000===getFileListByProjectId===", this.fileList)
        return this.fileList
      }
    },


      //信息确认提示
      // let confirm = await this.$myconfirm('确定删除该数据吗?')
      // console.log(confirm)
      // if(confirm){
      //   let res = await deleteProjectApi({id:row.id})
      //   if(res && res.code == 200){
      //     //信息提示
      //     this.$message.success(res.msg)
      //     //刷新表格
      //     this.getList();
      //   }
      // }
      // console.log("======save() this.form======", this.form)

      // let res = await getFileListByProjectIdPageApi(row.projectId);
      // if (res.code === 200 ) {
      //   // this.$message.success("保存成功")
      //   this.dialogFormVisible = true
      //   res.data
      //   this.load()
      // } else {
      //   this.$message.error("保存失败")
      // }


    // updateDialog() {
    //   // this.dialogContent = '<p>我是最新的内容！</p>'
    //   this.dialogFormVisible = false
    //   this.$nextTick(() => {
    //     this.dialogFormVisible = true
    //   })
    // },

    // handlePreview(file){
    //   console.log("===000=== handlePreview file", file)
    //   window.open(file.url);
    // },

    // previewFile(file){
    //   console.log("===000=== handlePreview file", file)
    //   window.open(file.url);
    // },

    // downloadFile(fileurl, filename, fileid, filetype){
    // downloadFile(file){
    //   console.log("===000=== downloadFile file", file)
    //   let link = document.createElement('a');
    //   link.style.display = 'none';
    //   link.href = file.url;
    //   // link.download = file.name;
    //   link.download = "888888";
    //   link.setAttribute('download', "888888");
    //   // link.href = baseUrl + '/xxx/xxx/xxx?flieId=' + flieId;
    //   document.body.appendChild(link);
    //   link.click();
    //
    //   //               :href="item.url"
    //   //                :download="item.name"
    //
    //   // item.url,
    //   // item.name,
    //   // item.id,
    //   // item.type
    //
    // },


    downloadFile(file) {
      var xml = new XMLHttpRequest();   //文件重命名: 跨域下,解决download属性失效
      xml.open('GET', file.url, true);
      xml.responseType = 'blob';
      xml.onload = function () {
        var url = window.URL.createObjectURL(xml.response);
        var a = document.createElement('a');
        a.href = url;
        a.download = file.name;
        a.click();
      };
      xml.send();
    },


    // 点击确定按钮 上传文件
    confirm() {
      var param = new FormData()
      this.fileList.forEach((val, index) => {
        param.append('file', val.raw)
      })
        param.append('projectId', scope.row.projectId)


      // 拿取其他的信息
      //   param.append('id', sessionStorage.getItem('id'))
      // ...

      this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
      this.token = this.user.token
      this.headers = {"X-Token": this.token}

      // axios('http://local host:8089/api/file/upload', {
      //   headers: {
      //     "X-Token": this.token,
      //     // 'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      //     'Content-Type': 'multipart/form-data'
      //   },
      //   method: 'post',
      //   data: param
      // }).then((res) => {
      //   if (res.data.code === 200) {
      //     this.$message.success('上传成功')
      //   } else {
      //     this.$message.error('上传失败')
      //   }
      // })

    }



    // // 处理上传成功之后操作
    // handleSuccess(resp, file, fileList) {
    //   this.fileList = fileList;//！！！！注意：这个地方要直接复制，不能拿file然后push到this.fileList里面去，push进去页面会跳动
    //   this.$api.saveOperationManual({ operationManual: resp.safeUrl }).then((res) => {
    //     console.log(res);
    //     if (res.code === 0) {
    //       this.fileList[0].id = res.data.id;
    //       const fileName = res.data.operationManual.split('/')[res.data.operationManual.split('/').length - 1];
    //       this.fileList[0].fileName = fileName;
    //       this.$notice.operateSuccess('上传成功');
    //     } else {
    //       this.$notice.operateError('上传失败', `${ res.message }`);
    //     }
    //   })
    //     .catch((error) => {
    //       this.$notice.operateError('上传失败', `${ error.data.message }`);
    //     });
    // },
    // // 处理上传失败之后操作
    // handleError() {
    //   this.$message.error('上传失败');
    // },
    // // /删除文件
    // handleRemove(file) {
    //   this.$api.deleteOperationManual(file.id).then((res) => {
    //     console.log(res);
    //     if (res.code === 0) {
    //       this.$notice.operateSuccess('删除成功');
    //       this.showFile = false;
    //       this.fileList = [];
    //       console.log(this.fileList);
    //     } else {
    //       this.$notice.operateError('删除失败', `${ res.message }`);
    //     }
    //   })
    //     .catch((error) => {
    //       this.$notice.operateError('删除失败', `${ error.message }`);
    //     });
    // },
    // // 下载文件
    // handlePreview(file) {
    //   // 创建a标签
    //   const link = document.createElement('a');
    //   // download属性
    //   link.setAttribute('download', file.name);
    //   // href链接
    //   link.setAttribute('href', file.operationManual);
    //   // 自执行点击事件
    //   link.click();
    //
    // },
    // // 处理超出文件选择范围
    // handleExceed() {
    //   this.$message.warning('当前限制上传 1 个文件');
    // },
    // // 查询文件
    // getFilesList() {
    //   this.$api.operationManual().then((res) => {
    //     console.log(res);
    //     if (res.code === 0 && res.data) {
    //       const fileName = res.data.operationManual.split('/')[res.data.operationManual.split('/').length - 1];
    //       this.fileList.push({ ...res.data,
    //         name: fileName });
    //     }
    //   })
    //     .catch((error) => {
    //       this.$notice.operateError('获取失败', `${ error.message }`);
    //     });
    // },


  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}

.t-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
