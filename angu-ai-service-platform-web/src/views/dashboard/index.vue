<template>
  <div class="page-container">
    <a-row :gutter="16" style="margin-bottom: 16px">
      <a-col :span="6" v-for="stat in statCards" :key="stat.title">
        <a-card>
          <a-statistic
            :title="stat.title"
            :value="stat.value"
            :prefix="h(stat.icon)"
            :value-style="{ color: stat.color }"
          />
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="16">
        <a-card title="场景调用趋势（近7天）" :loading="chartLoading">
          <v-chart class="chart" :option="lineChartOption" autoresize />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-card title="场景调用分布" :loading="chartLoading">
          <v-chart class="chart" :option="pieChartOption" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { h, ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, TitleComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { MessageOutlined, RobotOutlined, DatabaseOutlined, UserOutlined } from '@ant-design/icons-vue'

use([CanvasRenderer, LineChart, PieChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent])

const chartLoading = ref(false)

const statCards = ref([
  { title: '今日调用次数', value: 1284, icon: MessageOutlined, color: '#1677ff' },
  { title: 'AI场景数量', value: 32, icon: RobotOutlined, color: '#52c41a' },
  { title: '知识库数量', value: 8, icon: DatabaseOutlined, color: '#fa8c16' },
  { title: '系统用户数', value: 56, icon: UserOutlined, color: '#722ed1' },
])

const lineChartOption = ref({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] },
  yAxis: { type: 'value' },
  series: [{ name: '调用次数', type: 'line', smooth: true, data: [820, 932, 901, 934, 1290, 1330, 1520], itemStyle: { color: '#1677ff' } }],
})

const pieChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [{
    name: '场景分布',
    type: 'pie',
    radius: ['40%', '70%'],
    data: [
      { value: 435, name: '智能对话' },
      { value: 310, name: '文档分析' },
      { value: 274, name: '内容生成' },
      { value: 200, name: '知识检索' },
    ],
  }],
})
</script>

<style scoped>
.chart { height: 300px; }
</style>
