<script lang="ts" setup>
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from '@/components/GlobalSider.vue'



</script>

<template>
  <div id="basic-layout">
    <a-layout>
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout class="layout-content">
        <a-layout-sider class="slider" breakpoint="lg" collapsed-width="0">
          <GlobalSider/>
        </a-layout-sider>
        <a-layout-content class="content">
          <RouterView/>
        </a-layout-content>
      </a-layout>
      <a-layout-footer class="footer">
        版权所有 © MSJ
      </a-layout-footer>
    </a-layout>
  </div>
</template>


<style scoped>
#basic-layout .header {
  padding-inline: 20px;
  color: unset;
  background: white;
  height: 64px;
  border-bottom: 2px solid #efefef;
}


#basic-layout .slider {
  width: 200px;
  top: 64px;
  bottom: 48px;
  border-bottom: 2px solid #efefef;
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to right, #fefefe, #fff);
}

#basic-layout .content {
  margin-left: 200px;
  height: calc(100vh - 112px);
}

#basic-layout .footer {
  background: #fff;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  height: 48px;
}
</style>
