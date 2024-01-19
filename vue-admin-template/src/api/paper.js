import http from '@/utils/http'
//获取列表
export async function getPaperListApi(parm){
    return await http.get("/api/paper/list",parm)
}
//新增问卷
export async function addPaperApi(parm){
    return await http.post("/api/paper",parm)
}
//编辑
export async function editPaperApi(parm){
    return await http.put("/api/paper",parm)
}
//删除
export async function deletePaperApi(parm){
  return await http.delete("/api/paper",parm)
}

//获取问卷
//parm =>  {id:10}
//http://local host:8089/api/user?id=10
export async function getPaperApi(parm){
  return await http.get("/api/paper",parm)
}
//  //parm =>  {id:10}
//   // http://local host:8089/api/user/10
export async function getPaperRestApi(parm){
  return await http.getRestApi("/api/paper",parm)
}

export async function getPapersByActivityIdApi(activityId, parm){
  console.log("====== 999 activityId =====",  activityId );
  console.log("====== 999 parm =====",  parm );

  return await http.get("/api/paper/activityId/"+ activityId, parm)
}

//统计查询
export async function getPaperTotalListApi(parm){
    return await http.get("/api/paper/getTotalList",parm)
}
//分析图
export async function getPaperSysListApi(parm){
    return await http.get("/api/paper/getTotalListEchart",parm)
}
