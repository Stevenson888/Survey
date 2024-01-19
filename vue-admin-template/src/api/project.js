import request from '@/utils/request'
import http from '@/utils/http'



//获取项目
export async function getProjectApi(parm){
  return await http.get("/api/project",parm)
}
//获取项目页
export async function getProjectPageApi(parm){
  return await http.get("/api/project/page",parm)
}
//获取项目列表
export async function getProjectListApi(parm){
  return await http.get("/api/project/list",parm)
}
//获取项目关联的活动
export async function getSelectedActivityListByProjectIdApi(parm){
  return await http.getLongIdRestApi("/api/project/getSelectedActivityListByProjectId",parm)
}
//获取项目关联的活动id
export async function getSelectedActivityIdListByProjectIdApi(parm){
  return await http.getLongIdRestApi("/api/project/getSelectedActivityIdListByProjectId",parm)
}
//新增项目
export async function insertOrUpdateProjectApi(parm){
  return await http.post("/api/project",parm)
}
//编辑项目
export async function editProjectApi(parm){
  return await http.put("/api/project",parm)
}
//删除项目
export async function deleteProjectApi(parm){
  return await http.delete("/api/project",parm)
}


//删除项目
export async function getFileListByProjectIdPageApi(parm){
  return await http.getLongIdRestApi("/api/file/getFileListByProjectId",parm)
}

// //删除项目报告文件
// export async function deleteProjectFileApi(parm){
//   console.log("deleteProjectFileApi", parm)
//   return await http.delete("/api/file",parm)
// }
//删除项目报告文件
export async function deleteProjectFileApi(parm){
  console.log("deleteProjectFileApi", parm)
  return await http.delete("/api/file",parm)
}

// //获取客户经理用户列表
// export async function getPartnerListByAdminApi(parm){
//   return await http.get("/api/user/getPartnerListByAdmin",parm)
// }

// //获取零售门店用户列表
// export async function getStoreUserList(parm){
//   return await http.get("/api/user/getStoreUserList",parm)
// }
// //获取零售门店用户列表
// export async function getCustomerListByPartnerApi(parm){
//   return await http.get("/api/user/getCustomerListByPartner",parm)
// }
//
//
// //获取消费者用户列表
// export async function getCustomerListByAdminApi(parm){
//   return await http.get("/api/user/getCustomerListByAdmin",parm)
// }
// //编辑用户
// export async function editUserApi(parm){
//   return await http.put("/api/user",parm)
// }
// //删除用户
// export async function deleteUserApi(parm){
//   return await http.delete("/api/user",parm)
// }
//
// //根据用户ID获取门店
// export async function getStoresByUserId(parm){
//   return await http.get("/api/user/getStoresByUserId",parm)
// }
