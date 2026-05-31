<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import dayjs from 'dayjs'
import { statsApi } from '@/api/stats'
import { labelizeSource } from '@/utils/common'

const loading = ref(false)
const range = ref<any[]>([])
const rows = ref<any[]>([])

const pieOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [{
    type: 'pie',
    radius: ['42%', '72%'],
    data: rows.value.map((item) => ({ value: item.cnt, name: labelizeSource(item.source) })),
  }],
}))

async function loadData() {
  loading.value = true
  try {
    const params = range.value.length === 2
      ? { startDate: dayjs(range.value[0]).format('YYYY-MM-DD'), endDate: dayjs(range.value[1]).format('YYYY-MM-DD') }
      : undefined
    const response: any = await statsApi.source(params)
    rows.value = response.data?.distribution || []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero">
      <h2>来源分布统计</h2>
      <p>验证 `/api/stats/source` 接口与饼图渲染。</p>
    </div>
    <a-card class="glass-card" :bordered="false">
      <div class="toolbar-row">
        <div class="toolbar-filters"><a-range-picker v-model:value="range" /></div>
        <div class="toolbar-actions"><a-button type="primary" @click="loadData">查询</a-button></div>
      </div>
    </a-card>
    <div class="split-grid">
      <a-card class="glass-card" :bordered="false" :loading="loading"><VChart class="chart-panel" :option="pieOption" autoresize /></a-card>
      <a-card class="glass-card" :bordered="false" :loading="loading">
        <a-table :data-source="rows" :pagination="false" row-key="source">
          <a-table-column title="来源渠道"><template #default="{ record }">{{ labelizeSource(record.source) }}</template></a-table-column>
          <a-table-column title="简历数量" data-index="cnt" />
        </a-table>
      </a-card>
    </div>
  </div>
</template>