import axios from 'axios';
import { API_SSO_MANAGER } from '../../config';
// import { message } from 'ant-design-vue'

export const requestForManager = axios.create({
  baseURL: API_SSO_MANAGER,
  timeout: 5000,
  withCredentials: true
});

export const request = axios.create({
  baseURL: API_SSO_MANAGER,
  timeout: 5000,
  withCredentials: true
});

// 全局请求拦截器
// requestForManager.interceptors.requestForManager.use(
//   function (config) {
//     // Do something before requestForManager is sent
//     return config
//   },
//   function (error) {
//     // Do something with requestForManager error
//     return Promise.reject(error)
//   },
// )

// 全局响应拦截器
// requestForManager.interceptors.response.use(
//   function (response) {
//     const { data } = response
//     // 未登录
//     if (data.code === 40100) {
//       // 不是获取用户信息的请求，并且用户目前不是已经在用户登录页面，则跳转到登录页面
//       if (
//         !response.requestForManager.responseURL.includes('user/get/login') &&
//         !window.location.pathname.includes('/user/login')
//       ) {
//         message.warning('请先登录')
//         window.location.href = `/user/login?redirect=${window.location.href}`
//       }
//     }
//     return response
//   },
//   function (error) {
//     // Any status codes that falls outside the range of 2xx cause this function to trigger
//     // Do something with response error
//     return Promise.reject(error)
//   },
// )


export default { requestForManager, request };
