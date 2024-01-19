import request from '@/utils/request'
import http from '@/utils/http'

//获取单个门店
// export async function getStoreByIdApi(parm){
//   return await http.getRestApi("/api/store/getStoreById",parm)
// }
export async function getStoreByIdApi(parm){
  return await http.getLongIdRestApi("/api/store/getStoreById",parm)
}
//获取列表
export async function getStoreListApi(parm){
  return await http.get("/api/store",parm)
}
export async function getStoreListPageApi(parm){
  return await http.get("/api/store/page",parm)
}
//获取零售门店列表
export async function getStoreListByAdminApi(parm){
  return await http.get("/api/store/getStoreListByAdmin",parm)
}
//获取某一个客户经理的零售门店列表
export async function getStoreListByUserIdApi(parm){
  return await http.get("/api/store/getStoreListByUserId",parm)
}
//获取零售门店用户列表
export async function getStoreListByPartnerApi(parm){
  return await http.get("/api/store/getStoreListByPartner",parm)
}
//新增
export async function addStoreApi(parm){
  return await http.post("/api/store",parm)
}
//编辑
export async function editStoreApi(parm){
  return await http.put("/api/store",parm)
}
//新增&编辑项目
// export async function insertOrUpdateStoreApi(parm){
//   return await http.post("/api/store",parm)
// }
//删除
export async function deleteStoreApi(parm){
  return await http.delete("/api/store",parm)
}
//删除
export async function deleteAuthByStoreId(parm){
  return await http.updateLongIdRestApi("/api/store/deleteAuthByStoreId",parm)
}
//批量删除
export async function deleteBatchStoreApi(parm){
  return await http.post("/api/store/del/batch",parm)
}
//getStoresByUserId
export async function getStoresByUserId(parm){
  return await http.get("/api/store/getStoresByUserId",parm)
}
// export async function exportStoreListApi(parm){
//   return await http.get("/api/store/exportStoreList",parm)
// }





// //获取问卷
// //parm =>  {id:10}
// //http://local host:8089/api/user?id=10
// export async function getApi(parm){
//   return await http.get("/api/user",parm)
// }
// //  //parm =>  {id:10}
// //   // http://local host:8089/api/user/10
// export async function getRestApi(parm){
//   return await http.getRestApi("/api/user",parm)
// }



