import requestForCenter from '@/utils/requestForCenter.ts'
import { inputPassToFormPass } from '@/utils/md5.ts'
import { requestForManager } from '@/utils/requestForManager.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'

export function login(userAccount: string, password: string) {
  const urlParams = new URLSearchParams(window.location.search)
  const appId = urlParams.get('appId') ?? '' // 默认值为空字符串
  const redirectUri = urlParams.get('redirectUri') ?? '' // 默认值为空字符串
  const appServerUri = urlParams.get('appServerUri') ?? ''

  const formData = JSON.stringify({
    userAccount: userAccount,
    password: inputPassToFormPass(password),
  })

  return requestForCenter.post(
    `/authServer/login?appId=${appId}&redirectUri=${redirectUri}&appServerUri=${appServerUri}`,
    formData,
    {
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/json',
      },
    },
  )
}

export function register(
  userName: string,
  userAccount: string,
  password: string,
  confirmPassword: string,
) {
  const formData = JSON.stringify({
    userName: userName,
    userAccount: userAccount,
    password: inputPassToFormPass(password),
    confirmPassword: inputPassToFormPass(confirmPassword),
  })

  return requestForCenter.post('/authServer/register', formData, {
    headers: {
      'X-Requested-With': 'XMLHttpRequest',
      'Content-Type': 'application/json',
    },
  })
}

export async function getLoginStatus() {
  await requestForManager.get('/get/loginStatus', { withCredentials: true }).then((res) => {
    if (res.data.code === 20000) {
      if (res.data.data.userRole === "admin") {
        const loginUserStore = useLoginUserStore()
        loginUserStore.setLoginUser(res.data.data)
      }else {
        message.error('非管理员不可访问管理系统')
        logout()
      }
    } else {
      const redirectUri = window.location.origin + window.location.pathname
      window.location.href = res.data.data + '&redirectUri=' + redirectUri
    }
  })
}

export async function logout() {
  await requestForCenter.get("/authServer/logout")
}
