<template>
  <!-- el-main是一个容器，只需要把他当做正常的div使用就可以
:model 绑定表单的数据域
ref 是相当于div的id
:rules 表单的验证规则 - rules配合prop：用来校验表单this.$refs.editForm.validate
:inline 控制表单的展示方向
size 表单的大小
 -->
  <el-main>
    <el-form
      :model="parms"
      ref="searchForm"
      label-width="80px"
      :inline="true"
      size="small"
    >
      <el-form-item label="搜索昵称">
        <el-input v-model="parms.name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="resetBtn" icon="el-icon-close">重置</el-button>
<!--        <el-button type="primary" icon="el-icon-plus" @click="addBtn">新增</el-button>-->
      </el-form-item>
    </el-form>
    <!-- 用户表格
    :data= 是表格的数据

    el-table-column 中的  prop需要跟返回的字段对应
    label 字段的名称
     -->
    <el-table :data="tableList" :height="tableHeight" border stripe>
<!--      <el-table-column prop="id" label="用户Id"></el-table-column>-->
<!--      <el-table-column prop="userId" label="用户Id(FT)"></el-table-column>-->
<!--      <el-table-column prop="username" label="昵称"></el-table-column>-->
      <el-table-column prop="nickname" label="微信昵称"></el-table-column>
<!--      <el-table-column prop="mobile" label="注册微信手机号"></el-table-column>-->
      <el-table-column prop="mobile" label="注册微信手机号">
        <template slot-scope="scope">
          {{scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/,"$1****$3"):""}}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="小程序注册日期"></el-table-column>
<!--      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>-->
      <el-table-column prop="provinceName" label="省份名称"></el-table-column>
<!--      <el-table-column prop="cityCode" label="城市编号"></el-table-column>-->
<!--      <el-table-column prop="cityName" label="城市名称"></el-table-column>-->
<!--      <el-table-column prop="districtCode" label="区域编号"></el-table-column>-->
      <el-table-column prop="districtName" label="区域名称"></el-table-column>
<!--      <el-table-column prop="gender" label="性别"></el-table-column>-->
      <!--      <el-table-column prop="mobile" label="电话"></el-table-column>-->

<!--      <el-table-column prop="store.name" label="所属门店名称"></el-table-column>-->
      <el-table-column
        label="所属门店"
        align="left"
        width="180px">
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.storeList" :key="index+'abc'" >
            <div v-if="scope.row.storeList.length>1">{{index+1}}.{{item.name}}</div>
            <div v-else>{{item.name}}</div>
          </div>
        </template>
      </el-table-column>

<!--      <el-table-column prop="store.licenseNo" label="关联门店专卖证号"></el-table-column>-->
      <el-table-column
        label="关联门店专卖证号"
        align="left"
        width="180px">
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.storeList" :key="index+'abc'" >
            <div v-if="scope.row.storeList.length>1">{{index+1}}.{{item.licenseNo}}</div>
            <div v-else>{{item.licenseNo}}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="answerAmount" label="完成问卷数量"></el-table-column>
      <el-table-column prop="" label="用户画像(预留)">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="primary"
            size="small"
            @click="getAvatar(scope.row)"
          >查看</el-button
          >
        </template>
      </el-table-column>

<!--      <el-table-column prop="email" label="邮箱"></el-table-column>-->
<!--      <el-table-column prop="address" label="地址"></el-table-column>-->
<!--      <el-table-column prop="status" label="用户状态"></el-table-column>-->
<!--      <el-table-column prop="createTime" label="注册时间"></el-table-column>-->

<!--      <el-table-column align="center" width="180" label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            icon="el-icon-edit"-->
<!--            type="primary"-->
<!--            size="small"-->
<!--            @click="editBtn(scope.row)"-->
<!--            >编辑</el-button-->
<!--          >-->
<!--          <el-button-->
<!--            icon="el-icon-delete"-->
<!--            type="danger"-->
<!--            size="small"-->
<!--            @click="deleteBtn(scope.row)"-->
<!--            >删除</el-button-->
<!--          >-->
<!--        </template>-->
<!--      </el-table-column>-->

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
      :page-sizes="[2, 10, 20, 40, 80, 100]"
      :page-size="parms.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="parms.total"
      background
    >
    </el-pagination>

    <!-- 新增弹框 -->
