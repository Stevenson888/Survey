<template>
  <el-main>

  <div>

<!--    <div style="margin: 10px 0">-->
<!--      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>-->
<!--      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>-->
<!--      <el-button type="warning" @click="reset">重置</el-button>-->
<!--    </div>-->
<!--    <div style="margin: 10px 0">-->
<!--      <el-button type="primary" @click="handleAdd(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>-->
<!--      <el-popconfirm-->
<!--        class="ml-5"-->
<!--        confirm-button-text='确定'-->
<!--        cancel-button-text='我再想想'-->
<!--        icon="el-icon-info"-->
<!--        icon-color="red"-->
<!--        title="您确定批量删除这些数据吗？"-->
<!--        @confirm="delBatch"-->
<!--      >-->
<!--        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>-->
<!--      </el-popconfirm>-->
<!--    </div>-->

    <el-form :model="parms" ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item label="搜索">
        <el-input v-model="parms.name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-remove-outline"  @click="delBatch">批量删除</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableList" border stripe :header-cell-class-name="'headerBg'"
              row-key="id" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="pid" label="父级ID"></el-table-column>
      <el-table-column prop="pagePath" label="页面路径"></el-table-column>
      <el-table-column prop="sortNum" label="顺序"></el-table-column>
      <el-table-column label="操作"  width="400" align="left">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="danger" @click="del(scope.row)">删除 <i class="el-icon-delete"></i></el-button>
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单 <i class="el-icon-plus"></i></el-button>
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
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="父级ID">
          <el-input v-model="form.pid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="顺序">
          <el-input v-model="form.sortNum" autocomplete="off"></el-input>
        </el-form-item>
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
import {getMenuListPageApi, addMenuApi, editMenuApi, deleteMenuApi, deleteBatchMenuApi} from "@/api/menu";

export default {
  name: "Menu",
  data() {
    return {
      //表格的数据
      tableList: [],
      parms: {
        name: "",
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
      },
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      options: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    // load() {
    //   this.request.get("/menu", {
    //     params: {
    //       name: this.name,
    //     }
    //   }).then(res => {
    //     this.tableData = res.data
    //   })
    //
    //   // 请求图标的数据
    //   this.request.get("/menu/icons").then(res => {
    //     this.options = res.data
    //   })
    // },

    load() {
      this.getMenus()
    },

    //搜索按钮
    searchBtn() {
      this.getRoles();
    },

    async getMenus() {
      let res = await getMenuListPageApi(this.parms);
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.tableList = res.data.records;
        //总条数
        this.parms.total = res.data.total;
      }
    },

    async save() {
      // this.request.post("/menu", this.form).then(res => {
      //   if (res.code === '200') {
      //     this.$message.success("保存成功")
      //     this.dialogFormVisible = false
      //     this.load()
      //   } else {
      //     this.$message.error("保存失败")
      //   }
      // })
      let res = await addMenuApi(this.form);

      if (res.code === 200 ) {
        this.$message.success("保存成功")
        this.dialogFormVisible = false
        this.load()
      } else {
        this.$message.error("保存失败")
      }
    },
    handleAdd(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    async del(row) {
      //信息提示
      let confirm = await this.$myconfirm("确定删除该数据吗？");
      if (confirm) {
        let res = await deleteMenuApi({ id: row.id });

        if (res && res.code === 200 ) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      }
    },
    // del(id) {
    //   this.request.delete("/menu/" + id).then(res => {
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
    // delBatch() {
    //   let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
    //   this.request.post("/menu/del/batch", ids).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("批量删除成功")
    //       this.load()
    //     } else {
    //       this.$message.error("批量删除失败")
    //     }
    //   })
    // },
    async delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      //信息提示
      let confirm = await this.$myconfirm("确定批量删除该数据吗？");
      if (confirm) {
        let res = await deleteBatchMenuApi({ ids });

        if (res && res.code === 200 ) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      }
    },
    reset() {
      this.parms.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>
