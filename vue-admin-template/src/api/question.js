import http from '@/utils/http'
//保存试题
export async function saveQuestionApi(paperId,parm){
    return await http.post("/api/question/saveQuestion/" + paperId ,parm)
}
//查询试题列表
export async function getQuestionListApi(parm){
    return await http.get("/api/question/getQuestionList",parm)
}
