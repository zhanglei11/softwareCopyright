<template>
  <div>
    <a-row :gutter="16" style="margin-bottom:16px">
      <a-col :span="6">
        <a-card><a-statistic title="数据源总数" :value="overview.total" prefix=""><template #prefix><database-outlined /></template></a-statistic></a-card>
      </a-col>
      <a-col :span="6">
        <a-card><a-statistic title="在线数据源" :value="overview.online" :value-style="{color:'#52c41a'}" /></a-card>
      </a-col>
      <a-col :span="6">
        <a-card><a-statistic title="离线数据源" :value="overview.offline" :value-style="{color:'#ff4d4f'}" /></a-card>
      </a-col>
      <a-col :span="6">
        <a-card><a-statistic title="异常数据源" :value="overview.error" :value-style="{color:'#faad14'}" /></a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="融合执行统计（近7日）" :loading="fusionLoading">
          <div v-if="fusionStats">
            <a-descriptions :column="2" size="small" bordered>
              <a-descriptions-item label="总执行次数">{{ fusionStats.totalCount ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="成功次数">{{ fusionStats.successCount ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="失败次数">{{ fusionStats.failCount ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="成功率">{{ fusionStats.successRate ?? '-' }}</a-descriptions-item>
            </a-descriptions>
          </div>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="决策执行统计（近7日）" :loading="decisionLoading">
          <div v-if="decisionStats">
            <a-descriptions :column="2" size="small" bordered>
              <a-descriptions-item label="总触发次数">{{ decisionStats.totalCount ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="规则命中数">{{ decisionStats.hitCount ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="未命中数">{{ decisionStats.missCount ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="命中率">{{ decisionStats.hitRate ?? '-' }}</a-descriptions-item>
            </a-descriptions>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getDatasourceStatusOverviewApi } from '@/api/datasource'
import { getFusionSummaryApi } from '@/api/stats'
import { getDecisionSummaryApi } from '@/api/stats'
import dayjs from 'dayjs'

const overview = ref<any>({ total: 0, online: 0, offline: 0, error: 0 })
const fusionStats = ref<any>(null)
const decisionStats = ref<any>(null)
const fusionLoading = ref(false)
const decisionLoading = ref(false)

const endTime = dayjs().format('YYYY-MM-DD')
const startTime = dayjs().subtract(7, 'day').format('YYYY-MM-DD')

onMounted(async () => {
  try {
    const res: any = await getDatasourceStatusOverviewApi()
    // API 返回数据源列表，按 connStatus 统计
    const list: any[] = Array.isArray(res.data) ? res.data : []
    const online = list.filter((d: any) => d.connStatus === 1).length
    const offline = list.filter((d: any) => d.connStatus === 0).length
    overview.value = { total: list.length, online, offline, error: 0 }
  } catch {}

  fusionLoading.value = true
  try {
    const res: any = await getFusionSummaryApi({ startTime, endTime })
    // API 返回 { total, trend:[{dt,cnt,success_cnt}], bySceneType }
    const d = res.data || {}
    const total = d.total ?? 0
    const trendList: any[] = Array.isArray(d.trend) ? d.trend : []
    const successCount = trendList.reduce((s: number, t: any) => s + (t.success_cnt ?? 0), 0)
    const failCount = total - successCount
    fusionStats.value = {
      totalCount: total,
      successCount,
      failCount,
      successRate: total > 0 ? ((successCount / total) * 100).toFixed(1) + '%' : '-'
    }
  } finally { fusionLoading.value = false }

  decisionLoading.value = true
  try {
    const res: any = await getDecisionSummaryApi({ startTime, endTime })
    // API 返回 { total, byRule:[{rule_id,rule_name,cnt}], avgResponseTime }
    const d = res.data || {}
    const total = d.total ?? 0
    decisionStats.value = {
      totalCount: total,
      hitCount: total,
      missCount: 0,
      hitRate: total > 0 ? '100%' : '-'
    }
  } finally { decisionLoading.value = false }
})
</script>
