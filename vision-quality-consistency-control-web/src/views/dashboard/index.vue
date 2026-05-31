<template>
  <div>
    <a-row :gutter="16" style="margin-bottom:24px">
      <a-col :span="6" v-for="card in statCards" :key="card.label">
        <a-statistic
          :title="card.label"
          :value="card.value"
          :suffix="card.suffix"
          :value-style="{ color: card.color, fontSize: '28px', fontWeight: 700 }"
          style="background:#fff; padding:20px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,.06)"
        />
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="近30天不合格品趋势" :bordered="false" style="border-radius:8px">
          <v-chart :option="trendOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="不合格品处置状态分布" :bordered="false" style="border-radius:8px">
          <v-chart :option="radarOption" style="height:280px" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { LineChart, BarChart, PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { statsApi } from '@/api/quality/stats'

use([LineChart, BarChart, PieChart, GridComponent, TooltipComponent, LegendComponent, CanvasRenderer])

const statCards = ref([
  { label: '不合格品总数', value: 0, suffix: '张', color: '#1677ff' },
  { label: '待处置数量', value: 0, suffix: '张', color: '#ff4d4f' },
  { label: '已处置数量', value: 0, suffix: '张', color: '#52c41a' },
  { label: '验证通过数量', value: 0, suffix: '张', color: '#faad14' },
])

const trendOption = ref<any>({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: [] },
  yAxis: { type: 'value' },
  series: [{ name: '不合格品数', type: 'bar', data: [], itemStyle: { color: '#1677ff' } }],
})

const radarOption = ref<any>({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [{
    type: 'pie',
    radius: '65%',
    data: [],
    label: { formatter: '{b}: {c}' },
  }],
})

onMounted(async () => {
  try {
    const res: any = await statsApi.dashboard()
    const d = res.data || {}
    const s = d.defectSummary || {}
    statCards.value[0].value = s.total ?? 0
    statCards.value[1].value = s.pending_count ?? 0
    statCards.value[2].value = s.disposed_count ?? 0
    statCards.value[3].value = s.verified_pass_count ?? 0

    const trend = d.defectTrend || []
    if (trend.length) {
      trendOption.value.xAxis.data = trend.map((t: any) => t.stat_date)
      trendOption.value.series[0].data = trend.map((t: any) => t.total)
    }

    radarOption.value.series[0].data = [
      { value: s.pending_count ?? 0, name: '待处置' },
      { value: s.processing_count ?? 0, name: '处置中' },
      { value: s.disposed_count ?? 0, name: '已处置' },
      { value: s.ignored_count ?? 0, name: '已忽略' },
    ]
  } catch { /* ignore */ }
})
</script>
