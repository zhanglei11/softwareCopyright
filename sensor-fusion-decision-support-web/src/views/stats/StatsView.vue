<template>
  <div>
    <a-card title="统计查询" style="margin-bottom:16px">
      <a-space wrap>
        <a-date-picker v-model:value="startDate" placeholder="开始时间" format="YYYY-MM-DD" style="width:140px" @change="(_, v) => params.startTime=v" />
        <a-date-picker v-model:value="endDate" placeholder="结束时间" format="YYYY-MM-DD" style="width:140px" @change="(_, v) => params.endTime=v" />
        <a-select v-model:value="params.granularity" style="width:100px">
          <a-select-option value="day">按天</a-select-option>
          <a-select-option value="hour">按小时</a-select-option>
        </a-select>
        <a-button type="primary" @click="loadAll"><search-outlined />查询</a-button>
        <a-button @click="resetParams"><reload-outlined />重置</a-button>
      </a-space>
    </a-card>

    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="融合执行统计" :loading="fusionLoading">
          <div v-if="fusionData">
            <a-descriptions bordered :column="2" size="small" style="margin-bottom:12px">
              <a-descriptions-item label="总执行次数">{{ fusionData.total ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="成功次数">{{ fusionSuccessCount }}</a-descriptions-item>
              <a-descriptions-item label="失败次数">{{ (fusionData.total ?? 0) - fusionSuccessCount }}</a-descriptions-item>
              <a-descriptions-item label="成功率">{{ fusionData.total > 0 ? ((fusionSuccessCount / fusionData.total) * 100).toFixed(1) + '%' : '-' }}</a-descriptions-item>
            </a-descriptions>
            <div v-if="fusionData.trend?.length">
              <div style="font-weight:500;margin-bottom:6px">每日趋势</div>
              <a-table :data-source="fusionData.trend" :columns="trendColumns" size="small" :pagination="false" row-key="dt" />
            </div>
          </div>
          <a-empty v-else description="暂无数据" />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="决策执行统计" :loading="decisionLoading">
          <div v-if="decisionData">
            <a-descriptions bordered :column="2" size="small" style="margin-bottom:12px">
              <a-descriptions-item label="总触发次数">{{ decisionData.total ?? '-' }}</a-descriptions-item>
              <a-descriptions-item label="平均响应时长">{{ decisionAvgTime }}</a-descriptions-item>
            </a-descriptions>
            <div v-if="decisionData.byRule?.length">
              <div style="font-weight:500;margin-bottom:6px">规则命中分布</div>
              <a-table :data-source="decisionData.byRule" :columns="ruleColumns" size="small" :pagination="false" row-key="rule_id" />
            </div>
          </div>
          <a-empty v-else description="暂无数据" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getFusionSummaryApi, getDecisionSummaryApi } from '@/api/stats'

const fusionLoading = ref(false)
const decisionLoading = ref(false)
const fusionData = ref<any>(null)
const decisionData = ref<any>(null)
const startDate = ref<any>(null)
const endDate = ref<any>(null)
const params = reactive({ startTime: '', endTime: '', granularity: 'day' })

const trendColumns = [
  { title: '日期', dataIndex: 'dt', key: 'dt' },
  { title: '总执行', dataIndex: 'cnt', key: 'cnt' },
  { title: '成功', dataIndex: 'success_cnt', key: 'success_cnt' },
]

const ruleColumns = [
  { title: '规则名称', dataIndex: 'rule_name', key: 'rule_name' },
  { title: '触发次数', dataIndex: 'cnt', key: 'cnt' },
]

const fusionSuccessCount = computed(() => {
  const trend: any[] = fusionData.value?.trend ?? []
  return trend.reduce((s: number, t: any) => s + (t.success_cnt ?? 0), 0)
})

const decisionAvgTime = computed(() => {
  const list: any[] = decisionData.value?.avgResponseTime ?? []
  if (!list.length) return '-'
  const avg = list.reduce((s: number, t: any) => s + (t.avg_sec ?? 0), 0) / list.length
  return avg.toFixed(2) + 's'
})

function resetParams() {
  Object.assign(params, { startTime: '', endTime: '', granularity: 'day' })
  startDate.value = null; endDate.value = null; loadAll()
}

async function loadFusion() {
  fusionLoading.value = true
  try { const res: any = await getFusionSummaryApi(params); fusionData.value = res.data }
  finally { fusionLoading.value = false }
}

async function loadDecision() {
  decisionLoading.value = true
  try { const res: any = await getDecisionSummaryApi(params); decisionData.value = res.data }
  finally { decisionLoading.value = false }
}

async function loadAll() { await Promise.all([loadFusion(), loadDecision()]) }

onMounted(loadAll)
</script>