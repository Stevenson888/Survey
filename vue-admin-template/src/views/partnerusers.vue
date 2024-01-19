<template>
  <!-- el-main是一个容器，只需要把他当做正常的div使用就可以
ref 是相当于div的id
:model 绑定表单的数据域
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
      <el-form-item label="搜索实名">
        <el-input v-model="parms.realName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="resetBtn" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn" v-if="user.roleFlag === 'ROLE_ADMIN'">新增</el-button>
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
      <el-table-column prop="username" label="登录用户名"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" width=100px></el-table-column>
<!--      <el-table-column prop="appUserId" label="appUserId(ForTesting)" width=100px></el-table-column>-->
<!--      <el-table-column prop="roleFlag" label="角色标签(FT)"></el-table-column>-->
<!--      <el-table-column prop="createUid" label="创建者(FT)"></el-table-column>-->
<!--      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>-->
      <el-table-column prop="provinceName" label="所在省/市"></el-table-column>
<!--      <el-table-column prop="cityCode" label="城市编号"></el-table-column>-->
<!--      <el-table-column prop="cityName" label="所在省/直辖市"></el-table-column>-->
<!--      <el-table-column prop="districtCode" label="区域编号"></el-table-column>-->
      <el-table-column prop="districtName" label="所在市/区"></el-table-column>
<!--      <el-table-column prop="gender" label="性别"></el-table-column>-->
<!--      <el-table-column prop="mobile" label="电话"></el-table-column>-->
      <el-table-column prop="mobile" label="微信关联电话">
        <template slot-scope="scope">
          {{scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/,"$1****$3"):""}}
        </template>
      </el-table-column>
<!--      <el-table-column prop="email" label="邮箱"></el-table-column>-->
<!--      <el-table-column prop="address" label="地址"></el-table-column>-->
<!--      <el-table-column prop="status" label="用户状态"></el-table-column>-->
<!--      <el-table-column prop="createTime" label="注册时间"></el-table-column>-->
      <el-table-column prop="appCreateTime" label="小程序注册时间"></el-table-column>
      <el-table-column prop="appNickname" label="小程序昵称"></el-table-column>
<!--      <el-table-column prop="storeNumber" label="管理门店" width=100px></el-table-column>-->

<!--        <template slot-scope="scope">-->
            <!--  /stores?name=xxx  -->
<!--            <router-link :to="{path:'/partnerusers',query: { userId: scope.row.userId }}">{{scope.row.storeNumber}}</router-link>-->
<!--        </template>-->

<!--      20231203改之前-->
<!--        <el-table-column prop="storeNumber" label="管理门店" v-if="user.roleFlag !== 'ROLE_ADMIN'">-->
<!--          <template slot-scope="scope" >-->
<!--            &lt;!&ndash;  /stores?realName=xxx  &ndash;&gt;-->
<!--&lt;!&ndash;            <router-link  :to="{path:'/stores',query: { realName: scope.row.realName, userId: scope.row.userId, roleFlag: scope.row.roleFlag }}">{{scope.row.storeNumber}}</router-link>&ndash;&gt;-->
<!--              <router-link  :to="{path:'/stores',query: { userId: scope.row.userId }}">{{scope.row.storeNumber}}</router-link>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      <el-table-column prop="storeNumber" label="管理门店" v-if="user.roleFlag === 'ROLE_ADMIN'"></el-table-column>-->
      <!--      20231203改之前-->

      <!--      20231203改之后-->
      <el-table-column prop="storeNumber" label="管理门店">
        <template slot-scope="scope" >
          <router-link  :to="{path:'/stores',query: { userId: scope.row.appUserId }}">{{scope.row.storeNumber}}</router-link>
        </template>
      </el-table-column>
      <!--      20231203改之后-->


<!--      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag === 'ROLE_ADMIN'">-->
<!--        <template slot-scope="scope" >-->
<!--          &lt;!&ndash;  /partnerusers?realName=xxx  &ndash;&gt;-->
<!--          <router-link :to="{path:'/partnerusers',query: { realName: scope.row.createUserName }}">{{scope.row.createUserName}}</router-link>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag !== 'ROLE_ADMIN'"></el-table-column>-->

      <el-table-column align="center" width="300" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
            v-if="user.roleFlag === 'ROLE_DEV' || user.roleFlag === 'ROLE_ADMIN'"
            >编辑</el-button
          >
