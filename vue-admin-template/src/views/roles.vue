<template>
  <el-main>
  <div>

    <el-form :model="parms" ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item label="搜索">
        <el-input v-model="parms.name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableList" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" ></el-table-column>-->
      <el-table-column prop="id" label="ID" width="200px"></el-table-column>
      <el-table-column prop="name" label="名称" ></el-table-column>
      <el-table-column prop="roleFlag" label="唯一标识" ></el-table-column>
      <el-table-column prop="description" label="描述" ></el-table-column>
      <el-table-column label="操作"  align="left" width="400px">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单 <i class="el-icon-menu"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="danger" @click="del(scope.row)">删除 <i class="el-icon-delete"></i></el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="parms.pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="parms.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="parms.total"
          background>
      </el-pagination>
    </div>

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.roleFlag" autocomplete="off" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>

  </el-main>

</template>

<script>
import {getRoleListPageApi, addRoleApi, editRoleApi, deleteRoleApi, getMenusByRoleApi, saveMenusByRoleApi} from "@/api/role";
import {getMenuIdListApi, getMenuListApi} from "@/api/menu";


export default {
  name: "Role",
  data() {
    return {
      //表格的数据
      tableList: [],
      parms: {
        name: "",
        total: 0,
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
      },
      // total: 0,
      // pageNum: 1,
      // pageSize: 10,
      // name: "",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: "",
      roleFlag2: "",
      ids: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    // load() {
    //   this.service.get("/role/page", {
    //     params: {
    //       pageNum: this.pageNum,
    //       pageSize: this.pageSize,
    //       name: this.name,
    //     }
    //   }).then(res => {
    //     this.tableData = res.data.records
    //     this.total = res.data.total
    //   })
    //
    //   this.request.get("/menu/ids").then(r => {
    //     this.ids = r.data
    //   })
    // },
    load() {
      this.getRoles()
      this.getMenuIdList()

        // this.request.get("/menu/ids").then(r => {
        //   this.ids = r.data
        // })

    },

    //搜索按钮
    searchBtn() {
      this.getRoles();
    },

    async getRoles() {
      let res = await getRoleListPageApi(this.parms);
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.tableList = res.data.records;
        //总条数
        this.parms.total = res.data.total;
      }
    },

    async getMenuIdList() {
      let res = await getMenuIdListApi(this.parms);
      if (res && res.data && res.code == 200) {
        this.ids = res.data
      }
    },

    async save() {
      let res = await addRoleApi(this.form);

      if (res.code === 200 ) {
        this.$message.success("保存成功")
        this.dialogFormVisible = false
        this.load()
      } else {
        this.$message.error("保存失败")
      }
    },
    // saveRoleMenu() {
    //   this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("绑定成功")
    //       this.menuDialogVis = false
    //
    //       // 操作管理员角色后需要重新登录
    //       if (this.roleFlag === 'ROLE_ADMIN') {
    //         this.$store.commit("logout")
    //       }
    //
    //     } else {
    //       this.$message.error(res.msg)
    //     }
    //   })
    // },

    async saveRoleMenu() {
      // this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
      //   if (res.code === '200') {
      //     this.$message.success("绑定成功")
      //     this.menuDialogVis = false
      //     // 操作管理员角色后需要重新登录
      //     if (this.roleFlag === 'ROLE_ADMIN') {
      //       this.$store.commit("logout")
      //     }
      //   } else {
      //     this.$message.error(res.msg)
      //   }
      // })

      console.log(" 888888 role === this.$refs.tree.getCheckedKeys()" , this.$refs.tree.getCheckedKeys())

      // return

      //this.roleId: 1.developer 2.admin 3.partner 4.store 5.customer
      // this.roleFlag2是指修改的哪个role
      if ( this.roleId === 1){
        this.roleFlag2 = 'ROLE_DEV'
      }else if( this.roleId === 2 ){
        this.roleFlag2 = 'ROLE_ADMIN'
      }else if( this.roleId === 3 ){
        this.roleFlag2 = 'ROLE_PARTNER'
      }else if( this.roleId === 4 ){
        this.roleFlag2 = 'ROLE_STORE'
      }else if( this.roleId === 5 ){
        this.roleFlag2 = 'ROLE_CUSTOMER'
      }

      let res = await saveMenusByRoleApi(this.roleId, this.$refs.tree.getCheckedKeys());
      if (res.code === 200 ) {
        this.$message.success("绑定成功")
        this.menuDialogVis = false

        // 操作管理员角色后需要重新登录
        // if (this.roleFlag === 'ROLE_ADMIN') {

        console.log(" 888888 role === this.user.roleFlag" , this.user.roleFlag )
        console.log(" 888888 role === this.roleFlag2" , this.roleFlag2 )
        // 编辑自己的角色后需要重新登录
        if (this.user.roleFlag === this.roleFlag2) {       // this.roleFlag2是指修改的哪个role,this.roleFlag为当前登录用户的role
          //调的store里面user模块的logout方法
          await this.$store.dispatch('user/logout')
          this.$router.push(`/login?redirect=${this.$route.fullPath}`)
        }
      } else {
        this.$message.error(res.msg)
      }
    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    async del(row) {
      //信息提示
      let confirm = await this.$myconfirm("确定删除该数据吗？");
      if (confirm) {
        console.log("===111 role row=== role row", row );
        let res = await deleteRoleApi({ id: row.id });
        console.log("===111 role === role res", res );

        if (res && res.code === 200 ) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      }
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/role/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.parms.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.parms.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.parms.pageNum = pageNum
      this.load()
    },
    // async selectMenu(role) {
    //   this.roleId = role.id
    //   this.roleFlag = role.flag
    //
    //   // 请求菜单数据
    //   this.request.get("/menu").then(res => {
    //     this.menuData = res.data
    //
    //     // 把后台返回的菜单数据处理成 id数组
    //     this.expends = this.menuData.map(v => v.id)
    //   })
    //
    //   this.menuDialogVis = true
    //   this.request.get("/role/roleMenu/" + this.roleId).then(res => {
    //     this.checks = res.data
    //     this.$refs.tree.setCheckedKeys(res.data)
    //     this.ids.forEach(id => {
    //       if (!this.checks.includes(id)) {
    //         this.$refs.tree.setChecked(id, false)
    //       }
    //     })
    //   })
    // },

    async selectMenu(role) {
      this.roleId = role.id
      this.roleFlag = role.roleFlag

      // // 请求菜单数据
      // this.request.get("/api/menu").then(res => {
      //     this.menuData = res.data
      //     // 把后台返回的菜单数据处理成 id数组
      //     this.expends = this.menuData.map(v => v.id)
      // })


      let res = await getMenuListApi(this.parms);
      if (res && res.data && res.code == 200) {
        this.menuData = res.data
        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
        console.log("=== 888 roles.selectMenu === this.expends" , this.expends)
      }

      this.menuDialogVis = true

      let res2 = await getMenusByRoleApi({roleId: this.roleId});
      if (res2 && res2.code == 200) {
        this.checks = res2.data
        console.log("=== 888 roles.selectMenu === this.checks" , this.checks)
        this.$refs.tree.setCheckedKeys(res2.data)
        console.log("=== 888 roles.selectMenu === this.ids" , this.ids)
        this.ids.forEach(id => {
            if (!this.checks.includes(id)) {
              this.$refs.tree.setChecked(id, false)
            }
        })
      }

    },

  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
