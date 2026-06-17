import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import permissionDirective from './directives/permission'
import './styles/index.scss'
import './styles/nprogress.scss'

const app = createApp(App)
app.use(createPinia()).use(router).use(Antd).use(i18n).use(permissionDirective)
app.mount('#app')
