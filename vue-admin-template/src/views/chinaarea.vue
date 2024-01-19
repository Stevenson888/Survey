<template>
  <div id="chinaarea">
    <el-cascader
      size="large"
      :options="options"
      v-model="selectedOptions"
      @change="addressChange">
    </el-cascader>

<!--    <el-cascader-->
<!--      size="large"-->
<!--      :options="pcaTextArr"-->
<!--      v-model="selectedOptions">-->
<!--    </el-cascader>-->

  </div>
</template>

<script src="https://unpkg.com/element-china-area-data@6.0.0/dist/element-china-area-data.iife.js"></script>
<script>

import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";

export default {
  name: "Chinaarea",
  data () {
    return {
      // options: regionDataPlus,
      options: regionData,
      selectedOptions: [],

      // codeToText: elementChinaAreaData.codeToText,
      // provinceAndCityData: elementChinaAreaData.provinceAndCityData,
      // pcTextArr: elementChinaAreaData.pcTextArr,
      // regionData: elementChinaAreaData.regionData,
      // pcaTextArr: elementChinaAreaData.pcaTextArr,
      // selectedOptions1: ["11", "110101"],
      // selectedOptions2: ["12", "1201", "120101"],
      // selectedOptions3: ["北京市", "朝阳区"],
      // selectedOptions4: ["北京市", "市辖区", "朝阳区"],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

    }
  },

  methods: {
    created() {
      this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    },
    addressChange(arr) {
      // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
      console.log("111===arr===", arr);
      console.log("222===codeToText[arr[0]]===", codeToText[arr[0]] );
      console.log("333===codeToText[11]===", codeToText[11] );
      console.log("444===", codeToText[arr[0]], codeToText[arr[1]], codeToText[arr[2]]);
    },

    // getCodeToText(codeStr, codeArray) {
    //   if (null === codeStr && null === codeArray) {
    //     return null;
    //   } else if (null === codeArray) {
    //     codeArray = codeStr.split(",");
    //   }
    //
    //   let area = "";
    //   switch (codeArray.length) {
    //     case 1:
    //       area += CodeToText[codeArray[0]];
    //       break;
    //     case 2:
    //       area += CodeToText[codeArray[0]] + "/" + CodeToText[codeArray[1]];
    //       break;
    //     case 3:
    //       area +=
    //         CodeToText[codeArray[0]] +
    //         "/" +
    //         CodeToText[codeArray[1]] +
    //         "/" +
    //         CodeToText[codeArray[2]];
    //       break;
    //     default:
    //       break;
    //   }
    //   return area;
    // }

  }
}
</script>


<style lang="scss" scoped>
</style>
