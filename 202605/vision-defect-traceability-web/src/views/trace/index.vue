<template>
  <a-card title="批次追溯查询">
    <a-form layout="inline" :model="query" style="margin-bottom:16px">
      <a-form-item label="批次号" required>
        <a-input v-model:value="query.batchNo" placeholder="请输入批次号" style="width:200px" @pressEnter="loadData" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="loadData" :loading="loading"><SearchOutlined />查询</a-button>
      </a-form-item>
    </a-form>
    <template v-if="result">
      <a-descriptions bordered :column="3" style="margin-bottom:16px">
        <a-descriptions-item label="批次号">{{ result.batchNo }}</a-descriptions-item>
        <a-descriptions-item label="产线">{{ result.lineName }}</a-descriptions-item>
        <a-descriptions-item label="检测总数">{{ result.totalCount }}</a-descriptions-item>
        <a-descriptions-item label="合格数">{{ result.qualifiedCount }}</a-descriptions-item>
        <a-descriptions-item label="缺陷数">{{ result.defectCount }}</a-descriptions-item>
        <a-descriptions-item label="合格率">{{ ((result.qualifiedRate ?? 0) * 100).toFixed(1) }}%</a-descriptions-item>
      </a-descriptions>
      <a-table :columns="columns" :data-source="result.records" row-key="id" size="small" />
    </template>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { SearchOutlined } from '@ant-design/icons-vue'
import { traceBatch } from '@/api/trace'
import type { BatchTraceVO } from '@/types'
const loading = ref(false)
const query = reactive({ batchNo: '', startDate: '', endDate: '' })
const result = ref<BatchTraceVO | null>(null)
const columns = [
  { title: '序列号', dataIndex: 'serialNo' },
  { title: '产品型号', dataIndex: 'productTypeName' },
  { title: '缺陷分类', dataIndex: 'categoryName' },
  { title: '检测结果', dataIndex: 'result', customRender: ({value}: any) => value === 1 ? '合格' : (value === 0 ? '不合格' : '-') },
  { title: '检测时间', dataIndex: 'detectTime', customRender: ({value}: any) => value?.replace('T', ' ').slice(0, 19) || '-' },
]
const loadData = async () => {
  if (!query.batchNo.trim()) { message.warning('请输入批次号'); return }
  loading.value = true
  try { const res = await traceBatch(query); result.value = (res as any).data } catch {} finally { loading.value = false }
}
</script>
