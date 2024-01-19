<template>
  <el-card style="width: 500px; height:620px ">
    <!--    rules配合prop：用来校验表单this.$refs.editForm.validate-->
    <el-form label-width="150px" size="large" :model="form" ref="form" :inline="true" :rules="rules">
<!--      <el-form-item label="用户Id">-->
<!--        <el-input v-model="form.id" disabled autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item prop="realName" label="真实姓名">
        <el-input v-model="form.realName" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item prop="username" label="登录用户名">
        <el-input v-model="form.username" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item prop="roleFlag" label="角色标签">
        <el-select :disabled="user.roleFlag!='ROLE_ADMIN'" clearable v-model="form.roleFlag" placeholder="请选择角色" style="width: 200px">
          <el-option v-for="item in roles" :key="item.name" :label="item.description" :value="item.roleFlag"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="mobile" label="电话">
        <el-input v-model="form.mobile" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="gender" label="性别">
        <el-select v-model="form.gender" placeholder="请选择性别" style="width:200px">
          <el-option v-for="(gender,index) in genderOptions" :label="gender.name" :key="gender.id" :value="gender.id" ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="address" label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="status" label="用户状态">
        <el-select disabled v-model="form.status" placeholder="请选择问卷状态" style="width:200px">
          <el-option v-for="(userStatus,index) in userStatusOptions" :label="userStatus.name" :key="userStatus.id" :value="userStatus.id" ></el-option>
        </el-select>
      </el-form-item>

<!--      <el-form-item label="最后登录时间">-->
<!--        <el-input v-model="form.lastLoginTime" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="用户建立时间">-->
<!--        <el-input v-model="form.createTime" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="最后修改时间">-->
<!--        <el-input v-model="form.updateTime" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->

      <div style=" padding-left:200px; padding-top:20px; float:right;">
        <el-form-item >
          <el-button type="primary" @click="save">保 存</el-button>
        </el-form-item>
      </div>

    </el-form>
  </el-card>
</template>

<script>
import http from "@/utils/http";
import {getRoleListPageApi} from "@/api/role";
import {editUserApi} from "@/api/user";

export default {
  name: "Person",
  data() {
    return {
      form: {},
      roles: [],
      parms: {
        name: "",   //role.name
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },
      genderOptions: [
        {id: 0, name: "未填写"},
        {id: 1, name: "男"},
        {id: 2, name: "女"}
      ],
      userStatusOptions: [  //is_active 活动状态：0下线; 1上线
        {id: 0, name: "无效"},
        {id: 1, name: "有效"}
      ],
      rules: {
        realName: [
          {
            trigger: "change",
            message: "请填写真实姓名",
            required: true,
          },
        ],
        username: [
          {
            trigger: "change",
            message: "请填写登录用户名",
            required: true,
          },
        ],
        mobile: [
          {
            trigger: "change",
            message: "请填写手机号",
            required: true,
          },
        ],
        roleFlag: [
          {
            trigger: "change",
            message: "请选择角色",
            required: true,
          },
        ],
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    // this.getUser(this.user).then(res => {
    //   console.log(res)
    //   this.form = res
    // })
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    load(){
      this.form = this.user
      console.log("===this.user===", this.user);
      this.getRoles();
    },
    async getUser(id) {
      this.user = (await http.get("/api/user/" + id)).data;
      return this.user
    },
    async getRoles() {
      let res = await getRoleListPageApi(this.parms);
      console.log(res);
      if (res && res.data && res.code == 200) {
        this.roles = res.data.records
      }
    },
    async save() {
      //信息确认提示
      let confirm = await this.$myconfirm('修改个人信息后，请重新登录。')
      console.log(confirm)
      if(confirm){
          console.log("===this.user===", this.user);
          console.log("===this.form===", this.form);

          //表单验证
          this.$refs.form.validate(async (valid) => {
            if (valid) {
              let res = await editUserApi(this.form);
              if (res.code === 200) {
                this.$message.success("保存成功")

                // 触发父级更新User的方法
                // this.$emit("refreshUser")

                // 更新浏览器存储的用户信息
                this.user = this.getUser(this.user.id).then(res => {
                    localStorage.setItem("user", JSON.stringify(this.user))
                })

              } else {
                this.$message.error("保存失败")
              }
            }
          })

        await this.$store.dispatch('user/logout')
        this.$router.push(`/login?redirect=${this.$route.fullPath}`)

      }
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
