<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <a-row :gutter="16" style="margin-bottom:16px">
      <a-col :span="6" v-for="card in statCards" :key="card.label">
        <a-card :bordered="false" style="border-radius:8px">
          <a-statistic :title="card.label" :value="card.value" :value-style="{ color: card.color }" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 图表区 -->
    <a-row :gutter="16">
      <a-col :span="14">
        <a-card title="采集趋势" :bordered="false" style="border-radius:8px;margin-bottom:16px">
          <v-chart :option="trendOption" style="height:300px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="10">
        <a-card title="文件类型分布" :bordered="false" style="border-radius:8px;margin-bottom:16px">
          <v-chart :option="fileTypeOption" style="height:300px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="数据源贡献" :bordered="false" style="border-radius:8px">
          <v-chart :option="datasourceOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="处理任务汇总" :bordered="false" style="border-radius:8px">
          <v-chart :option="processOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, TitleComponent } from 'echarts/components'
import { getOverview, getIngestTrend, getProcessSummary, getDatasourceContribution, getFileTypeDistribution } from '@/api/stats'
import type { OverviewDTO } from '@/types'

use([CanvasRenderer, LineChart, PieChart, BarChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent])

const overview = ref<OverviewDTO>({ datasourceCount: 0, todayIngestCount: 0, todayIngestSize: 0, todayProcessCount: 0, storageUsageRate: 0, ingestTaskCount: 0, processTaskCount: 0 })
const trendData = ref<{ dates: string[]; counts: number[]; sizes: number[] }>({ dates: [], counts: [], sizes: [] })
const fileTypeData = ref<{ name: string; value: number }[]>([])
const datasourceData = ref<{ name: string; value: number }[]>([])
const processData = ref<{ name: string; value: number }[]>([])

const statCards = computed(() => [
  { label: '数据源数量', value: overview.value.datasourceCount, color: '#1677ff' },
  { label: '今日采集量', value: overview.value.todayIngestCount, color: '#52c41a' },
  { label: '处理任务数', value: overview.value.processTaskCount, color: '#faad14' },
  { label: '采集任务数', value: overview.value.ingestTaskCount, color: '#722ed1' },
])

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  legend: { data: ['采集数量'] },
  xAxis: { type: 'category', data: trendData.value.dates },
  yAxis: { type: 'value' },
  series: [{ name: '采集数量', type: 'line', smooth: true, data: trendData.value.counts, areaStyle: { opacity: 0.3 } }],
}))

const fileTypeOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [{ type: 'pie', radius: ['40%', '70%'], data: fileTypeData.value, emphasis: { itemStyle: { shadowBlur: 10 } } }],
}))

const datasourceOption = computed(() => ({
  tooltip: { trigger: 'item' },
  series: [{ type: 'pie', radius: '65%', data: datasourceData.value }],
}))

const processOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: processData.value.map(i => i.name) },
  yAxis: { type: 'value' },
  series: [{ type: 'bar', data: processData.value.map(i => i.value), itemStyle: { color: '#1677ff' } }],
}))

onMounted(async () => {
  try {
    const [ovRes, trendRes, ftRes, dsRes, psRes] = await Promise.all([
      getOverview(), getIngestTrend(), getFileTypeDistribution(), getDatasourceContribution(), getProcessSummary(),
    ])
    overview.value = ovRes.data
    trendData.value = trendRes.data
    fileTypeData.value = ftRes.data
    datasourceData.value = dsRes.data
    // process/summary returns { totalCount, activeCount }, convert to chart array
    const ps = psRes.data
    if (Array.isArray(ps)) {
      processData.value = ps
    } else {
      processData.value = [
        { name: '总任务数', value: ps.totalCount ?? 0 },
        { name: '运行中', value: ps.activeCount ?? 0 },
      ]
    }
  } catch { /* ignore */ }
})
</script>
