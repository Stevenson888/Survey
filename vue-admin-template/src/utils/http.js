import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import ElementUI from 'element-ui';
import store from '@/store'
import { getToken } from '@/utils/auth'
import qs from 'qs'

// 环境的切换
// if (process.env.VUE_APP_BASE_API == 'development') {
//   axios.defaults.baseURL = '/api';
// } else if (process.env.VUE_APP_BASE_API == 'test') {
//   axios.defaults.baseURL = '';
// } else if (process.env.VUE_APP_BASE_API == 'production') {
//   axios.defaults.baseURL = 'http://api.123dailu.com/';
// }

// 环境的切换
// if (process.env.VUE_APP_BASE_API == 'development') {
// } else if (process.env.VUE_APP_BASE_API == 'test') {
// } else if (process.env.VUE_APP_BASE_API == 'production') {
// }

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000000 // request timeout
})

// request interceptor
//请求发送之前的拦截器
service.interceptors.request.use(
  config => {
    // do something before request is sent
    //判断token是否存在，如果存在，加到请求的头部
    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
//请求返回之后的拦截器
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // if the custom code is not 20000, it is judged as an error.
    //默认是20000,需要改为自己实际的状态码
    if (res.code !== 200) {

      // UserController: throw new ServiceException( Constants.CODE_600 , "用户不存在");
      if (res.code === 600) {
        console.log(" error code 600 res", res)
        ElementUI.Message({
            message: res.msg,
            type: 'error',
            duration: 5 * 1000
        });
        return
      }
      // UserController: throw new ServiceException( Constants.CODE_601 , "密码错误");
      if (res.code === 601) {
        console.log(" error code 601 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        });
        return
      }
      // UserController: throw new ServiceException( Constants.CODE_607 , "此用户未激活");
      if (res.code === 607) {
        console.log(" error code 607 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        });
        return
      }
      // UserController: throw new ServiceException( Constants.CODE_603 , "此手机号已注册");
      if (res.code === 603) {
        console.log(" error code 603 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        });
        return
      }
      // UserController: throw new ServiceException( Constants.CODE_604 , "此用户名已注册");
      if (res.code === 604) {
        console.log(" error code 604 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        });
        return
      }
      // UserController: throw new ServiceException( Constants.CODE_605 , "该手机号对应用户未注册小程序，请先注册小程序");
      if (res.code === 605) {
        console.log(" error code 605 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        });
        return
      }
      // StoreController: throw new ServiceException( Constants.CODE_606 , 门店导入时字段为空);
      if (res.code === 606) {
        console.log(" error code 606 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 100 * 1000
        });
        return
      }
      // UserController: throw new ServiceException( Constants.CODE_700 , "licenseNo专卖证号已存在");
      if (res.code === 700) {
        console.log(" error code 700 res", res)
        ElementUI.Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        });
        return
      }

      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      //token过期处理
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)


//请求方法 支持 restful api
const http = {
  post(url, params) {
    return service.post(url, params, {
      transformRequest: [(params) => {
        return JSON.stringify(params)
      }],
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },
  put(url, params) {
    return service.put(url, params, {
      transformRequest: [(params) => {
        return JSON.stringify(params)
      }],
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },
  //parm =>  {id:10}
  // http://local host:8089/api/user?id=10
  get(url, params) {
    return service.get(url, {
      params: params,
      paramsSerializer: (params) => {
        return qs.stringify(params)
      }
      ,headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },
  //parm =>  {id:10}
  // http://local host:8089/api/user/10
  getRestApi(url, params) {
    let _params
    if (Object.is(params, undefined || null)) {
      _params = ''
    } else {
      _params = '/'
      for (const key in params) {
        // console.log(key)
        // console.log(params[key])
        // eslint-disable-next-line no-prototype-builtins
        if (params.hasOwnProperty(key) && params[key] !== null && params[key] !== '') {
          _params += `${params[key]}/`
        }
      }
      //去掉参数最后一位?
      _params = _params.substr(0, _params.length - 1)
    }
    // console.log(_params)
    if (_params) {
      return service.get(`${url}${_params}`
        ,{headers:{'Content-Type': 'multipart/form-data'} }
      )
    } else {
      return service.get(url  ,{headers:{'Content-Type': 'multipart/form-data'} })
    }
  },

  //parm =>  {id:6027446903321395}
  // http://local host:8089/api/store/6027446903321395
  getLongIdRestApi(url, params) {
    let _params
    if (Object.is(params, undefined || null)) {
      _params = ''
    } else {
      _params = '/'
      for (const key in params) {
        // console.log(key)
        // console.log(params[key])
        // eslint-disable-next-line no-prototype-builtins
        if (params.hasOwnProperty(key) && params[key] !== null && params[key] !== '') {
          _params += `${params[key]}`
        }
      }
      //去掉参数最后一位?
      _params = _params.substr(0, _params.length )
    }
    // console.log(_params)
    if (_params) {
      return service.get(`${url}${_params}`
        ,{headers:{'Content-Type': 'multipart/form-data'} }
      )
    } else {
      return service.get(url  ,{headers:{'Content-Type': 'multipart/form-data'} })
    }
  },

  getWithoutParams(url) {
    return service.get(url, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },
  // return service.get(url  ,{headers:{'Content-Type': 'multipart/form-data'} })

  //parm =>  {id:6027446903321395}
  // http://local host:8089/api/store/6027446903321395
  updateLongIdRestApi(url, params) {
    let _params
    if (Object.is(params, undefined || null)) {
      _params = ''
    } else {
      _params = '/'
      for (const key in params) {
        // console.log(key)
        // console.log(params[key])
        // eslint-disable-next-line no-prototype-builtins
        if (params.hasOwnProperty(key) && params[key] !== null && params[key] !== '') {
          _params += `${params[key]}`
        }
      }
      //去掉参数最后一位?
      _params = _params.substr(0, _params.length )
    }
    // console.log(_params)
    if (_params) {
      return service.put(`${url}${_params}`
        ,{headers:{'Content-Type': 'multipart/form-data'} }
      )
    } else {
      return service.put(url  ,{headers:{'Content-Type': 'multipart/form-data'} })
    }
  },


  //parm =>  {id:10}
  // http://local host:8089/api/user/10
  delete(url, params) {
    let _params
    if (Object.is(params, undefined || null)) {
      _params = ''
    } else {
      _params = '/'
      for (const key in params) {
        // eslint-disable-next-line no-prototype-builtins
        if (params.hasOwnProperty(key) && params[key] !== null && params[key] !== '') {
          _params += `${params[key]}/`
        }
      }
      //去掉参数最后一位?
      _params = _params.substr(0, _params.length - 1)
    }
    if (_params) {
      return service.delete(`${url}${_params}`).catch(err => {
        // message.error(err.msg)
        return Promise.reject(err)
      })
    } else {
      return service.delete(url).catch(err => {
        // message.error(err.msg)
        return Promise.reject(err)
      })
    }
  },
  upload(url, params) {
    return service.post(url, params, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}
export default http
