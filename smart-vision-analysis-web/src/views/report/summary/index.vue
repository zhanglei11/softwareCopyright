<template>
  <div class="report-summary-page">
    <a-card :bordered="false" title="综合汇总报告">
      <!-- 查询条件 -->
      <a-form layout="inline" class="search-form" @finish="loadSummary" :model="{}">
        <a-form-item label="时间范围">
          <a-range-picker
            v-model:value="dateRange"
            format="YYYY-MM-DD"
            :placeholder="['开始日期', '结束日期']"
            style="width:240px"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit" :loading="loading">查询</a-button>
            <a-button @click="reset">重置</a-button>
            <a-button @click="exportExcel" :loading="exporting">导出 Excel</a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <a-spin :spinning="loading">
        <div v-if="summary">
          <!-- 统计卡片 -->
          <a-row :gutter="16" class="stat-row">
            <a-col :span="6">
              <a-statistic title="总任务数" :value="summary.totalTasks || 0" />
            </a-col>
            <a-col :span="6">
              <a-statistic title="总识别图片" :value="summary.totalImages || 0" />
            </a-col>
            <a-col :span="6">
              <a-statistic title="整体成功率" :value="((summary.successRate || 0) * 100).toFixed(1)" suffix="%" :value-style="{ color: '#3f8600' }" />
            </a-col>
            <a-col :span="6">
              <a-statistic title="平均置信度" :value="((summary.avgConfidence || 0) * 100).toFixed(1)" suffix="%" />
            </a-col>
          </a-row>

          <!-- 趋势图 -->
          <a-row :gutter="16" class="chart-row">
            <a-col :span="14">
              <a-card size="small" title="识别趋势（按日/周）">
                <v-chart :option="trendOption" style="height:300px" autoresize />
              </a-card>
            </a-col>
            <a-col :span="10">
              <a-card size="small" title="模型使用分布">
                <v-chart :option="modelPieOption" style="height:300px" autoresize />
              </a-card>
            </a-col>
          </a-row>

          <!-- 汇总表格 -->
          <a-card size="small" title="任务汇总明细" class="detail-card">
            <a-table
              :data-source="summary.taskSummaries || []"
              :columns="tableColumns"
              :pagination="{ pageSize: 8 }"
              row-key="taskId"
              size="small"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'successRate'">
                  <a-progress
                    :percent="Number(((record.successRate || 0) * 100).toFixed(1))"
                    size="small"
                    :stroke-color="record.successRate >= 0.9 ? '#52c41a' : '#faad14'"
                  />
                </template>
                <template v-if="column.key === 'avgConfidence'">
                  {{ ((record.avgConfidence || 0) * 100).toFixed(1) }}%
                </template>
              </template>
            </a-table>
          </a-card>
        </div>

        <a-empty v-else description="点击查询生成报告" />
      </a-spin>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'
import type { Dayjs } from 'dayjs'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { LineChart, PieChart } from 'echarts/charts'
import {
  GridComponent, TooltipComponent, TitleComponent, LegendComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { getSummaryReportApi } from '@/api/report/index'

use([LineChart, PieChart, GridComponent, TooltipComponent, TitleComponent, LegendComponent, CanvasRenderer])

const loading = ref(false)
const exporting = ref(false)
const dateRange = ref<[Dayjs, Dayjs] | null>(null)
const summary = ref<any>(null)

const tableColumns = [
  { title: '任务名称', dataIndex: 'taskName', key: 'taskName', ellipsis: true },
  { title: '使用模型', dataIndex: 'modelName', key: 'modelName' },
  { title: '图片数', dataIndex: 'totalImages', key: 'totalImages', width: 80 },
  { title: '成功率', key: 'successRate', width: 160 },
  { title: '平均置信度', key: 'avgConfidence', width: 110 },
  { title: '完成时间', dataIndex: 'finishTime', key: 'finishTime', width: 160 }
]

const trendOption = computed(() => {
  const trend: { date: string; count: number }[] = summary.value?.trend || []
  return {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: trend.map(t => t.date),
      axisLabel: { rotate: 30, fontSize: 11 }
    },
    yAxis: { type: 'value', name: '识别数量' },
    series: [{
      type: 'line',
      data: trend.map(t => t.count),
      smooth: true,
      areaStyle: { opacity: 0.15 },
      itemStyle: { color: '#1677ff' }
    }]
  }
})

const modelPieOption = computed(() => {
  const dist: Record<string, number> = summary.value?.modelDistribution || {}
  return {
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left', top: 'middle' },
    series: [{
      type: 'pie',
      radius: '60%',
      center: ['65%', '50%'],
      data: Object.entries(dist).map(([name, value]) => ({ name, value })),
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.3)' }
      }
    }]
  }
})

async function loadSummary() {
  loading.value = true
  try {
    const params: Record<string, string> = {}
    if (dateRange.value) {
      params.startDate = dateRange.value[0].format('YYYY-MM-DD')
      params.endDate = dateRange.value[1].format('YYYY-MM-DD')
    }
    const res = await getSummaryReportApi(params)
    summary.value = res.data?.data || res.data
  } catch {
    message.error('查询失败')
  } finally {
    loading.value = false
  }
}

function reset() {
  dateRange.value = null
  summary.value = null
}

async function exportExcel() {
  exporting.value = true
  try {
    const params: Record<string, string> = { format: 'excel' }
    if (dateRange.value) {
      params.startDate = dateRange.value[0].format('YYYY-MM-DD')
      params.endDate = dateRange.value[1].format('YYYY-MM-DD')
    }
    const res = await getSummaryReportApi({ ...params })
    const blob = new Blob([res.data])
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'summary_report.xlsx'
    a.click()
    URL.revokeObjectURL(url)
  } catch {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}
</script>

<style scoped lang="scss">
.report-summary-page {
  padding: 16px;
}

.search-form {
  margin-bottom: 20px;
}

.stat-row {
  margin-bottom: 20px;
}

.chart-row {
  margin-bottom: 20px;
}

.detail-card {
  margin-top: 4px;
}
</style>
