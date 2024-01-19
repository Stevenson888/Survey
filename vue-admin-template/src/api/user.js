import request from '@/utils/request'
import http from '@/utils/http'
// export function login(data) {
//   return request({
//     url: '/vue-admin-template/user/login',
//     method: 'post',
//     data
//   })
// }
// export function getInfo(token) {
//   return request({
//     url: '/vue-admin-template/user/info',
//     method: 'get',
//     params: { token }
//   })
// }
export async function login(parm){
  return http.post("/api/user/login",parm)
}
export async function register(parm){
  return http.post("/api/user/register",parm)
}

// export function getInfo(token) {
//   return http.get("/api/user/getInfo",{userId:token})
// }
export function getInfo() {
  let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : ""
  return http.get("/api/user/getInfo",{id: user.id})
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
//新增用户
export async function addUserApi(parm){
  return await http.post("/api/user",parm)
}
//获取客户经理用户列表
export async function getAllAdminsByDevApi(parm){
  return await http.get("/api/user/getAllAdminsByDev",parm)
}
//获取用户列表
export async function getUserListApi(parm){
  return await http.get("/api/user/list",parm)
}
//获取客户经理用户列表
export async function getAllPartnersByDevApi(parm){
  return await http.get("/api/user/getAllPartnersByDev",parm)
}
//获取客户经理用户列表
export async function getPartnerListByAdminApi(parm){
  return await http.get("/api/user/getPartnerListByAdmin",parm)
}

// //获取零售门店用户列表
// export async function getStoreUserList(parm){
//   return await http.get("/api/user/getStoreUserList",parm)
// }
//获取零售门店用户列表
export async function getCustomerListByPartnerApi(parm){
  return await http.get("/api/user/getCustomerListByPartner",parm)
}


//获取消费者用户列表
export async function getCustomersByDevApi(parm){
  return await http.get("/api/user/getCustomersByDev",parm)
}
//获取消费者用户列表
export async function getCustomerListByAdminApi(parm){
  return await http.get("/api/user/getCustomerListByAdmin",parm)
}
//编辑用户
export async function editUserApi(parm){
  return await http.put("/api/user",parm)
}
//编辑用户
export async function updatePasswordApi(parm){
  return await http.put("/api/user/updatePassword",parm)
}
//编辑用户
export async function updateOthersPasswordApi(parm){
  return await http.put("/api/user/updateOthersPassword",parm)
}
export async function updateUserStatusApi(parm){
  return await http.put("/api/user/updateUserStatus",parm)
}
//删除用户
export async function deleteUserApi(parm){
  return await http.delete("/api/user",parm)
}

//根据用户ID获取门店
export async function getStoresByUserId(parm){
  return await http.get("/api/user/getStoresByUserId",parm)
}

//获取所有客户经理
export async function getAllPartnerListApi(parm){
  return await http.get("/api/user/getAllPartnerList",parm)
}
//获取所有客户经理
export async function getAllPartnerListByAreaApi(parm){
  return await http.getLongIdRestApi("/api/user/getAllPartnerListByArea",parm)
}
