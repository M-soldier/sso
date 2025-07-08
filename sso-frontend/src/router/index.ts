import { createRouter, createWebHistory } from 'vue-router';
import UserManagePage from '@/views/admin/UserManagePage.vue';
import HomePage from '@/views/home/HomePage.vue';
import APPManagePage from '@/views/app/APPManagePage.vue';
import LoginUserManagePage from '@/views/admin/LoginUserManagePage.vue';
import { getLoginStatus, logout } from '@/api/user.ts'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: '登录页面',
      component: () => import('@/views/loginAndRegister/UserLoginAndRegisterPage.vue'),
    },
    {
      path: '/manager',
      component: () => import('@/layouts/BasicLayout.vue'),
      children: [
        {
          path: '/manager/home',
          name: '首页',
          component: HomePage,
        },
        {
          path: '/manager/userManage',
          name: '用户管理',
          component: UserManagePage,
        },
        {
          path: '/manager/appManage',
          name: '应用管理',
          component: APPManagePage,
        },
        {
          path: '/manager/loginUserManage',
          name: '登录用户管理',
          component: LoginUserManagePage,
        },
      ]
    }
  ],
})

// 全局前置守卫
router.beforeEach( async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  if (to.path === '/login') {
    if (!to.query.redirectUri || !to.query.appId) {
      // 没有参数，禁止访问
      message.error('不可直接访问登录页面')
      return next(false) // 阻止导航
    }
  }
  if (to.path.startsWith('/manager')) {
    await getLoginStatus();
    console.log(loginUserStore.loginUser.userRole === undefined)
    if (loginUserStore.loginUser.userRole === undefined) {
      return next(false) // 阻止导航
    }
  }
  next() // 放行
})

export default router
