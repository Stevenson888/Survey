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
        <el-input v-model="parms.username"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="resetBtn" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 用户表格
    :data= 是表格的数据

    el-table-column 中的  prop需要跟返回的字段对应
    label 字段的名称
     -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="id" label="用户Id"></el-table-column>
      <el-table-column prop="username" label="昵称"></el-table-column>
      <el-table-column prop="realName" label="真实姓名"></el-table-column>
      <el-table-column prop="roleFlag" label="角色标签"></el-table-column>
      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>
      <el-table-column prop="provinceName" label="省份名称"></el-table-column>
      <el-table-column prop="cityCode" label="城市编号"></el-table-column>
      <el-table-column prop="cityName" label="城市名称"></el-table-column>
      <el-table-column prop="districtCode" label="区域编号"></el-table-column>
      <el-table-column prop="districtName" label="区域名称"></el-table-column>
      <el-table-column prop="gender" label="性别"></el-table-column>
<!--      <el-table-column prop="mobile" label="电话"></el-table-column>-->
      <el-table-column prop="mobile" label="电话">
        <template slot-scope="scope">
          {{scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/,"$1****$3"):""}}
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="status" label="用户状态"></el-table-column>
      <el-table-column align="center" width="180" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
            >编辑</el-button
          >
          <el-button
            icon="el-icon-delete"
            type="danger"
            size="small"
            @click="deleteBtn(scope.row)"
            >删除</el-button
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
      :current-page.sync="parms.currentPage"
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
      :height="addDialog.height"
      :width="addDialog.width"
      :visible="addDialog.visible"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <template slot="content">
        <el-form
          :model="addModel"
          ref="addForm"
          :rules="rules"
          label-width="80px"
          :inline="true"
          size="small"
        >
<!--          <el-form-item prop="id" label="id">-->
<!--            <el-input v-model="addModel.id"></el-input>-->
<!--          </el-form-item>-->
          <el-form-item prop="username" label="昵称">
            <el-input v-model="addModel.username"></el-input>
          </el-form-item>
          <el-form-item prop="realName" label="实名">
            <el-input v-model="addModel.realName"></el-input>
          </el-form-item>
<!--          <el-form-item prop="roleFlag" label="角色标签">-->
<!--            <el-input v-model="addModel.roleFlag"></el-input>-->
<!--          </el-form-item>-->
          <el-form-item label="角色标签">
            <el-select clearable v-model="addModel.roleFlag" placeholder="请选择角色" style="width: 100%">
              <el-option v-for="item in roles" :key="item.name" :label="item.description" :value="item.roleFlag"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="status" label="省份编号">
            <el-input v-model="addModel.provinceCode"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="省份名称">
            <el-input v-model="addModel.provinceName"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="城市编号">
            <el-input v-model="addModel.cityCode"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="城市名称">
            <el-input v-model="addModel.cityName"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="区域编号">
            <el-input v-model="addModel.districtCode"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="区域名称">
            <el-input v-model="addModel.districtName"></el-input>
          </el-form-item>
          <el-form-item prop="gender" label="性别">
            <el-input v-model="addModel.gender"></el-input>
          </el-form-item>
          <el-form-item prop="mobile" label="电话">
            <el-input v-model="addModel.mobile"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="addModel.email"></el-input>
          </el-form-item>
          <el-form-item prop="address" label="地址">
            <el-input v-model="addModel.address"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="用户状态">
            <el-input v-model="addModel.status"></el-input>
          </el-form-item>
        </el-form>
      </template>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from "@/components/system/SysDialog.vue";
