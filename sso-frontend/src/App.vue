<script setup lang="ts">
import { RouterView } from 'vue-router'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
</script>

<template>
  <a-config-provider :locale="zhCN">
    <header>
      <div id="app">
        <RouterView />
      </div>
    </header>
  </a-config-provider>
</template>

<style scoped>

</style>
