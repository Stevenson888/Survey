<template>
  <div class="dashboard-container" style="display: flex">
    <el-card class="box-card" style="flex: 1">
      <div slot="header" class="clearfix">
        <span style="color: #000000; font-weight: 600">【敬请期待】</span>
      </div>
<!--      <div class="text item">-->
<!--        <div style="color: #000000; font-weight: 600">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="color: #000000; font-weight: 600">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--      </div>-->
    </el-card>
<!--    <el-card class="box-card" style="flex: 1">-->
<!--      <div slot="header" class="clearfix">-->
<!--        <span style="color: #000000; font-weight: 600">Help</span>-->
<!--      </div>-->
<!--      <div class="text item">-->
<!--        <div style="color: #000000; font-weight: 600">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="padding: 10px 30px">Help</div>-->
<!--        <div style="color: #000000; font-weight: 600;margin-top:20px;color: #FF7670;">Help</div>-->
<!--        <div style="padding: 10px 30px"><span>Help，加</span><span style="color:blue;font-weight: 600;">QQ：Help</span><span>Help；</span></div>-->
<!--      </div>-->
<!--    </el-card>-->
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Help",
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
