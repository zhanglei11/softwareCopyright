<template>
  <div class="page-container">
    <a-row :gutter="16" style="margin-bottom:16px">
      <a-col :span="6" v-for="s in stats" :key="s.title">
        <a-card><a-statistic :title="s.title" :value="s.value" :prefix="h(s.icon)" :value-style="{color:s.color}" /></a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="识别任务状态分布">
          <v-chart class="chart" :option="pieOption" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="近7日识别任务趋势">
          <v-chart class="chart" :option="lineOption" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { h, ref } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { FileImageOutlined, PlayCircleOutlined, EyeOutlined, RobotOutlined } from '@ant-design/icons-vue'
use([CanvasRenderer, PieChart, LineChart, GridComponent, TooltipComponent, LegendComponent])
const stats = ref([
  { title: '影像总数', value: 3842, icon: FileImageOutlined, color: '#1677ff' },
  { title: '识别任务', value: 127, icon: PlayCircleOutlined, color: '#52c41a' },
  { title: '待审核结果', value: 48, icon: EyeOutlined, color: '#fa8c16' },
  { title: '模型版本', value: 12, icon: RobotOutlined, color: '#722ed1' },
])
const pieOption = ref({ tooltip: { trigger: 'item' }, legend: { bottom: 0 },
  series: [{ type: 'pie', radius: ['40%','70%'], data: [
    { value: 45, name: '已完成' }, { value: 8, name: '识别中' }, { value: 12, name: '待提交' }, { value: 3, name: '失败' }
  ]}] })
const lineOption = ref({ tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: ['周一','周二','周三','周四','周五','周六','周日'] },
  yAxis: { type: 'value' },
  series: [{ name: '任务数', type: 'line', smooth: true, data: [12,18,15,22,19,8,5], itemStyle: { color: '#1677ff' } }] })
</script>
<style scoped>.chart { height:280px; }</style>
