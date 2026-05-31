<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-space wrap>
          <a-input v-model:value="query.keyword" placeholder="关键词" allow-clear style="width:160px" @press-enter="loadData" />
          <a-date-picker v-model:value="startDate" placeholder="开始时间" format="YYYY-MM-DD" style="width:140px" @change="(_, v) => query.startTime=v" />
          <a-date-picker v-model:value="endDate" placeholder="结束时间" format="YYYY-MM-DD" style="width:140px" @change="(_, v) => query.endTime=v" />
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="resetQuery"><reload-outlined />重置</a-button>
        </a-space>
      </div>
      <a-table :columns="columns" :data-source="tableData" :loading="loading" :pagination="pagination" row-key="id" @change="handleTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleDetail(record)">详情</a-button>
              <a-button type="link" size="small" @click="handleTrace(record)">溯源</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:open="detailVisible" :title="traceMode?'决策结果溯源':'决策结果详情'" :footer="null" width="700px">
      <a-descriptions v-if="detail" bordered :column="2" size="small">
        <a-descriptions-item label="ID">{{ detail.id }}</a-descriptions-item>
        <a-descriptions-item label="规则ID">{{ detail.ruleId }}</a-descriptions-item>
        <a-descriptions-item label="方案ID">{{ detail.schemeId }}</a-descriptions-item>
        <a-descriptions-item label="决策时间">{{ detail.decisionTime }}</a-descriptions-item>
        <a-descriptions-item label="决策结果" :span="2">{{ detail.decisionResult }}</a-descriptions-item>
        <a-descriptions-item label="置信度">{{ detail.confidence }}</a-descriptions-item>
        <a-descriptions-item label="触发值" :span="2"><pre style="max-height:150px;overflow:auto">{{ JSON.stringify(detail.triggerValue, null, 2) }}</pre></a-descriptions-item>
        <template v-if="traceMode && detail.traceInfo">
          <a-descriptions-item label="溯源信息" :span="2"><pre style="max-height:200px;overflow:auto">{{ JSON.stringify(detail.traceInfo, null, 2) }}</pre></a-descriptions-item>
        </template>
      </a-descriptions>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getDecisionResultListApi, getDecisionResultDetailApi, getDecisionResultTraceApi } from '@/api/decision/result'

const loading = ref(false)
const tableData = ref<any[]>([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true })
const detailVisible = ref(false)
const traceMode = ref(false)
const detail = ref<any>(null)
const startDate = ref<any>(null)
const endDate = ref<any>(null)
const query = reactive<any>({ keyword: '', startTime: '', endTime: '' })

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '规则ID', dataIndex: 'ruleId', key: 'ruleId' },
  { title: '方案ID', dataIndex: 'schemeId', key: 'schemeId' },
  { title: '决策结果', dataIndex: 'decisionResult', key: 'decisionResult' },
  { title: '置信度', dataIndex: 'confidence', key: 'confidence' },
  { title: '决策时间', dataIndex: 'decisionTime', key: 'decisionTime' },
  { title: '操作', key: 'action', width: 120 },
]

function resetQuery() {
  Object.assign(query, { keyword: '', startTime: '', endTime: '' }); startDate.value = null; endDate.value = null
  pagination.current = 1; loadData()
}

async function loadData() {
  loading.value = true
  try {
    const res: any = await getDecisionResultListApi({ ...query, pageNum: pagination.current, pageSize: pagination.pageSize })
    tableData.value = res.data?.records || []; pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

function handleTableChange(pag: any) { pagination.current = pag.current; pagination.pageSize = pag.pageSize; loadData() }

async function handleDetail(record: any) {
  const res: any = await getDecisionResultDetailApi(record.id)
  detail.value = res.data; traceMode.value = false; detailVisible.value = true
}

async function handleTrace(record: any) {
  const res: any = await getDecisionResultTraceApi(record.id)
  detail.value = res.data; traceMode.value = true; detailVisible.value = true
}

onMounted(loadData)
</script>
