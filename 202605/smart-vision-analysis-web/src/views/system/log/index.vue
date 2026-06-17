<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline">
        <a-form-item label="操作人"><a-input v-model:value="q.operator" allow-clear /></a-form-item>
        <a-form-item label="时间范围">
          <a-range-picker v-model:value="q.dateRange" format="YYYY-MM-DD" />
        </a-form-item>
        <a-form-item><a-button type="primary" @click="load">查询</a-button></a-form-item>
      </a-form>
    </div>
    <a-table :columns="cols" :data-source="list" :loading="loading" :pagination="page" @change="c => { page.current=c.current; load() }" row-key="id" />
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getOperationLogsApi } from '@/api/system/log'
const loading = ref(false); const list = ref<any[]>([])
const q = reactive<any>({ operator: '', dateRange: [] })
const page = reactive({ current: 1, pageSize: 10, total: 0 })
const cols = [{ title: '操作模块', dataIndex: 'module' }, { title: '操作描述', dataIndex: 'operation' }, { title: '操作人', dataIndex: 'username' }, { title: '操作IP', dataIndex: 'ip' }, { title: '结果', dataIndex: 'status', customRender: ({ text }: any) => text === 1 ? '成功' : text === 0 ? '失败' : '-' }, { title: '操作时间', dataIndex: 'createdAt', customRender: ({ text }: any) => text ? text.replace('T', ' ').slice(0, 19) : '-' }]
async function load() { loading.value = true; try { const r = await getOperationLogsApi({ pageNum: page.current, pageSize: page.pageSize, username: q.operator, startTime: q.dateRange?.[0] ? q.dateRange[0].format?.('YYYY-MM-DD') || q.dateRange[0] : undefined, endTime: q.dateRange?.[1] ? q.dateRange[1].format?.('YYYY-MM-DD') || q.dateRange[1] : undefined }); list.value = r.data?.rows || r.data?.list || []; page.total = r.data?.total || 0 } finally { loading.value = false } }
onMounted(load)
</script>