import { addUserApi, getUserListApi,editUserApi ,deleteUserApi} from "@/api/user";
import {getRoleListPageApi} from "@/api/role";
export default {
  name: "Users",
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
      roles: [],

      //新增表单的验证规则
      rules: {
        // userId: [
        //   {
        //     trigger: "change",
        //     message: "请填写用户ID",
        //     required: true,
        //   },
        // ],
        username: [
          {
            trigger: "change",
            message: "请填写账户",
            required: true,
          },
        ],
        realName: [
          {
            trigger: "change",
            message: "请填写姓名",
            required: true,
          },
        ],
        roleFlag: [
          {
            trigger: "change",
            message: "请填写角色标签",
            required: true,
          },
        ],
        provinceCode: [
          {
            trigger: "change",
            message: "请填写省份编号",
            required: true,
          },
        ],
        provinceName: [
          {
            trigger: "change",
            message: "请填写省份名称",
            required: true,
          },
        ],
        cityCode: [
          {
            trigger: "change",
            message: "请填写城市编号",
            required: true,
          },
        ],
        cityName: [
          {
            trigger: "change",
            message: "请填写城市名称",
            required: true,
          },
        ],
        districtCode: [
          {
            trigger: "change",
            message: "请填写区域编号",
            required: true,
          },
        ],
        districtName: [
          {
            trigger: "change",
            message: "请填写区域名称",
            required: true,
          },
        ],
      },

      //新增表单绑定的数据域
      addModel: {
        editType: "", //0：新增 1：编辑

        id: "",
        userId: this.userId,
        username: "",
        password: "",
        realName: "",
        roleFlag: "",
        provinceCode: "",
        provinceName: "",
        cityCode: "",
        cityName: "",
        districtCode: "",
        districtName: "",
        gender: "",
        mobile: "",
        email: "",
        address: "",
        status: "",

        // lastLoginTime: "",
        // createTime: "",
        // updateTime: "",

      },
      //弹框属性
      addDialog: {
        title: "",
        height: 250,
        width: 1200,
        visible: false,
      },
      parms: {
        username: "",
        currentPage: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },
      parms2: {
        name: "",
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    };
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.getList();
    this.getRoles();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  methods: {
    //搜索按钮
    searchBtn(){
      this.parms.currentPage = 1;
      this.getList();
    },
    //重置按钮
    resetBtn(){
      this.parms.username = '';
      this.getList();
    },
    //页数改变时触发
    currentChange(val) {
      console.log(val);
      this.parms.currentPage = val;
      //重新获取列表
      this.getList();
    },
    //页容量改变时触发
    sizeChange(val) {
      console.log(val);
      this.parms.pageSize = val;
      this.parms.currentPage = 1;
      this.getList();
    },
    //删除按钮
    async deleteBtn(row) {
      console.log(row);
      //信息确认提示
      let confirm = await this.$myconfirm('确定删除该数据吗?')
      console.log(confirm)
      if(confirm){
        let res = await deleteUserApi({id:row.id})
        if(res && res.code == 200){
          //信息提示
          this.$message.success(res.msg)
          //刷新表格
          this.getList();
        }
      }
    },
    //编辑按钮
    editBtn(row) {
      console.log(row);
      //表单的清空
      this.$resetForm('addForm',this.addModel)
      //设置弹框属性
      this.addDialog.title = '编辑用户'
      this.addDialog.visible = true;
      //设置要编辑的数据回显
      //把当前要编辑的数据设置到表单绑定的数据域
      this.$objCoppy(row,this.addModel)
      console.log(this.addModel)
      //设置为编辑
      this.addModel.editType = '1'; //1:标识编辑
    },
    //获取表格列表
    async getList() {
      let res = await getUserListApi(this.parms);
      console.log(res);
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.tableList = res.data.records;
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
    addBtn() {
      //表单的清空
      this.$resetForm('addForm',this.addModel)
      //设置是新增还是编辑
      this.addModel.editType = "0";
      this.addDialog.title = "新增用户";
      this.addDialog.visible = true;
    },
    //确认弹框
    onConfirm() {
      //表单验证
      this.$refs.addForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          //判断是新增还是编辑
          if (this.addModel.editType == "0") {

            //新增
            res = await addUserApi(this.addModel);
          }else{
            res = await editUserApi(this.addModel)
          }
          console.log(res);
          if (res && res.code == 200) {
            //信息提示
            this.$message.success(res.msg)
            //刷新列表
            this.getList();
            this.addDialog.visible = false;
          }
        }
      });
    },
    //关闭弹框
    onClose() {
      this.addDialog.visible = false;
    },
  },
};
</script>

<style lang="scss" scoped></style>
