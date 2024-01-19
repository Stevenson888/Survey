<template>
  <div class="dashboard-container" style="display: flex">
    <el-card class="box-card" style="flex: 1">
      <div slot="header" class="clearfix">
        <span style="color: #000000; font-weight: 600">【About】</span>
      </div>
      <div class="text item">
        <div style="color: #000000; font-weight: 600">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="color: #000000; font-weight: 600">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="padding: 10px 30px">About</div>
      </div>
    </el-card>
    <el-card class="box-card" style="flex: 1">
      <div slot="header" class="clearfix">
        <span style="color: #000000; font-weight: 600">About</span>
      </div>
      <div class="text item">
        <div style="color: #000000; font-weight: 600">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="padding: 10px 30px">About</div>
        <div style="color: #000000; font-weight: 600;margin-top:20px;color: #FF7670;">About</div>
        <div style="padding: 10px 30px"><span>About，加</span><span style="color:blue;font-weight: 600;">QQ：About</span><span>About；</span></div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "About",
  computed: {
    ...mapGetters(["name"]),
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
