import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n
import locale from 'element-ui/lib/locale/lang/zh-CN'
import '@/styles/index.scss' // global css

import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts

import App from './App'
import store from './store'
import router from './router'

//打印
import Print from 'vue-print-nb'
 Vue.use(Print);
import '@/icons' // icon
import '@/permission' // permission control

//清空表单
import resetForm from '@/utils/resetForm'
Vue.prototype.$resetForm = resetForm;

import debounce from '@/utils/debounce'
Vue.prototype.$debounce = debounce;

//信息提示
import myconfirm from '@/utils/myconfirm'
Vue.prototype.$myconfirm = myconfirm;

//对象的快速复制
import objCoppy from '@/utils/objCoppy'
Vue.prototype.$objCoppy = objCoppy;

import '@/styles/index.scss';   //引用全局css

//loading
Vue.prototype.openLoading = function() {
  const loading = this.$loading({           // 声明一个loading对象
    lock: true,                             // 是否锁屏
    text: '加载中',                         // 加载动画的文字
    spinner: 'el-icon-loading',             // 引入的loading图标
    background: 'rgba(0, 0, 0, 0.8)',       // 背景颜色
    // opacity: 0.001,
    target: '.el-table, .table-flex, .region',       // **需要遮罩的区域，这里写要添加loading的选择器**
    fullscreen: false,
    customClass: 'loadingclass'             // **遮罩层新增类名，如果需要修改loading的样式**
  })
  setTimeout(function () {                  // 设定定时器，超时5S后自动关闭遮罩层，避免请求失败时，遮罩层一直存在的问题
    loading.close();                        // 关闭遮罩层
  },50000)
  return loading;
}

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

// set ElementUI lang to EN
Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
// Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
