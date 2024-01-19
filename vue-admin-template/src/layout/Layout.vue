<template>
  <el-container>

  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />

<!--    <sidebar class="sidebar-container" />-->

    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" class="sidebar-container"/>
    </el-aside>

    <div class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar  :user="user" @refreshUser="getUser" />
      </div>
      <app-main />
    </div>
  </div>

  </el-container>

</template>

<script>
import {  Navbar, Sidebar, AppMain } from './components'
import { Aside } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import http from "@/utils/http";

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    Aside
  },
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      user: {}
    }
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    fixedHeader() {
      return this.$store.state.settings.fixedHeader
    },
    classObj() {
      return {
        // hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },

  //页面加载时
  created() {
      this.getUser()
  },

  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    async getUser() {
        this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : ""
        await http.get("/api/user/" + this.user.id).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    &.mobile.openSidebar{
      position: fixed;
      top: 0;
    }
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px)
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
