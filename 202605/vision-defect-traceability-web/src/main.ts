import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import { vPermission } from './directives/permission'
import './styles/index.scss'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)
app.use(Antd)
app.directive('permission', vPermission)

app.mount('#app')
