<template>
  <div>
    <a-row :gutter="16" style="margin-bottom:16px">
      <a-col :span="8">
        <a-card>
          <a-statistic title="今日检测总数" :value="data?.todayTotal ?? 0" :value-style="{color:'#1890ff'}" />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-card>
          <a-statistic title="今日合格率" :value="((data?.todayQualifiedRate ?? 0) * 100)" suffix="%" :precision="1" :value-style="{color:'#52c41a'}" />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-card>
          <a-statistic title="今日缺陷数" :value="data?.todayDefectCount ?? 0" :value-style="{color:'#ff4d4f'}" />
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="本月缺陷趋势">
          <v-chart :option="trendOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="缺陷分类分布">
          <v-chart :option="pieOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, GridComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { getDashboard } from '@/api/stats'
import type { DashboardVO } from '@/types'

use([CanvasRenderer, LineChart, PieChart, TitleComponent, TooltipComponent, GridComponent, LegendComponent])

const data = ref<DashboardVO | null>(null)

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: data.value?.monthTrend?.map(i => i.date) ?? [] },
  yAxis: { type: 'value', name: '合格率(%)', max: 100 },
  series: [{ name: '合格率', type: 'line', smooth: true, data: data.value?.monthTrend?.map(i => +(i.qualifiedRate * 100).toFixed(1)) ?? [] }]
}))

const pieOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [{ name: '缺陷分类', type: 'pie', radius: '60%', data: data.value?.categoryDistribution ?? [] }]
}))

onMounted(async () => {
  try {
    const res = await getDashboard()
    data.value = (res as any).data
  } catch {}
})
</script>
