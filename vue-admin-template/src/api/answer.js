import request from '@/utils/request'
import http from '@/utils/http'



// //获取项目
// export async function getActivityApi(parm){
//   return await http.get("/api/activity",parm)
// }
// //获取项目页 getAnswerListByActivityId
// export async function getAnswerListByActivityIdApi(parm){
//   return await http.get("/api/activity/page",parm)
// }
//获取项目列表
export async function getAnswerListByActivityIdApi(activityId, parm){
  return await http.get("/api/answer/getAnswerListByActivityId/" + activityId, parm)
}
export async function getAnswersProcessByActivityIdApi(activityId, parm){
  return await http.get("/api/answer/getAnswersProcessByActivityId/" + activityId, parm)
}
//获取一份答卷
export async function getSelectQuestionOptionResultsByAnswerIdApi( parm){
  return await http.getRestApi("/api/activity/getSelectQuestionOptionResultsByAnswerId", parm)
}
export async function exportAnswerListApi(activityId, parm){
  return await http.get("/api/answer/exportAnswerList/" + activityId, parm)
}

// export async function saveQuestionApi(paperId,parm){
//   return await http.post("/api/question/saveQuestion/" + paperId ,parm)
// }


// //获取项目VO列表
// export async function getAllActivityVoListApi(parm){
//   return await http.get("/api/activity/getAllActivityVoList",parm)
// }
// //获取所有已关联项目Project的活动ActivityVO列表
// export async function getAllSelectedActivityVoListApi(parm){
//   return await http.get("/api/activity/getAllSelectedActivityVoList",parm)
// }
// //获取项目列表
// export async function getActivityTitleListApi(parm){
//   return await http.get("/api/activity/activityTitleList",parm)
// }

// //新增项目
// export async function insertOrUpdateActivityApi(parm){
//   return await http.post("/api/activity",parm)
// }
// //新增项目
// export async function addActivityApi(parm){
//   return await http.post("/api/activity",parm)
// }
// //编辑项目
// export async function editActivityApi(parm){
//   return await http.put("/api/activity",parm)
// }
// //删除项目
// export async function deleteActivityApi(parm){
//   return await http.delete("/api/activity",parm)
// }

// home page

//获取累计问卷数量 1.1 累计问卷数量
export async function getAnswerAmountByDevUserIdApi(parm){
  return await http.get("/api/answer/getAnswerAmountByDevUserId",parm)
}
//获取新增问卷数量 1.2 累计参与门店
export async function getStoreAmountByDevUserIdApi( parm){
  return await http.get("/api/answer/getStoreAmountByDevUserId", parm)
}
//home page 1.3 当前管理员下，昨日新增问卷数量
export async function getLastdayAnswerAmountByDevUserIdApi( parm){
  return await http.get("/api/answer/getLastdayAnswerAmountByDevUserId", parm)
}
//获取昨日参与门店 1.4 当前管理员下，昨日参与门店
export async function getLastdayAnswerListStoreAmountByDevUserIdApi( parm){
  return await http.get("/api/answer/getLastdayAnswerListStoreAmountByDevUserId", parm)
}
//获取昨日参与门店 1.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
export async function getGroupLastdayAnswerListByDevUserIdApi( parm){
  return await http.get("/api/answer/getGroupLastdayAnswerListByDevUserId", parm)
}
//获取昨日参与门店 1.6 当前管理员下，昨日门店来源 storeTotalLastday
export async function getGroupLastdayAnswerListStoreAmountByDevUserIdApi( parm){
  return await http.get("/api/answer/getGroupLastdayAnswerListStoreAmountByDevUserId", parm)
}


//获取累计问卷数量 2.1 累计问卷数量
// export async function getAnswerListByAdminUserIdApi(parm){
//   return await http.get("/api/answer/getAnswerListByAdminUserId",parm)
// }
export async function getAnswerAmountByAdminUserIdApi(parm){
  return await http.get("/api/answer/getAnswerAmountByAdminUserId",parm)
}
//获取新增问卷数量 2.2 累计参与门店
export async function getStoreAmountByAdminUserIdApi( parm){
  return await http.get("/api/answer/getStoreAmountByAdminUserId", parm)
}
//home page 2.3 当前管理员下，昨日新增问卷数量
//获取累计参与门店
// export async function getLastdayAnswerListByAdminUserIdApi( parm){
//   return await http.get("/api/answer/getLastdayAnswerListByAdminUserId", parm)
// }
export async function getLastdayAnswerAmountByAdminUserIdApi( parm){
  return await http.get("/api/answer/getLastdayAnswerAmountByAdminUserId", parm)
}
//获取昨日参与门店 2.4 当前管理员下，昨日参与门店
// export async function getLastdayAnswerListStoreAmountByAdminUserIdApi( parm){
//   return await http.get("/api/answer/getLastdayAnswerListStoreAmountByAdminUserId", parm)
// }
export async function getLastdayAnswerListStoreAmountByAdminUserIdApi( parm){
  return await http.get("/api/answer/getLastdayAnswerListStoreAmountByAdminUserId", parm)
}
//获取昨日参与门店 2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
export async function getGroupLastdayAnswerListByAdminUserIdApi( parm){
  return await http.get("/api/answer/getGroupLastdayAnswerListByAdminUserId", parm)
}
//获取昨日参与门店 2.6 当前管理员下，昨日门店来源 storeTotalLastday
export async function getGroupLastdayAnswerListStoreAmountByAdminUserIdApi( parm){
  return await http.get("/api/answer/getGroupLastdayAnswerListStoreAmountByAdminUserId", parm)
}


//获取累计问卷数量 3.1 累计问卷数量
export async function getAnswerAmountByPartnerUserIdApi(parm){
  return await http.get("/api/answer/getAnswerAmountByPartnerUserId",parm)
}
//获取新增问卷数量 3.2 累计参与门店
export async function getStoreAmountByPartnerUserIdApi( parm){
  return await http.get("/api/answer/getStoreAmountByPartnerUserId", parm)
}
//home page 3.3 当前管理员下，昨日新增问卷数量
export async function getLastdayAnswerAmountByPartnerUserIdApi( parm){
  return await http.get("/api/answer/getLastdayAnswerAmountByPartnerUserId", parm)
}
//获取昨日参与门店 3.4 当前管理员下，昨日参与门店
export async function getLastdayAnswerListStoreAmountByPartnerUserIdApi( parm){
  return await http.get("/api/answer/getLastdayAnswerListStoreAmountByPartnerUserId", parm)
}
//获取昨日参与门店 3.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
export async function getGroupLastdayAnswerListByPartnerUserIdApi( parm){
  return await http.get("/api/answer/getGroupLastdayAnswerListByPartnerUserId", parm)
}
//获取昨日参与门店 3.6 当前管理员下，昨日门店来源 storeTotalLastday
export async function getGroupLastdayAnswerListStoreAmountByPartnerUserIdApi( parm){
  return await http.get("/api/answer/getGroupLastdayAnswerListStoreAmountByPartnerUserId", parm)
}


