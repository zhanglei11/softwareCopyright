<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { statsApi } from '@/api/stats'

const loading = ref(false)
const dashboard = ref<any>({ monthlyApplications: 0, monthlyInterviewDone: 0, monthlyHired: 0, funnel: {} })

const cards = computed(() => [
  { label: '本月投递总数', value: dashboard.value.monthlyApplications || 0 },
  { label: '本月面试完成数', value: dashboard.value.monthlyInterviewDone || 0 },
  { label: '本月录用数', value: dashboard.value.monthlyHired || 0 },
  { label: '本月录用转化率', value: dashboard.value.monthlyApplications ? `${Math.round(((dashboard.value.monthlyHired || 0) / dashboard.value.monthlyApplications) * 100)}%` : '0%' },
])

const funnelOption = computed(() => ({
  tooltip: { trigger: 'item' },
  series: [{
    type: 'funnel',
    left: '5%',
    top: 20,
    bottom: 20,
    width: '90%',
    data: [
      { value: dashboard.value.funnel?.applied || 0, name: '投递' },
      { value: dashboard.value.funnel?.passed || 0, name: '通过' },
      { value: dashboard.value.funnel?.interviewed || 0, name: '面试' },
      { value: dashboard.value.funnel?.hired || 0, name: '录用' },
    ],
  }],
}))

async function loadData() {
  loading.value = true
  try {
    const response: any = await statsApi.dashboard()
    dashboard.value = response.data || {}
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero">
      <h2>招聘漏斗与月度概览</h2>
      <p>直接验证 `/api/stats/dashboard` 接口与首页图表渲染链路。</p>
    </div>
    <div class="metric-grid">
      <div v-for="card in cards" :key="card.label" class="metric-card">
        <div class="metric-label">{{ card.label }}</div>
        <div class="metric-value">{{ card.value }}</div>
      </div>
    </div>
    <a-card class="glass-card" :bordered="false" :loading="loading">
      <div class="toolbar-row" style="margin-bottom: 14px;">
        <div>
          <h3 style="margin: 0;">招聘漏斗</h3>
          <div class="muted-text">投递 → 通过 → 面试 → 录用</div>
        </div>
        <a-button @click="loadData">刷新数据</a-button>
      </div>
      <VChart class="chart-panel" :option="funnelOption" autoresize />
    </a-card>
  </div>
</template>