<template>
  <div id="global-header">
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
        v-model:selectedKeys="current"
        mode="inline"
        style="height: 100%"
        @click="doMenuClick"
      >
        <a-menu-item key="/manager/home">
          <HomeOutlined />
          <span> 首页 </span>
        </a-menu-item>
        <a-menu-item key="/manager/userManage">
          <user-outlined />
          <span> 用户管理 </span>
        </a-menu-item>
        <a-menu-item key="/manager/appManage">
          <AppstoreOutlined />
          <span> 应用管理 </span>
        </a-menu-item>
        <a-menu-item key="/manager/loginUserManage">
          <TeamOutlined />
          <span> 登录用户管理 </span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
  </div>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router'
import { HomeOutlined, UserOutlined, AppstoreOutlined, TeamOutlined } from '@ant-design/icons-vue'
import { ref, onMounted  } from 'vue'
const current = ref<string[]>([]);

const route = useRoute();
const router = useRouter();
// 菜单点击事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  });
};

// 初始化设置一次（刷新时也能拿到）
onMounted(() => {
  current.value = [route.path]
})

router.afterEach((to) => {
  console.log(to.path);
  current.value = [to.path];
})


</script>

<style scoped></style>
