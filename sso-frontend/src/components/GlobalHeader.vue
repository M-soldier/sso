<template>
  <div id="global-header">
    <a-row :wrap="false">
      <a-col flex="auto">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.jpg" alt="logo"/>
          <div class="title">单点登录管理系统</div>
        </div>
      </a-col>
      <a-col flex="60px">
        <div class="login-status">
            <a-dropdown>
              <ASpace>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
              </ASpace>
              <template #overlay>
                <div>
                  <a-card hoverable style="width: 240px">
                    <template #actions >
                      <edit-outlined key="edit" />
                      <logout-outlined @click="doLogout" />
                    </template>
                    <a-card-meta :title="loginUserStore.loginUser.userAccount" :description="loginUserStore.loginUser.userName">
                      <template #avatar>
                        <a-avatar :src="loginUserStore.loginUser.userAvatar"/>
                      </template>
                    </a-card-meta>
                  </a-card>
                </div>
              </template>
            </a-dropdown>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { LogoutOutlined, EditOutlined } from '@ant-design/icons-vue';
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import {logout} from '@/api/user.ts'

const loginUserStore = useLoginUserStore()

const doLogout = async () => {
  await logout()
  window.location.reload()
}

</script>

<style scoped>
#global-header .title-bar {
  display: flex;
  align-items: center;
}
.logo {
  margin-top: 6px;
  height: 30px;
}
.title {
  color: #214673;
  font-size: 32px;
  font-weight: 800;
  margin-left: 12px;
}
</style>
