<template>
  <div>
    <a-card title="趋势报表筛选" :bordered="false" style="margin-bottom:16px">
      <a-form layout="inline" :model="query" @finish="loadStats">
        <a-form-item label="时间范围">
          <a-range-picker v-model:value="query.dateRange" value-format="YYYY-MM-DD" />
        </a-form-item>
        <a-form-item><a-button type="primary" html-type="submit">查询</a-button></a-form-item>
      </a-form>
    </a-card>

    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="不合格品数量趋势" :bordered="false">
          <v-chart :option="passRateOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="处置/验证数量趋势" :bordered="false">
          <v-chart :option="defectTypeOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { statsApi } from '@/api/quality/stats'
import dayjs from 'dayjs'

use([LineChart, BarChart, GridComponent, TooltipComponent, LegendComponent, CanvasRenderer])

const query = reactive({
  dateRange: [dayjs().subtract(30, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')] as [string, string],
})

const passRateOption = ref<any>({ series: [] })
const defectTypeOption = ref<any>({ series: [] })

async function loadStats() {
  const params = { startDate: query.dateRange[0], endDate: query.dateRange[1] }
  try {
    const res: any = await statsApi.trend(params)
    const trend: any[] = res.data?.trend || []
    const dates = trend.map((t: any) => t.stat_date)
    const totals = trend.map((t: any) => t.total)
    const disposedCounts = trend.map((t: any) => t.disposed_count)
    const verifiedCounts = trend.map((t: any) => t.verified_count)

    passRateOption.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: dates },
      yAxis: { type: 'value', name: '数量' },
      series: [{ name: '不合格品总数', type: 'bar', data: totals, itemStyle: { borderRadius: [4, 4, 0, 0] } }],
    }

    defectTypeOption.value = {
      tooltip: { trigger: 'axis' },
      legend: {},
      xAxis: { type: 'category', data: dates },
      yAxis: { type: 'value', name: '数量' },
      series: [
        { name: '已处置', type: 'line', smooth: true, data: disposedCounts },
        { name: '已验证', type: 'line', smooth: true, data: verifiedCounts },
      ],
    }
  } catch { /* ignore */ }
}

onMounted(loadStats)
</script>
