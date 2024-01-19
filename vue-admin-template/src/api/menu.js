import request from '@/utils/request'
import http from '@/utils/http'

//获取列表
export async function getMenuIdListApi(parm){
  return await http.get("/api/menu/ids",parm)
}
export async function getMenuListApi(parm){
  return await http.get("/api/menu",parm)
}
export async function getMenuListPageApi(parm){
  return await http.get("/api/menu/page",parm)
}
//新增问卷
export async function addMenuApi(parm){
  return await http.post("/api/menu",parm)
}
//编辑
export async function editMenuApi(parm){
  return await http.put("/api/menu",parm)
}
//删除
export async function deleteMenuApi(parm){
  return await http.delete("/api/menu",parm)
}
//批量删除
export async function deleteBatchMenuApi(parm){
  return await http.post("/api/menu/del/batch",parm)
}

