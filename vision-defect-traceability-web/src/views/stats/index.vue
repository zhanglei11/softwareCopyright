<template>
  <a-card title="统计报表">
    <a-form layout="inline" :model="query" style="margin-bottom:16px">
      <a-form-item label="开始日期">
        <a-date-picker v-model:value="query.startDate" value-format="YYYY-MM-DD" />
      </a-form-item>
      <a-form-item label="结束日期">
        <a-date-picker v-model:value="query.endDate" value-format="YYYY-MM-DD" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="loadData"><SearchOutlined />查询</a-button>
      </a-form-item>
    </a-form>
    <a-spin :spinning="loading">
      <a-descriptions bordered :column="3" v-if="data" style="margin-bottom:16px">
        <a-descriptions-item label="检测总数">{{ data.totalCount }}</a-descriptions-item>
        <a-descriptions-item label="平均合格率">{{ data.avgQualifiedRate?.toFixed(1) }}%</a-descriptions-item>
      </a-descriptions>
      <v-chart v-if="chartOption" :option="chartOption" style="height:320px" autoresize />
    </a-spin>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, GridComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { SearchOutlined } from '@ant-design/icons-vue'
import { getTrend } from '@/api/stats'
use([CanvasRenderer, LineChart, TitleComponent, TooltipComponent, GridComponent, LegendComponent])
const loading = ref(false)
const data = ref<any>(null)
const query = reactive({ startDate: '', endDate: '' })
const chartOption = computed(() => {
  if (!data.value?.series?.length) return null
  return {
    tooltip: { trigger: 'axis' },
    legend: { data: ['检测数量', '合格率'] },
    xAxis: { type: 'category', data: data.value.series.map((i: any) => i.date) },
    yAxis: [{ type: 'value', name: '检测数量' }, { type: 'value', name: '合格率(%)', max: 100, axisLabel: { formatter: '{value}%' } }],
    series: [
      { name: '检测数量', type: 'line', smooth: true, data: data.value.series.map((i: any) => i.totalCount) },
      { name: '合格率', type: 'line', smooth: true, yAxisIndex: 1, data: data.value.series.map((i: any) => +(i.qualifiedRate * 100).toFixed(1)) }
    ]
  }
})
const loadData = async () => {
  if (!query.startDate || !query.endDate) return
  loading.value = true
  try { const res = await getTrend(query); data.value = (res as any).data } catch {} finally { loading.value = false }
}
</script>
