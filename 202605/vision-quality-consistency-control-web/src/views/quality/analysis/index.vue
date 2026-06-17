<template>
  <div>
    <a-card title="偏差分析报告" :bordered="false" style="margin-bottom:16px">
      <a-form layout="inline" :model="query" @finish="loadReport">
        <a-form-item label="选择检测任务">
          <a-select v-model:value="query.taskId" :options="taskOptions" placeholder="选择任务" style="width:280px" show-search />
        </a-form-item>
        <a-form-item><a-button type="primary" html-type="submit" :disabled="!query.taskId">查看报告</a-button></a-form-item>
      </a-form>
      <div v-if="report" style="margin-top:24px">
        <a-row :gutter="16" style="margin-bottom:16px">
          <a-col :span="8">
            <a-statistic title="不合格影像占比" :value="report.defectRate ?? 0" suffix="%" :value-style="{color:'#ff4d4f'}" />
          </a-col>
          <a-col :span="8">
            <a-statistic title="偏差最大指标" :value="report.topDeviationMetric ?? '-'" />
          </a-col>
        </a-row>
        <a-table :columns="metricCols" :data-source="report.metricStats||[]" :pagination="false" row-key="metricId" size="small" />
        <div v-if="report.suggestion" style="margin-top:16px; padding:12px; background:#f6ffed; border-radius:6px; border:1px solid #b7eb8f">
          <b>改善建议：</b>{{ report.suggestion }}
        </div>
      </div>
      <a-empty v-else description="请选择检测任务查看报告" />
    </a-card>

    <a-card title="多维度对比分析" :bordered="false">
      <a-row :gutter="16" style="margin-bottom:16px">
        <a-col :span="12">
          <a-select v-model:value="compareTaskIds" mode="multiple" :options="taskOptions" placeholder="选择2个或多个任务对比" style="width:100%" />
        </a-col>
        <a-col :span="4">
          <a-button type="primary" @click="loadTrend">对比分析</a-button>
        </a-col>
      </a-row>
      <v-chart v-if="trendOption.series?.length" :option="trendOption" style="height:300px" autoresize />
      <a-empty v-else description="选择多个任务后点击对比分析" />
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { BarChart, LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { analysisApi } from '@/api/quality/analysis'
import { taskApi } from '@/api/quality/task'

use([BarChart, LineChart, GridComponent, TooltipComponent, LegendComponent, CanvasRenderer])

const query = reactive({ taskId: undefined as number | undefined })
const report = ref<any>(null)
const taskOptions = ref<any[]>([])
const compareTaskIds = ref<number[]>([])
const metricCols = [
  { title: '指标名称', dataIndex: 'metricName' },
  { title: '平均值', dataIndex: 'avgValue' },
  { title: '达标比例', dataIndex: 'passRate', customRender: ({ text }: any) => text != null ? text + '%' : '-' },
  { title: '是否达标', dataIndex: 'passed', customRender: ({ text }: any) => text ? '✓' : '✗' },
]
const trendOption = ref<any>({ series: [] })

async function loadReport() {
  if (!query.taskId) return
  const res: any = await analysisApi.taskReport(query.taskId)
  report.value = res.data
}

async function loadTrend() {
  if (compareTaskIds.value.length < 2) return
  const res: any = await analysisApi.trend({ taskIds: compareTaskIds.value.join(',') })
  const d = res.data || {}
  trendOption.value = {
    tooltip: { trigger: 'axis' },
    legend: {},
    xAxis: { type: 'category', data: d.metrics || [] },
    yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
    series: (d.tasks || []).map((t: any) => ({ name: t.taskName, type: 'bar', data: t.values })),
  }
}

onMounted(async () => {
  const res: any = await taskApi.list({ status: 3, pageSize: 100 })
  taskOptions.value = (res.data?.rows || res.data?.list || res.data?.records || []).map((t: any) => ({ label: t.taskName, value: t.id }))
})
</script>
