import request from '@/utils/request'
import http from '@/utils/http'

//获取列表
// export async function getListApi(parm){
//   return await http.get("/api/role",parm)
// }
export async function getRoleListPageApi(parm){
  return await http.get("/api/role/page",parm)
}

//新增
export async function addRoleApi(parm){
  return await http.post("/api/role",parm)
}
//编辑
export async function editRoleApi(parm){
  return await http.put("/api/role",parm)
}
//删除
export async function deleteRoleApi(parm){
  return await http.delete("/api/role",parm)
}
//获取roleMenu:  /api/role/roleMenu/{roleId}
export async function getMenusByRoleApi(parm){
  return await http.getRestApi("/api/role/roleMenu",parm)
}
//保存roleMenu:  /api/role/roleMenu/{roleId}
export async function saveMenusByRoleApi(roleId, parm){
  return await http.post("/api/role/roleMenu/" + roleId, parm)
}



// //获取
// //parm =>  {id:10}
// //http://local host:8089/api/user?id=10
// export async function getRoleApi(parm){
//   return await http.get("/api/role",parm)
// }
// //  //parm =>  {id:10}
// // http://local host:8089/api/user/10
// export async function getRoleRestApi(parm){
//   return await http.getRestApi("/api/role",parm)
// }
