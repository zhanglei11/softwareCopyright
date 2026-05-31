<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline">
        <a-form-item label="任务名称"><a-input v-model:value="q.taskName" allow-clear /></a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="q.status" allow-clear placeholder="全部" style="width:120px">
            <a-select-option :value="0">待提交</a-select-option>
            <a-select-option :value="1">识别中</a-select-option>
            <a-select-option :value="2">已完成</a-select-option>
            <a-select-option :value="1">执行中</a-select-option>
            <a-select-option :value="2">已完成</a-select-option>
            <a-select-option :value="3">失败</a-select-option>
            <a-select-option :value="4">已取消</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="load">查询</a-button>
          <a-button style="margin-left:8px" @click="()=>{q.taskName='';q.status=undefined;load()}">重置</a-button>
        </a-form-item>
      </a-form>
    </div>
    <div class="table-toolbar">
      <span>任务列表</span>
      <a-button type="primary" @click="$router.push({ name: 'TaskDetail', params: { id: 'new' } })"><PlusOutlined /> 创建任务</a-button>
    </div>
    <a-table :columns="cols" :data-source="list" :loading="loading" :pagination="page" @change="c=>{page.current=c.current;load()}" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='taskStatus'">
          <a-tag :color="statusColor(record.status)">{{ statusLabel(record.status) }}</a-tag>
        </template>
        <template v-if="column.key==='progress'">
          <a-progress v-if="record.status===1" :percent="record.totalCount ? Math.round((record.processedCount||0)/record.totalCount*100) : 0" size="small" />
          <span v-else>-</span>
        </template>
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="$router.push({ name: 'TaskDetail', params: { id: record.id } })">详情</a-button>
          <a-button size="small" type="link" v-if="record.status===0" @click="submit(record.id)">提交</a-button>
          <a-button size="small" type="link" danger v-if="[0,1].includes(record.status)" @click="cancel(record.id)">取消</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getTaskListApi, submitTaskApi, cancelTaskApi } from '@/api/task'
const loading = ref(false); const list = ref<any[]>([])
const q = reactive<any>({ taskName: '', status: undefined })
const page = reactive({ current: 1, pageSize: 10, total: 0 })
const statusLabel = (s: number) => ['待提交','执行中','已完成','失败','已取消'][s] || '-'
const statusColor = (s: number) => ['default','processing','success','error','default'][s] || 'default'
const cols = [
  { title: '任务名称', dataIndex: 'taskName' }, { title: '模型版本', dataIndex: 'modelVersionNo' },
  { title: '置信度阈值', dataIndex: 'confidenceThreshold', width: 100 }, { title: '影像数', dataIndex: 'totalCount', width: 80 },
  { title: '状态', key: 'taskStatus', width: 90 }, { title: '进度', key: 'progress', width: 120 },
  { title: '创建时间', dataIndex: 'createdAt', customRender: ({ text }: any) => text ? text.replace('T', ' ').slice(0, 19) : '-' }, { title: '操作', key: 'action', width: 160 },
]
async function load() { loading.value = true; try { const r = await getTaskListApi({ pageNum: page.current, pageSize: page.pageSize, ...q }); list.value = r.data?.rows || r.data?.list || []; page.total = r.data?.total || 0 } finally { loading.value = false } }
async function submit(id: number) { await submitTaskApi(id); message.success('已提交'); load() }
async function cancel(id: number) { await cancelTaskApi(id); message.success('已取消'); load() }
onMounted(load)
</script>
