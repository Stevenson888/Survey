<template>
  <el-dialog
    top="1vh"
    :title="title"
    :visible.sync="visible"
    :disabled="disabled"
    :isShow="isShow"
    :width="width + 'px'"
    :before-close="onClose"
  >
    <div class="container" :style="{ height: height + 'px' }">
      <slot name="content"></slot>
    </div>
    <span slot="footer" class="dialog-footer">
      <span  style="float:left;" >
          <el-button  type="primary" @click="onExportExcel" v-show="isShow">导 出</el-button>
          <el-button  type="danger" v-if="print" v-print="'#printTotal'">打 印</el-button>
      </span>
      <span  style="text-align:right;" >
          <el-button type="danger" @click="onClose">取 消</el-button>
          <el-button type="primary" @click="onConfirm" :disabled="disabled">确 定</el-button>
      </span>
    </span>
  </el-dialog>
</template>

<script>
export default {
  //接收父组件传过来的值
  props: {
    title: {
      type: String,
      default: "标题",
    },
    width: {
      type: Number,
      default: 600,
    },
    height: {
      type: Number,
      default: 400,
    },
    visible: {
      type: Boolean,
      default: false,
    },
    isShow: {
      type: Boolean,
      default: false,
    },
    print:{
      type:Boolean,
      default:false
    },
    exportExcel:{
      type:Boolean,
      default:false
    },
    disabled:{
      type:Boolean
    }
  },
  // created() {
  //   console.log("000000 created: this.disabled ", this.disabled)
  // },
  // mounted() {
  //   console.log("000000 mounted: this.disabled ", this.disabled)
  // },
  methods: {
    onExportExcel() {
      //调用父组件的方法
      this.$emit("onExportExcel");
    },
    onClose() {
      //调用父组件的方法
      this.$emit("onClose");
    },
    onConfirm() {
      //调用父组件的方法
      this.$emit("onConfirm");
    },
  },
};
</script>

<style lang="scss" scoped>
.el-dialog__wrapper {
  ::v-deep .el-dialog {
    border-top-left-radius: 7px;
    border-top-right-radius: 7px;
    .el-dialog__header {
      border-top-left-radius: 7px;
      border-top-right-radius: 7px;
      background-color: #1890ff;
      .el-dialog__title {
        color: #fff;
        font-size: 16px;
        font-weight: 600;
      }
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__body {
      padding: 10px;
    }
    .el-dialog__footer {
      border-top: 1px solid #e8eaec;
      padding: 10px;
    }
  }
}
.container {
  overflow-x: inherit;
  overflow-y: auto;
}
</style>
