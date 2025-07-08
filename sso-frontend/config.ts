// // config.js
// const apiUrls = {
//     development: 'http://sso.authentication.com:8080',
//     production: 'http://sso.authentication.com:8080',
//     // staging: 'http://sso.authentication.com:8080',
// };
//
// export default apiUrls;

// export const API_BASE = process.env.VITE_APP_API_BASE;
// console.log('当前环境：', process.env.NODE_ENV);
// console.log('API地址：', API_BASE);

// src/config/config.ts

export const API_SSO_CENTER = import.meta.env.VITE_APP_API_SSO_CENTER;
export const API_SSO_MANAGER = import.meta.env.VITE_APP_API_SSO_MANAGER;

if (!API_SSO_CENTER) {
  throw new Error("VITE_APP_API_SSO_CENTER is not defined in your environment variables.");
}

if (!API_SSO_MANAGER) {
  throw new Error("VITE_APP_API_SSO_MANAGER is not defined in your environment variables.")
}

console.log('当前环境：', import.meta.env.VITE_ENV || 'development');
console.log('SSO认证中心API地址：', API_SSO_CENTER);
console.log('SSO管理中心API地址：', API_SSO_CENTER);

export const config = {
  ssoCenterUrl: API_SSO_CENTER,
  ssoManagerUrl: API_SSO_MANAGER,
  env: import.meta.env.VITE_ENV || 'development',
};
