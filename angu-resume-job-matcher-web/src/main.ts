import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import dayjs from 'dayjs'
import zhCn from 'dayjs/locale/zh-cn'
import { use as useEcharts } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, FunnelChart, BarChart } from 'echarts/charts'
import { TooltipComponent, LegendComponent, GridComponent, TitleComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import App from './App.vue'
import router from './router'
import { pinia } from './store'
import i18n from './i18n'
import permissionDirective from './directives/permission'
import 'ant-design-vue/dist/reset.css'
import './styles/index.scss'

useEcharts([CanvasRenderer, PieChart, FunnelChart, BarChart, TooltipComponent, LegendComponent, GridComponent, TitleComponent])
dayjs.locale(zhCn)

const app = createApp(App)
app.use(Antd)
app.use(pinia)
app.use(router)
app.use(i18n)
app.directive('permission', permissionDirective)
app.component('VChart', VChart)
app.mount('#app')
