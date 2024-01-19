import request from '@/utils/request'
import http from '@/utils/http'



//获取项目
export async function getActivityApi(parm){
  return await http.get("/api/activity",parm)
}
//获取项目页
export async function getTotalListByActivityIdApi(parm){
  return await http.getLongIdRestApi("/api/activity/getTotalListByActivityId",parm)
}
//获取项目页
export async function getPapersByActivityIdApi(parm){
  return await http.getLongIdRestApi("/api/papers/activityId",parm)
}
//获取项目页
export async function exportTotalListByActivityIdApi(parm){
  return await http.getLongIdRestApi("/api/activity/export/getTotalListByActivityId",parm)
}
//获取项目页
export async function getActivityPageApi(parm){
  return await http.get("/api/activity/page",parm)
}
//获取项目列表
export async function getAllActivityListApi(parm){
  return await http.get("/api/activity",parm)
}
//获取项目VO列表
export async function getAllActivityVoListApi(parm){
  return await http.get("/api/activity/getAllActivityVoList",parm)
}
//获取所有已关联项目Project的活动ActivityVO列表
export async function getAllUnselectedActivityVoListApi(parm){
  return await http.get("/api/activity/getAllUnselectedActivityVoList",parm)
}
// //获取项目列表
// export async function getActivityTitleListApi(parm){
//   return await http.get("/api/activity/activityTitleList",parm)
// }

//新增项目
export async function insertOrUpdateActivityApi(parm){
  return await http.post("/api/activity",parm)
}
// //新增项目
// export async function addActivityApi(parm){
//   return await http.post("/api/activity",parm)
// }
// //编辑项目
// export async function editActivityApi(parm){
//   return await http.put("/api/activity",parm)
// }
//删除项目
export async function deleteActivityApi(parm){
  return await http.delete("/api/activity",parm)
}


