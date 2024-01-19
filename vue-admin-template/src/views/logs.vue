<template>
  <div class="dashboard-container" style="display: flex">
    <el-card class="box-card" style="flex: 1">
      <div slot="header" class="clearfix">
        <span style="color: #000000; font-weight: 600">【Log Page is Under Construction】</span>
      </div>
      <div class="text item">
        <div style="color: #000000; font-weight: 600"></div>
        <div style="padding: 10px 30px"></div>
        <div style="color: #000000; font-weight: 600"></div>
        <div style="padding: 10px 30px"></div>
        <div style="padding: 10px 30px"></div>
        <div style="padding: 10px 30px"></div>
        <div style="padding: 10px 30px"></div>
      </div>
    </el-card>
<!--    <el-card class="box-card" style="flex: 1">-->
<!--      <div slot="header" class="clearfix">-->
<!--        <span style="color: #000000; font-weight: 600"></span>-->
<!--      </div>-->
<!--      <div class="text item">-->
<!--        <div style="color: #000000; font-weight: 600"></div>-->
<!--        <div style="padding: 10px 30px"></div>-->
<!--        <div style="padding: 10px 30px"></div>-->
<!--        <div style="padding: 10px 30px"></div>-->
<!--        <div style="padding: 10px 30px"></div>-->
<!--        <div style="color: #000000; font-weight: 600;margin-top:20px;color: #FF7670;"></div>-->
<!--        <div style="padding: 10px 30px"><span></span><span style="color:blue;font-weight: 600;"></span><span></span></div>-->
<!--      </div>-->
<!--    </el-card>-->
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Log",
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
