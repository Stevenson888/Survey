<template>
  <div class="wrapper">
    <div style="height: 50px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">管理系统</div>
    <div style=" margin: 0px auto; background-color: #fff; width: 1200px;  padding: 20px; border-radius: 10px; ">
      <div style="margin: 0px 0; text-align: center; font-size: 24px; "><b>注 册</b></div>

      <el-form label-width="120px" :model="userForm" :rules="rules" ref="userForm">
        <el-row>
          <el-col :span="12" style=" padding: 20px;" >

        <el-form-item prop="username" label="用户名">
          <el-input placeholder="请输入用户名" size="medium" prefix-icon="el-icon-user" autocomplete="off" v-model="userForm.username"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" autocomplete="off" show-password v-model="userForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword" label="核实密码">
          <el-input placeholder="请确认密码" size="medium" prefix-icon="el-icon-lock" autocomplete="off" show-password v-model="userForm.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item prop="realName" label="真实姓名">
          <el-input placeholder="请输入真实姓名" size="medium" prefix-icon="el-icon-user" v-model="userForm.realName"></el-input>
        </el-form-item>
<!--        <el-form-item prop="roleFlag">-->
<!--          <el-input placeholder="请输入角色标签" size="medium" prefix-icon="el-icon-user" v-model="userForm.roleFlag"></el-input>-->
<!--        </el-form-item>-->

<!--            <el-form-item prop="gender">-->
<!--              <el-input placeholder="请输入性别" size="medium" prefix-icon="el-icon-user" v-model="userForm.gender"></el-input>-->
<!--            </el-form-item>-->
            <el-form-item prop="gender" label="性别">
              <el-select v-model="userForm.gender" placeholder="请选择性别" style="width:420px">
                <el-option v-for="(gender,index) in genderOptions" :label="gender.name" :key="gender.id" :value="gender.id" ></el-option>
              </el-select>
            </el-form-item>

          </el-col>


          <el-col :span="12" style=" padding: 20px;">
<!--        <el-form-item prop="provinceCode">-->
<!--          <el-input placeholder="请输入省份编号" size="medium" prefix-icon="el-icon-user" v-model="userForm.provinceCode"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="provinceName">-->
<!--          <el-input placeholder="请输入省份名称" size="medium" prefix-icon="el-icon-user" v-model="userForm.provinceName"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="cityCode">-->
<!--          <el-input placeholder="请输入城市编号" size="medium" prefix-icon="el-icon-user" v-model="userForm.cityCode"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="cityName">-->
<!--          <el-input placeholder="请输入城市名称" size="medium" prefix-icon="el-icon-user" v-model="userForm.cityName"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="districtCode">-->
<!--          <el-input placeholder="请输入区域编号" size="medium" prefix-icon="el-icon-user" v-model="userForm.districtCode"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="districtName">-->
<!--          <el-input placeholder="请输入区域名称" size="medium" prefix-icon="el-icon-user" v-model="userForm.districtName"></el-input>-->
<!--        </el-form-item>-->

            <el-form-item prop="mobile" label="手机号">
              <el-input placeholder="请输入手机号码" size="medium" prefix-icon="el-icon-user" v-model="userForm.mobile"></el-input>
            </el-form-item>
            <el-form-item prop="email" label="邮箱">
              <el-input placeholder="请输入邮箱" size="medium" prefix-icon="el-icon-user" v-model="userForm.email"></el-input>
            </el-form-item>
            <el-form-item prop="roleFlag" label="角色">
              <el-select clearable v-model="userForm.roleFlag" placeholder="请选择角色" style="width: 100%">
                <el-option v-for="item in roles" :key="item.name" :label="item.description" :value="item.roleFlag"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item  prop="address" label="地址">
              <el-input placeholder="请输入地址" size="medium" prefix-icon="el-icon-user" v-model="userForm.address"></el-input>
            </el-form-item>
            <el-form-item prop="selectedAreaOptions" label="选择地区">
              <el-cascader
                clearable
                filterable
                size="large"
                style="width: 420px"
                :placeholder="请选择区域"
                :options="regionDataOptions"
                v-model="userForm.selectedAreaOptions"
                @change="addressChange">
              </el-cascader>
            </el-form-item>

            </el-col>
              </el-row>

        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="register">注册</el-button>
        </el-form-item>
      </el-form>

<!--      </div>-->
    </div>
  </div>
</template>

<script>

import {editUserApi, register} from "@/api/user";
import {getRoleListPageApi} from "@/api/role";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";

export default {
  name: "Register",
  data() {
    return {
      roles: [],
      genderOptions: [
        {id: 0, name: "未填写"},
        {id: 1, name: "男"},
        {id: 2, name: "女"}
      ],
      parms2: {
        name: "",
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },
      //新增表单绑定的数据域
      userForm: {
        // id: "",
        // userId: this.userId,
        username: "",
        password: "",
        confirmPassword: "",
        realName: "",
        gender: "",
        mobile: "",
        email: "",
        roleFlag: "",

        provinceCode: "",
        provinceName: "",
        cityCode: "",
        cityName: "",
        districtCode: "",
        districtName: "",

        selectedAreaOptions: [],
        address: "",

        // lastLoginTime: "",
        // createTime: "",
        // updateTime: "",

      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'blur' },
        ],
        mobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
        ],
        roleFlag: [
          { required: true, message: '请选择角色', trigger: 'blur' },
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' },
        ],
        selectedAreaOptions: [
          { required: true, message: '请选择地区', trigger: 'blur' },
        ],
      },

      regionDataOptions: regionData,
      pcaTextArrOptions: pcaTextArr,
      selectedAreaOptions: [],
      props: { multiple: true },

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.$resetForm("userForm", this.userForm);
    this.getRoles();
  },
  methods: {
    async getRoles() {
      let res = await getRoleListPageApi(this.parms2);
      console.log(res);
      if (res && res.data && res.code == 200) {
        this.roles = res.data.records
      }
    },
    register() {
      this.$refs.userForm.validate(async (valid) => {
        let res = null;
        if (valid) {  // 表单校验合法

          if (this.userForm.password !== this.userForm.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }

          console.log("===this.userForm===", this.userForm);
          res = await register(this.userForm)
          console.log(res);

          if (res && res.code == 200) {
            this.$message.success("注册成功")
          } else {
            this.$message.error(res.msg)
          }

          this.$router.push({ path:  "/login" });
      }
      });
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

      this.userForm.provinceCode = provinceCode
      this.userForm.provinceName = provinceName
      this.userForm.cityCode = cityCode
      this.userForm.cityName = cityName
      this.userForm.districtCode = districtCode
      this.userForm.districtName = districtName

    },
  }
}
</script>

<style>
  .wrapper {
    height: 100vh;
    background-image: linear-gradient(to bottom right,  #4169E1 ,	#87CEFA);
    overflow: scroll;
  }
</style>