<!--    <sys-dialog-->
<!--      :title="addDialog.title"-->
<!--      :height="addDialog.height"-->
<!--      :width="addDialog.width"-->
<!--      :visible="addDialog.visible"-->
<!--      @onClose="onClose"-->
<!--      @onConfirm="onConfirm"-->
<!--    >-->
<!--      <template slot="content">-->
<!--        <el-form-->
<!--          :model="addModel"-->
<!--          ref="addForm"-->
<!--          :rules="rules"-->
<!--          label-width="80px"-->
<!--          :inline="true"-->
<!--          size="small"-->
<!--        >-->
<!--&lt;!&ndash;          <el-form-item prop="id" label="id">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-input v-model="addModel.id"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-form-item>&ndash;&gt;-->
<!--          <el-form-item prop="username" label="昵称">-->
<!--            <el-input v-model="addModel.username"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="realName" label="实名">-->
<!--            <el-input v-model="addModel.realName"></el-input>-->
<!--          </el-form-item>-->
<!--&lt;!&ndash;          <el-form-item prop="roleFlag" label="角色标签">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-input v-model="addModel.roleFlag"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-form-item>&ndash;&gt;-->
<!--          <el-form-item label="角色标签">-->
<!--            <el-select disabled clearable v-model="addModel.roleFlag" placeholder="请选择角色" style="width: 100%">-->
<!--              <el-option v-for="item in roles" :key="item.name" :label="item.description" :value="item.roleFlag"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="省份编号">-->
<!--            <el-input v-model="addModel.provinceCode"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="省份名称">-->
<!--            <el-input v-model="addModel.provinceName"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="城市编号">-->
<!--            <el-input v-model="addModel.cityCode"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="城市名称">-->
<!--            <el-input v-model="addModel.cityName"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="区域编号">-->
<!--            <el-input v-model="addModel.districtCode"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="区域名称">-->
<!--            <el-input v-model="addModel.districtName"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item prop="selectedAreaOptions" label="地区" > &lt;!&ndash;:props="props" 可多选&ndash;&gt;-->
<!--            <el-cascader-->
<!--              clearable-->
<!--              filterable-->
<!--              size="large"-->
<!--              :options="regionDataOptions"-->
<!--              v-model="addModel.selectedAreaOptions"-->
<!--              @change="addressChange">-->
<!--            </el-cascader>-->
<!--            &lt;!&ndash;            <el-cascader&ndash;&gt;-->
<!--            &lt;!&ndash;              :props="props"&ndash;&gt;-->
<!--            &lt;!&ndash;              clearable&ndash;&gt;-->
<!--            &lt;!&ndash;              filterable&ndash;&gt;-->
<!--            &lt;!&ndash;              size="large"&ndash;&gt;-->
<!--            &lt;!&ndash;              :options="pcaTextArrOptions"&ndash;&gt;-->
<!--            &lt;!&ndash;              v-model="addModel.selectedOptions"&ndash;&gt;-->
<!--            &lt;!&ndash;              @change="addressChange">&ndash;&gt;-->
<!--            &lt;!&ndash;            </el-cascader>&ndash;&gt;-->
<!--          </el-form-item>-->

<!--          <el-form-item prop="gender" label="性别">-->
<!--            <el-input v-model="addModel.gender"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="mobile" label="电话">-->
<!--            <el-input v-model="addModel.mobile"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="email" label="邮箱">-->
<!--            <el-input v-model="addModel.email"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="address" label="地址">-->
<!--            <el-input v-model="addModel.address"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="status" label="用户状态">-->
<!--            <el-input v-model="addModel.status"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--      </template>-->
<!--    </sys-dialog>-->

<!--    <sys-dialog-->
<!--      :title="avatarDialog.title"-->
<!--      :height="avatarDialog.height"-->
<!--      :width="avatarDialog.width"-->
<!--      :visible="avatarDialog.visible"-->
<!--      @onClose="onClose"-->
<!--    >-->
<!--      <template slot="content">-->
<!--        <div style="font-size:20px; text-align: center; margin:30px">敬请期待</div>-->
<!--      </template>-->
<!--    </sys-dialog>-->

  </el-main>
</template>

<script>
import SysDialog from "@/components/system/SysDialog.vue";
import {
  addUserApi,
  getUserListApi,
  editUserApi,
  deleteUserApi,
  getCustomerListByAdminApi,
  getCustomerListByPartnerApi,
  getPartnerListByAdminApi, getCustomersByDevApi
} from "@/api/user";
import {getRoleListPageApi} from "@/api/role";
import {getStoreListByAdminApi, getStoreListByPartnerApi} from "@/api/storeshop";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";

