import request from '@/utils/request'
import http from '@/utils/http'

export async function getProvinceNameByProvinceCodeApi(parm){
  return await http.getLongIdRestApi("/api/chinaRegion/getProvinceNameByProvinceCode",parm)
}
export async function getCityNameByCityCodeApi(parm){
  return await http.getLongIdRestApi("/api/chinaRegion/getCityNameByCityCode",parm)
}
export async function getDistrictNameByDistrictCodeApi(parm){
  return await http.getLongIdRestApi("/api/chinaRegion/getDistrictNameByDistrictCode",parm)
}