<!--          <el-button-->
<!--            icon="el-icon-delete"-->
<!--            type="danger"-->
<!--            size="small"-->
<!--            @click="deleteBtn(scope.row)"-->
<!--            v-if="user.roleFlag === 'ROLE_ADMIN'"-->
<!--            >删除</el-button-->
<!--          >-->

          <el-button
            icon="el-icon-edit"
            type="danger"
            size="small"
            @click="changePswBtn(scope.row)"
          >修改密码</el-button
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
      :current-page="parms.pageNum"
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
          label-width="100px"
          :inline="true"
          size="small"
          style="padding:30px;"
        >
<!--          <el-form-item prop="id" label="id">-->
<!--            <el-input v-model="addModel.id"></el-input>-->
<!--          </el-form-item>-->
          <el-form-item  prop="realName" label="姓名">
            <el-input v-model="addModel.realName" :disabled="readOnly"></el-input>
          </el-form-item>
          <el-form-item prop="username" label="登录用户名" >
            <el-input v-model="addModel.username" :disabled="readOnly"></el-input>
          </el-form-item>

          <el-form-item prop="roleFlag" label="角色标签">
            <el-select disabled clearable v-model="addModel.roleFlag" placeholder="请选择角色" style="width: 200px">
              <el-option v-for="item in roles" :key="item.name" :label="item.description" :value="item.roleFlag"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="mobile" label="电话">
            <el-input v-model="addModel.mobile"></el-input>
          </el-form-item>
<!--          <el-form-item prop="username" label="登录密码">-->
<!--            <el-input v-model="addModel.password"></el-input>-->
<!--          </el-form-item>-->
          <el-form-item prop="gender" label="性别">
            <el-select v-model="addModel.gender" placeholder="请选择性别" style="width:200px">
              <el-option v-for="(gender,index) in genderOptions" :label="gender.name" :key="gender.id" :value="gender.id" ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="addModel.email"></el-input>
          </el-form-item>

          <el-form-item prop="password" label="密码" v-show="!readOnly">
            <el-input v-model="addModel.password"></el-input>
          </el-form-item>

          <el-form-item prop="address" label="地址">
            <el-input v-model="addModel.address"></el-input>
          </el-form-item>

          <el-form-item prop="selectedAreaOptions" label="地区" > <!--:props="props" 可多选-->
            <el-cascader
              clearable
              filterable
              size="large"
              :options="regionDataOptions"
              v-model="addModel.selectedAreaOptions"
              @change="addressChange">
            </el-cascader>
            <!--            <el-cascader-->
            <!--              :props="props"-->
            <!--              clearable-->
            <!--              filterable-->
            <!--              size="large"-->
            <!--              :options="pcaTextArrOptions"-->
            <!--              v-model="addModel.selectedOptions"-->
            <!--              @change="addressChange">-->
            <!--            </el-cascader>-->
          </el-form-item>
        </el-form>
      </template>
    </sys-dialog>

    <el-dialog title="修改密码" :visible.sync="changePswDialogFormVisible" width="35%" :close-on-click-modal="false">
      <el-form label-width="100px" size="small" style="width: 80%; margin: 0 auto"
               :model="form" :inline="true" :rules="rules2" ref="form">

        <!--        <el-form-item label="原密码" prop="password">-->
        <!--          <el-input v-model="form.password" autocomplete="off" show-password></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="changePswDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="changePsw">确 定</el-button>
      </div>
    </el-dialog>


  </el-main>
</template>

<script>
import SysDialog from "@/components/system/SysDialog.vue";
import {
  addUserApi,
  getUserListApi,
  editUserApi,
  deleteUserApi,
  getPartnerListByAdminApi,
  getAllPartnersByDevApi,
  updateOthersPasswordApi
} from "@/api/user";
import {getRoleListPageApi} from "@/api/role";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";

