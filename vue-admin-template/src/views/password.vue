<template>
  <el-card style="width: 500px;">
    <el-form label-width="120px" size="small" :model="form" :inline="true" :rules="rules" ref="form">

      <el-form-item label="原密码" prop="password">
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
      </el-form-item>

<!--      <el-form-item>-->
<!--        <el-button type="primary" @click="save">保 存</el-button>-->
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
import {editUserApi, updatePasswordApi} from "@/api/user";

export default {
  name: "Password",
  data() {
    return {
      form: {},
      rules: {
        password: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
          { min: 3, message: '长度不少于3位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 3, message: '长度不少于3位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, message: '长度不少于3位', trigger: 'blur' }
        ],
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
      this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      this.form.username = this.user.username
  },
  methods: {
    async save() {
      //信息确认提示
      let confirm = await this.$myconfirm('修改密码后，请重新登录。')
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

              // this.request.post("/user/updatePassword", this.form).then(res => {

              let res = await updatePasswordApi(this.form);
              if (res && res.code == 200) {
                this.$message.success("密码修改成功")
                await this.$store.dispatch('user/logout')
                this.$router.push(`/login?redirect=${this.$route.fullPath}`)
              } else {
                console.log("888 updatePassword===res===", res);

                this.$message.error(res.msg)
              }
              // })
            }
          })
      }
    },
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
