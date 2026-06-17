<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-space wrap>
          <a-input v-model:value="query.keyword" placeholder="关键词" allow-clear style="width:160px" @press-enter="loadData" />
          <a-date-picker v-model:value="startDate" placeholder="开始时间" format="YYYY-MM-DD" style="width:140px" @change="onStartChange" />
          <a-date-picker v-model:value="endDate" placeholder="结束时间" format="YYYY-MM-DD" style="width:140px" @change="onEndChange" />
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="resetQuery"><reload-outlined />重置</a-button>
        </a-space>
      </div>
      <a-table :columns="columns" :data-source="tableData" :loading="loading" :pagination="pagination" row-key="id" @change="handleTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="handleDetail(record)">详情</a-button>
          </template>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:open="detailVisible" title="融合结果详情" :footer="null" width="700px">
      <a-descriptions v-if="detail" bordered :column="2" size="small">
        <a-descriptions-item label="ID">{{ detail.id }}</a-descriptions-item>
        <a-descriptions-item label="方案ID">{{ detail.schemeId }}</a-descriptions-item>
        <a-descriptions-item label="融合时间">{{ detail.fusionTime }}</a-descriptions-item>
        <a-descriptions-item label="数据质量">{{ detail.dataQuality }}</a-descriptions-item>
        <a-descriptions-item label="融合结果" :span="2"><pre style="max-height:200px;overflow:auto">{{ JSON.stringify(detail.fusionResult, null, 2) }}</pre></a-descriptions-item>
      </a-descriptions>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import dayjs from 'dayjs'
import { getFusionResultListApi, getFusionResultDetailApi } from '@/api/fusion/result'

const loading = ref(false)
const tableData = ref<any[]>([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true })
const detailVisible = ref(false)
const detail = ref<any>(null)
const startDate = ref<any>(null)
const endDate = ref<any>(null)
const query = reactive<any>({ keyword: '', startTime: '', endTime: '', pageNum: 1, pageSize: 10 })

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '方案ID', dataIndex: 'schemeId', key: 'schemeId' },
  { title: '融合时间', dataIndex: 'fusionTime', key: 'fusionTime' },
  { title: '数据质量', dataIndex: 'dataQuality', key: 'dataQuality' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', width: 80 },
]

function onStartChange(_: any, val: string) { query.startTime = val }
function onEndChange(_: any, val: string) { query.endTime = val }

async function loadData() {
  loading.value = true
  try {
    const res: any = await getFusionResultListApi({ ...query, pageNum: pagination.current, pageSize: pagination.pageSize })
    tableData.value = res.data?.records || []; pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

function resetQuery() {
  Object.assign(query, { keyword: '', startTime: '', endTime: '' }); startDate.value = null; endDate.value = null
  pagination.current = 1; loadData()
}

function handleTableChange(pag: any) { pagination.current = pag.current; pagination.pageSize = pag.pageSize; loadData() }

async function handleDetail(record: any) {
  const res: any = await getFusionResultDetailApi(record.id)
  detail.value = res.data; detailVisible.value = true
}

onMounted(loadData)
</script>
