import "ant-design-vue/dist/reset.css";

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import Antd from "ant-design-vue";

import { ConfigProvider } from 'ant-design-vue'
import 'dayjs/locale/zh-cn'
import dayjs from 'dayjs'


const app = createApp(App)
app.use(Antd);
app.use(createPinia())
app.use(router)
app.use(ConfigProvider)

// 设置 dayjs 语言（用于日期组件）
dayjs.locale('zh-cn')

app.mount('#app')
