import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

/* Layout */
// import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
const routes = [
  {
    path: '/',
    component: () => import('@/layout/Layout.vue'),
    redirect:"/home",
    children: [
      { path: '/home', name: 'Home', component: () => import('../views/home.vue')},
      { path: '/about', name: 'About', component: () => import('../views/about.vue')},
      { path: '/chinaarea', name: 'Chinaarea', component: () => import('../views/chinaarea.vue')},
      { path: '/storeDetail/:storeId', name: 'StoreDetail', component: () => import('../views/storeDetail.vue')},
      { path: '/answers/:activityId', name: 'Answers', component: () => import('../views/answers.vue')},
      { path: '/paperCountList', name: 'PaperCountList', component: () => import('../views/paperCountList.vue')},
    ]
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   component: () => import('../views/About.vue'),
  // },
  // {
  //   path: '/',
  //   component: () => import('../views/Layout.vue'),
  //   redirect:"/home",
  //   children: [
  //   { path: 'home', name: 'Home', component: () => import('../views/Home.vue')},
  //   ]
  // },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/register.vue')
  },
  {
    path: '*',
    name: '404',
    component: () => import('../views/404.vue')
  },
  // {
  //   path: '/:pathMatch(.*)*',
  //   name: 'NotFound',
  //   component: () => import('../views/404.vue')
  // }
]

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch((err) => err);
};


// /* 在创建router实例对象之前，手动覆盖原型链的push来吞掉报错catch */
// // 先存储默认底层的push
// const originPush = VueRouter.prototype.push
// // 覆盖原型链的push
// VueRouter.prototype.push = function(location,resolve,reject){
//   // this:路由实例对象
//
//   // 判断用户有没有传后面两个可选参数
//   if( resolve || reject ){
//     return originPush.call(this,location,resolve,reject)
//   }else{// 用户只传了第一个参数
//     /*
//     默认底层： catch()方法代码  throw err : 抛出异常
//     吞掉报错原理： 重写catch()方法,把默认底层的 throw err给去掉，就不会抛出异常
//     */
//     return originPush.call(this,location).catch(err => {
//       //  throw err
//     })
//   }
// }


// const originalPush = VueRouter.prototype.push
// VueRouter.prototype.push = function push(location, onResolve, onReject) {
//   if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
//   return originalPush.call(this, location).catch((err) => err)
// }


//20230729 Good start
// const createRouter = () => new VueRouter({
//   // mode: 'history', // require service support
//   scrollBehavior: () => ({ y: 0 }),
//   // routes: constantRoutes
//   routes
// })
// const router = createRouter()
// // Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
// export function resetRouter() {
//   const newRouter = createRouter()
//   router.matcher = newRouter.matcher // reset router
// }
//20230729 Good End



//20230729
// 1.创建路由
const createRouter = () => new VueRouter({
  mode: 'history', // localhost
  // mode: 'hash',       //  test;prod
  base: process.env.BASE_URL,
  // scrollBehavior: () => ({ y: 0 }),
  routes
})
const router = createRouter()
// 2.重置路由
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',   // localhost
    // mode: 'hash',         //  test;prod
    routes
  })
}
// 3.设置路由
// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    // 拼装动态路由
    const manageRoute =
      {
        path: '/',
        name: 'Layout',
        component: () => import('../layout/Layout.vue'),
        redirect: "/home",
        children:
          [

          ]
      }
    const menus = JSON.parse(storeMenus)
    menus.forEach(item => {
      if (item.path) {  // 当且仅当path不为空的时候才去设置路由
        // let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('/@/views/' + item.pagePath + '.vue')}
        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () =>  Promise.resolve(require('@/views/' + item.pagePath + '.vue').default) }
        manageRoute.children.push(itemMenu)
      } else if(item.children.length) {
        item.children.forEach(item => {
          if (item.path) {
            // console.log("===item.path===", item.path)
            // if (item.path!="/materials") {
                // let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('/@/views/' + item.pagePath + '.vue')}
                let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () =>Promise.resolve(require('@/views/' + item.pagePath + '.vue').default) }
                manageRoute.children.push(itemMenu)
            // }
          }
        })
      }
    })

    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Layout')) {
      // 动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }
  }
}

// 每次刷新页面都要重新设置路由，否则路由就会被重置
setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.path)  // 设置当前的路由名称
  // store.commit("setPath")
  if (!to.matched.length) {
    const menus = localStorage.getItem("menus")
    if (!menus) {
      next("/login")
    } else {
      next("/404")
    }
  } else {
    next()
  }
})

export default router
