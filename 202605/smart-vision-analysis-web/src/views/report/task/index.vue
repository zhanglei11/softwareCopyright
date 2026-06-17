<template>
  <div class="report-task-page">
    <a-card :bordered="false" title="任务分析报告">
      <a-form layout="inline" class="search-form">
        <a-form-item label="选择任务">
          <a-select
            v-model:value="selectedTaskId"
            style="width:280px"
            placeholder="请选择已完成的任务"
            :loading="taskLoading"
            show-search
            :filter-option="false"
            @search="searchTask"
            @change="loadReport"
            allow-clear
          >
            <a-select-option v-for="t in taskOptions" :key="t.id" :value="t.id">
              {{ t.taskName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-if="selectedTaskId">
          <a-space>
            <a-button @click="exportReport('excel')" :loading="exporting">
              导出 Excel
            </a-button>
            <a-button @click="exportReport('pdf')" :loading="exporting">
              导出 PDF
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <a-spin :spinning="reportLoading">
        <div v-if="report">
          <!-- 统计卡片 -->
          <a-row :gutter="16" class="stat-cards">
            <a-col :span="4">
              <a-statistic title="总图片数" :value="report.totalImages" />
            </a-col>
            <a-col :span="4">
              <a-statistic title="识别成功" :value="report.successCount" :value-style="{ color: '#3f8600' }" />
            </a-col>
            <a-col :span="4">
              <a-statistic title="识别失败" :value="report.failCount" :value-style="{ color: '#cf1322' }" />
            </a-col>
            <a-col :span="4">
              <a-statistic title="平均置信度" :value="((report.avgConfidence || 0) * 100).toFixed(1)" suffix="%" />
            </a-col>
            <a-col :span="4">
              <a-statistic title="最低置信度" :value="((report.minConfidence || 0) * 100).toFixed(1)" suffix="%" :value-style="{ color: '#cf1322' }" />
            </a-col>
            <a-col :span="4">
              <a-statistic title="最高置信度" :value="((report.maxConfidence || 0) * 100).toFixed(1)" suffix="%" :value-style="{ color: '#3f8600' }" />
            </a-col>
          </a-row>

          <!-- 图表区 -->
          <a-row :gutter="16" class="chart-row">
            <a-col :span="12">
              <a-card size="small" title="置信度分布">
                <v-chart :option="confidenceOption" style="height:280px" autoresize />
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card size="small" title="分类识别数量">
                <v-chart :option="categoryOption" style="height:280px" autoresize />
              </a-card>
            </a-col>
          </a-row>

          <!-- 低置信度图片列表 -->
          <a-card size="small" title="低置信度图片列表" class="low-conf-card">
            <a-table
              :data-source="report.lowConfidenceImages || []"
              :columns="lowConfColumns"
              :pagination="{ pageSize: 5 }"
              row-key="resultId"
              size="small"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'confidence'">
                  <a-tag color="orange">{{ ((record.confidence || 0) * 100).toFixed(1) }}%</a-tag>
                </template>
                <template v-if="column.key === 'action'">
                  <a-button type="link" size="small" @click="goAnnotation(record.resultId)">
                    查看标注
                  </a-button>
                </template>
              </template>
            </a-table>
          </a-card>
        </div>

        <a-empty v-else description="请选择任务查看报告" />
      </a-spin>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, TitleComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { getTaskListApi } from '@/api/task/index'
import { getTaskReportApi, exportTaskReportApi } from '@/api/report/index'
import type { TaskItem } from '@/types/index'

use([BarChart, GridComponent, TooltipComponent, TitleComponent, CanvasRenderer])

const router = useRouter()

const taskLoading = ref(false)
const reportLoading = ref(false)
const exporting = ref(false)
const selectedTaskId = ref<number | undefined>()
const taskOptions = ref<TaskItem[]>([])
const report = ref<any>(null)

const lowConfColumns = [
  { title: '图片编号', dataIndex: 'imageNo', key: 'imageNo' },
  { title: '文件名', dataIndex: 'fileName', key: 'fileName', ellipsis: true },
  { title: '置信度', key: 'confidence', width: 100 },
  { title: '操作', key: 'action', width: 90 }
]

const confidenceOption = computed(() => {
  const dist: Record<string, number> = report.value?.confidenceDistribution || {}
  const keys = Object.keys(dist).sort()
  return {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: keys, axisLabel: { fontSize: 11 } },
    yAxis: { type: 'value', name: '数量' },
    series: [{
      type: 'bar',
      data: keys.map(k => dist[k]),
      itemStyle: { color: '#1677ff' },
      barMaxWidth: 40
    }]
  }
})

const categoryOption = computed(() => {
  const cats: Record<string, number> = report.value?.categoryCount || {}
  const keys = Object.keys(cats)
  return {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: keys,
      axisLabel: { rotate: 30, fontSize: 11 }
    },
    yAxis: { type: 'value', name: '识别数量' },
    series: [{
      type: 'bar',
      data: keys.map(k => cats[k]),
      itemStyle: { color: '#52c41a' },
      barMaxWidth: 40
    }]
  }
})

async function searchTask(kw: string) {
  taskLoading.value = true
  try {
    const res = await getTaskListApi({ taskName: kw, taskStatus: 3, pageSize: 20 })
    const data = res.data?.data || res.data
    taskOptions.value = data?.rows || data || []
  } catch {
    //
  } finally {
    taskLoading.value = false
  }
}

async function loadReport(id: number) {
  if (!id) { report.value = null; return }
  reportLoading.value = true
  try {
    const res = await getTaskReportApi(id)
    report.value = res.data?.data || res.data
  } catch {
    message.error('加载报告失败')
  } finally {
    reportLoading.value = false
  }
}

async function exportReport(fmt: string) {
  if (!selectedTaskId.value) return
  exporting.value = true
  try {
    const res = await exportTaskReportApi(selectedTaskId.value, fmt)
    const blob = new Blob([res.data])
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `task_report_${selectedTaskId.value}.${fmt}`
    a.click()
    URL.revokeObjectURL(url)
  } catch {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}

function goAnnotation(resultId: number) {
  router.push(`/result/${resultId}/annotation`)
}

onMounted(() => {
  searchTask('')
})
</script>

<style scoped lang="scss">
.report-task-page {
  padding: 16px;
}

.search-form {
  margin-bottom: 20px;
}

.stat-cards {
  margin-bottom: 20px;
}

.chart-row {
  margin-bottom: 20px;
}

.low-conf-card {
  margin-top: 8px;
}
</style>