export default {
  name: "Consumerusers",
  //注册组件
  components: { SysDialog },
  //在页面需要展示的数据，都需要在data里面显示的定义
  data() {
    return {
      id: "",
      userId: "",

      //表格高度
      tableHeight: 0,
      //表格的数据
      tableList: [],

      // user:{},
      roles: [],

      //新增表单的验证规则
      // rules: {
      //   // userId: [
      //   //   {
      //   //     trigger: "change",
      //   //     message: "请填写用户ID",
      //   //     required: true,
      //   //   },
      //   // ],
      //   username: [
      //     {
      //       trigger: "change",
      //       message: "请填写账户",
      //       required: true,
      //     },
      //   ],
      //   realName: [
      //     {
      //       trigger: "change",
      //       message: "请填写姓名",
      //       required: true,
      //     },
      //   ],
      //   roleFlag: [
      //     {
      //       trigger: "change",
      //       message: "请填写角色标签",
      //       required: true,
      //     },
      //   ],
      //   provinceCode: [
      //     {
      //       trigger: "change",
      //       message: "请填写省份编号",
      //       required: true,
      //     },
      //   ],
      //   provinceName: [
      //     {
      //       trigger: "change",
      //       message: "请填写省份名称",
      //       required: true,
      //     },
      //   ],
      //   cityCode: [
      //     {
      //       trigger: "change",
      //       message: "请填写城市编号",
      //       required: true,
      //     },
      //   ],
      //   cityName: [
      //     {
      //       trigger: "change",
      //       message: "请填写城市名称",
      //       required: true,
      //     },
      //   ],
      //   districtCode: [
      //     {
      //       trigger: "change",
      //       message: "请填写区域编号",
      //       required: true,
      //     },
      //   ],
      //   districtName: [
      //     {
      //       trigger: "change",
      //       message: "请填写区域名称",
      //       required: true,
      //     },
      //   ],
      // },

      //新增表单绑定的数据域
      // addModel: {
      //   editType: "", //0：新增 1：编辑
      //
      //   id: "",
      //   userId: this.userId,
      //   username: "",
      //   password: "",
      //   realName: "",
      //   roleFlag: "",
      //   provinceCode: "",
      //   provinceName: "",
      //   cityCode: "",
      //   cityName: "",
      //   districtCode: "",
      //   districtName: "",
      //
      //   gender: "",
      //   mobile: "",
      //   email: "",
      //   address: "",
      //   status: "",
      //
      //   // lastLoginTime: "",
      //   // createTime: "",
      //   // updateTime: "",
      //
      // },

      // //弹框属性
      // addDialog: {
      //   title: "",
      //   height: 250,
      //   width: 1200,
      //   visible: false,
      // },

      // //弹框属性
      // avatarDialog: {
      //   title: "",
      //   height: 250,
      //   width: 1200,
      //   visible: false,
      // },
      parms: {
        name: "",
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },
      parms2: {
        name: "",   //role.name
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },

      regionDataOptions: regionData,
      pcaTextArrOptions: pcaTextArr,
      selectedAreaOptions: [],
      props: { multiple: true },

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

    };
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    this.load()
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },

  methods: {
    load(){
      this.getList();
      // this.getRoles();
    },

    //获取表格列表
    // async getList() {
    //   let res = await getUserListApi(this.parms);
    //   console.log(res);
    //   if (res && res.code == 200) {
    //     //给表格数据赋值
    //     this.tableList = res.data.records;
    //     //总条数
    //     this.parms.total = res.data.total;
    //   }
    // }, getCustomerListByPartnerApi

    async getList() {
      // const rLoading = this.openLoading();

      // let res = await getCustomerListByAdminApi(this.parms);

      console.log("888888 ===stores=== this.params ", this.parms)
      // console.log("888888 ===stores=== this.user ", this.user)
      // console.log("888888 ===stores=== this.user.roleFlag ", this.user.roleFlag)
      // console.log("888888 ===stores=== this.user.roleFlag==ROLE_ADMIN ", this.user.roleFlag == "ROLE_ADMIN")
      // console.log("888888 ===stores=== this.user.roleFlag==ROLE_PARTNER ", this.user.roleFlag == "ROLE_PARTNER")

      let res = null;
      if (this.user.roleFlag == "ROLE_DEV"){
        res = await getCustomersByDevApi(this.parms);
      } else if (this.user.roleFlag == "ROLE_ADMIN"){
        res = await getCustomerListByAdminApi(this.parms);
      } else if (this.user.roleFlag == "ROLE_PARTNER") {
        res = await getCustomerListByPartnerApi(this.parms);
      }

      console.log(res);
      if (res && res.data && res.code == 200) {
        // rLoading.close();

        // console.log("888888 ===stores=== res.data.records ", res.data.records)
        // console.log("888888 ===stores=== res.data ", res.data)

        //给表格数据赋值
        this.tableList = res.data.records;
        // this.tableList = res.data.list;
        //总条数
        this.parms.total = res.data.total;
      }
    },
    async getRoles() {
      let res = await getRoleListPageApi(this.parms2);
      console.log(res);
      if (res && res.data && res.code == 200) {
        this.roles = res.data.records
      }
    },
    //新增按钮
    // addBtn() {
    //   //表单的清空
    //   this.$resetForm('addForm',this.addModel)
    //   //设置是新增还是编辑
    //   this.addModel.editType = "0";
    //   this.addDialog.title = "新增用户";
    //   this.addDialog.visible = true;
    //   this.addModel.roleFlag = "ROLE_CUSTOMER"
    // },

    //确认弹框
    // onConfirm() {
    //   //表单验证
    //   this.$refs.addForm.validate(async (valid) => {
    //     if (valid) {
    //       let res = null;
    //       //判断是新增还是编辑
    //       if (this.addModel.editType == "0") {
    //
    //         //新增
    //         res = await addUserApi(this.addModel);
    //       }else{
    //         res = await editUserApi(this.addModel)
    //       }
    //       console.log(res);
    //       if (res && res.code == 200) {
    //         //信息提示
    //         this.$message.success(res.msg)
    //         //刷新列表
    //         this.load();
    //         this.addDialog.visible = false;
    //       }
    //     }
    //   });
    // },

    //编辑按钮
    // editBtn(row) {
    //   console.log(row);
    //   //表单的清空
    //   this.$resetForm('addForm',this.addModel)
    //   //设置弹框属性
    //   this.addDialog.title = '编辑用户'
    //   this.addDialog.visible = true;
    //   //设置要编辑的数据回显
    //   //把当前要编辑的数据设置到表单绑定的数据域
    //   this.$objCoppy(row,this.addModel)
    //
    //   let provinceCode = row.provinceCode.substr(0, 2)
    //   console.log("provinceCode",provinceCode)
    //   let cityCode = row.cityCode.substr(0, 4)
    //   console.log("cityCode",cityCode)
    //   let districtCode = row.districtCode
    //   console.log("districtCode",districtCode)
    //   // this.selectedAreaOptions = [provinceCode, cityCode, districtCode]
    //   this.addModel.selectedAreaOptions = [provinceCode, cityCode, districtCode]
    //   console.log("this.addModel.selectedAreaOptions",this.addModel.selectedAreaOptions)
    //
    //   console.log(this.addModel)
    //   //设置为编辑
    //   this.addModel.editType = '1'; //1:标识编辑
    // },

    //删除按钮
    // async deleteBtn(row) {
    //   console.log(row);
    //   //信息确认提示
    //   let confirm = await this.$myconfirm('确定删除该数据吗?')
    //   console.log(confirm)
    //   if(confirm){
    //     let res = await deleteUserApi({id:row.id})
    //     if(res && res.code == 200){
    //       //信息提示
    //       this.$message.success(res.msg)
    //       //刷新表格
    //       this.load();
    //     }
    //   }
    // },

    //编辑按钮
    getAvatar(row) {
      console.log(row);

      // //设置弹框属性
      // this.avatarDialog.title = '用户画像'
      // this.avatarDialog.visible = true;

      this.$message.success("敬请期待")
    },

    //关闭弹框
    onClose() {
      this.addDialog.visible = false;
    },

    //搜索按钮
    searchBtn(){
      this.parms.pageNum = 1;
      this.load();
    },
    //重置按钮
    resetBtn(){
      this.parms.name = '';
      this.load();
    },
    //页数改变时触发
    currentChange(pageNum) {
      console.log(pageNum);
      this.parms.pageNum = pageNum;
      //重新获取列表
      this.load();
    },
    //页容量改变时触发
    sizeChange(pageSize) {
      console.log(pageSize);
      this.parms.pageSize = pageSize;
      this.parms.pageNum = 1;
      this.load();
    },

    //省市区三级联动下拉框
    // addressChange(arr) {
    //   //arr[0]:11 arr[1]:1101 arr[2]:110101
    //   // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
    //   const provinceCode = arr[0]+'0000';
    //   const cityCode = arr[1]+'00';
    //   const districtCode = arr[2];
    //   const provinceName = codeToText[arr[0]];
    //   const cityName = codeToText[arr[1]];
    //   const districtName = codeToText[arr[2]];
    //
    //   console.log("provinceCode:", provinceCode);
    //   console.log("cityCode:", cityCode);
    //   console.log("districtCode:", districtCode);
    //   console.log("provinceName:", provinceName);
    //   console.log("cityName:", cityName);
    //   console.log("districtName:", districtName);
    //
    //   this.addModel.provinceCode = provinceCode
    //   this.addModel.provinceName = provinceName
    //   this.addModel.cityCode = cityCode
    //   this.addModel.cityName = cityName
    //   this.addModel.districtCode = districtCode
    //   this.addModel.districtName = districtName
    //
    // },

  },
};
</script>

<style lang="scss" scoped></style>