export default {
  name: "Partnerusers",
  //注册组件
  components: { SysDialog },
  //在页面需要展示的数据，都需要在data里面显示的定义
  data() {
    return {

      regionDataOptions: regionData,
      pcaTextArrOptions: pcaTextArr,
      selectedAreaOptions: [],
      props: { multiple: true },

      id: "",
      userId: "",

      genderOptions: [
        {id: 0, name: "未填写"},
        {id: 1, name: "男"},
        {id: 2, name: "女"}
      ],

      // realName: "",
      // currentPage: 1, //从第几页开始
      // pageSize: 10, //每页查询的条数
      // total: 0, //总条数

      //表格高度
      tableHeight: 0,
      //表格的数据
      tableList: [],

      // user:{},
      roles: [],

      readOnly:false,

      //新增表单的验证规则
      rules: {
        realName: [
          {
            trigger: "blur",
            message: "请填写姓名",
            required: true,
          },
        ],
        username: [
          {
            trigger: "blur",
            message: "请填写登录用户名",
            required: true,
          },
        ],
        // password: [
        //   {
        //     trigger: "blur",
        //     message: "请填写登录密码",
        //     required: true,
        //   },
        // ],
        mobile: [
          {
            trigger: "blur",
            message: "请填写电话",
            required: true,
          },
        ],
        selectedAreaOptions: [
          {
            trigger: "blur",
            message: "请选择地区",
            required: true,
          },
        ],
      },

      //修改密码的验证规则
      rules2: {
        newPassword: [
          {
            trigger: "blur",
            message: "请填写新密码",
            required: true ,
          },
        ],
        confirmPassword: [
          {
            trigger: "blur",
            message: "请再次填写新密码",
            required: true ,
          },
        ],
      },

      // changePsw 弹框
      form: {
        username: "",
        newPassword: "",
        confirmPassword: "",
      },
      changePswDialogFormVisible: false,

      //新增表单绑定的数据域
      addModel: {
        editType: "", //0：新增 1：编辑
        disabled: this.readOnly,

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
        selectedAreaOptions: [],

        gender: "",
        mobile: "",
        email: "",
        address: "",
        status: "",

        appCreateTime: "",
        appNickname: "",

      },

      //弹框属性
      addDialog: {
        title: "",
        height: 300,
        width: 1200,
        visible: false,
      },

      parms: {
        // userId:"",
        realName: "",
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

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    });
  },
  created() {
    // console.log("666666 ===Partnerusers=== this.$route.query.realName", this.$route.query.realName)
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    if(this.$route.query.realName){
      this.parms.realName = this.$route.query.realName;
    }
    this.load()
  },
  methods: {
    load(){
      this.getList();
      this.getRoles();
    },

    async getList() {
      // const rLoading = this.openLoading();

      console.log("888 ===this.parms===", this.parms);

      let res = null;
      if (this.user.roleFlag == "ROLE_DEV"){
        console.log("888 ===ROLE_DEV===" );
        res = await getAllPartnersByDevApi(this.parms);
      }  else if (this.user.roleFlag == "ROLE_ADMIN") {
        console.log("888 ===ROLE_ADMIN===" );
        res = await getPartnerListByAdminApi(this.parms);
      }
      console.log(res);
      if (res && res.data && res.code == 200) {
        // rLoading.close();

        console.log("888 ===res.data===", res.data);
        //给表格数据赋值
        this.tableList = res.data.records;
        // this.tableList = res.data;
        console.log("888 ===this.tableList===", this.tableList);
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
      //20231206 新增时，密码为必填项
      this.$set(this.rules,
        'password',
          [
            { required: true,
              message: '请填写登录密码',
              trigger:['blur']
            }
          ]
      )
      //表单的清空
      this.$resetForm('addForm',this.addModel)
      //设置是新增还是编辑
      this.addModel.roleFlag = "ROLE_PARTNER"
      this.addModel.editType = "0";
      this.readOnly = false;
      this.addDialog.title = "新增用户";
      this.addDialog.visible = true;

    },
    //确认弹框
    onConfirm() {
      //表单验证
      this.$refs.addForm.validate(async (valid) => {
        if (valid) {
          console.log("888===this.addModel===", this.addModel)

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

    //编辑按钮
    editBtn(row) {
      console.log("row", row);
      //表单的清空
      this.$resetForm('addForm',this.addModel)
      //设置弹框属性
      this.addDialog.title = '编辑用户'
      this.addDialog.visible = true;
      //设置为编辑
      this.addModel.editType = '1'; //1:标识编辑
      this.readOnly = true;

      //设置要编辑的数据回显
      //把当前要编辑的数据设置到表单绑定的数据域
      this.$objCoppy(row,this.addModel)

      //20231206 编辑时，密码为非必填项
      this.$delete(this.rules,'password');
      this.addModel.password = '';

      // this.addModel.provinceCode = row.provinceCode
      // this.addModel.provinceName = row.provinceName
      // this.addModel.cityCode = row.cityCode
      // this.addModel.cityName = row.cityName
      // this.addModel.districtCode = row.districtCode
      // this.addModel.districtName = row.districtName

      let provinceCode = row.provinceCode.substr(0, 2)
      console.log("provinceCode",provinceCode)
      let cityCode = row.cityCode.substr(0, 4)
      console.log("cityCode",cityCode)
      let districtCode = row.districtCode
      console.log("districtCode",districtCode)
      // this.selectedAreaOptions = [provinceCode, cityCode, districtCode]
      this.addModel.selectedAreaOptions = [provinceCode, cityCode, districtCode]
      console.log("this.addModel.selectedAreaOptions",this.addModel.selectedAreaOptions)

      console.log("this.addModel",this.addModel)


    },

    //获取表格列表
    // 返回: IPage<User> 则this.tableList = res.data.records;
    // 返回: List<User>  则this.tableList = res.data;

    changePswBtn(row) {
      console.log("row", row);
      //表单的清空
      this.$resetForm('form', this.form)
      //设置弹框属性
      this.changePswDialogFormVisible = true;
      this.form.username = row.username
    },

    //修改密码
    async changePsw() {
      //信息确认提示
      let confirm = await this.$myconfirm('确定修改密码吗?')
      console.log(confirm)
      if(confirm){
        console.log("===this.user===", this.user);
        console.log("===this.form===", this.form);

        this.$refs.form.validate(async (valid) => {
          if (valid) {
            if (this.form.newPassword !== this.form.confirmPassword) {
              this.$message.error("新密码输入不一致,请重新输入")
              this.$resetForm("form", this.form);
              return false
            }

            let res = await updateOthersPasswordApi(this.form);
            if (res && res.code == 200) {
              this.$message.success("密码修改成功")
              // await this.$store.dispatch('user/logout')
              // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
            } else {
              console.log("888 updatePassword===res===", res);

              this.$message.error(res.msg)
            }

            this.changePswDialogFormVisible = false;
            // })
          }
        })
      }

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

    //关闭弹框
    onClose() {
      this.addDialog.visible = false;
    },

    //省市区三级联动下拉框
    addressChange(arr) {
      //arr[0]:11 arr[1]:1101 arr[2]:110101
      // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
      const provinceCode = arr[0]+'0000';
      const cityCode = arr[1]+'00';
      const districtCode = arr[2];
      const provinceName = codeToText[arr[0]];
      const cityName = codeToText[arr[1]];
      const districtName = codeToText[arr[2]];

      console.log("provinceCode:", provinceCode);
      console.log("cityCode:", cityCode);
      console.log("districtCode:", districtCode);
      console.log("provinceName:", provinceName);
      console.log("cityName:", cityName);
      console.log("districtName:", districtName);

      this.addModel.provinceCode = provinceCode
      this.addModel.provinceName = provinceName
      this.addModel.cityCode = cityCode
      this.addModel.cityName = cityName
      this.addModel.districtCode = districtCode
      this.addModel.districtName = districtName

    },

    //搜索按钮
    searchBtn(){
      this.parms.pageNum = 1;
      this.getList();
    },
    //重置按钮
    resetBtn(){
      this.parms.realName = '';
      this.getList();
    },

    //页容量改变时触发
    sizeChange(pageSize) {
      console.log(pageSize)
      this.parms.pageSize = pageSize
      // this.parms.pageNum = 1;
      this.getList()
    },

    //页数改变时触发
    currentChange(pageNum) {
      console.log(pageNum)
      this.parms.pageNum = pageNum
      //重新获取列表
      this.getList()
    },

  },
};
</script>

<style lang="scss" scoped>

a{
  text-decoration: underline;
  color: blue;
}

</style>
