<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline">
        <a-form-item label="所属任务">
          <a-select v-model:value="q.taskId" allow-clear placeholder="全部" :options="taskOptions" style="width:180px" />
        </a-form-item>
        <a-form-item label="审核状态">
          <a-select v-model:value="q.reviewStatus" allow-clear placeholder="全部" style="width:120px">
            <a-select-option :value="0">待审核</a-select-option>
            <a-select-option :value="1">已确认</a-select-option>
            <a-select-option :value="2">需修正</a-select-option>
            <a-select-option :value="3">已修正</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item><a-button type="primary" @click="load">查询</a-button></a-form-item>
      </a-form>
    </div>
    <div class="table-toolbar">
      <span>识别结果列表</span>
      <a-button @click="batchConfirm" :disabled="!selectedRows.length">批量确认 ({{ selectedRows.length }})</a-button>
    </div>
    <a-table
      :columns="cols" :data-source="list" :loading="loading"
      :pagination="page" @change="c=>{page.current=c.current;load()}"
      row-key="id"
      :row-selection="{ selectedRowKeys: selectedRows, onChange: (keys: any[]) => selectedRows = keys }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='thumb'">
          <img :src="record.thumbnailUrl" width="60" height="40" style="object-fit:cover;border-radius:4px;cursor:pointer" @click="goAnnotation(record.id)" />
        </template>
        <template v-if="column.key==='reviewStatus'">
          <a-tag :color="['orange','success','warning','blue'][record.reviewStatus]">{{ ['待审核','已确认','需修正','已修正'][record.reviewStatus] }}</a-tag>
        </template>
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="goAnnotation(record.id)">标注</a-button>
          <a-button size="small" type="link" v-if="record.reviewStatus!==1" @click="confirm(record.id)">确认</a-button>
          <a-button size="small" type="link" danger v-if="record.reviewStatus!==2" @click="markRevision(record.id)">标记修正</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getResultListApi, confirmResultApi, markRevisionApi, batchConfirmResultApi } from '@/api/result'
import { getTaskListApi } from '@/api/task'
const route = useRoute(); const router = useRouter()
const loading = ref(false); const list = ref<any[]>([])
const q = reactive<any>({ taskId: route.query.taskId ? Number(route.query.taskId) : undefined, reviewStatus: undefined })
const page = reactive({ current: 1, pageSize: 20, total: 0 })
const taskOptions = ref<any[]>([]); const selectedRows = ref<number[]>([])
const cols = [
  { title: '缩略图', key: 'thumb', width: 80 }, { title: '影像编号', dataIndex: 'imageNo' },
  { title: '影像文件名', dataIndex: 'fileName' }, { title: '所属任务', dataIndex: 'taskName' },
  { title: '识别框数', dataIndex: 'boxCount', width: 90 }, { title: '审核状态', key: 'reviewStatus', width: 90 },
  { title: '操作', key: 'action', width: 170 },
]
async function load() { loading.value = true; try { const r = await getResultListApi({ pageNum: page.current, pageSize: page.pageSize, ...q }); list.value = r.data?.rows || r.data?.list || []; page.total = r.data?.total || 0 } finally { loading.value = false } }
async function loadTaskOptions() { const r = await getTaskListApi({ pageSize: 999 }); taskOptions.value = (r.data?.rows || r.data?.list || []).map((t: any) => ({ label: t.taskName, value: t.id })) }
function goAnnotation(id: number) { router.push({ name: 'Annotation', params: { id } }) }
async function confirm(id: number) { await confirmResultApi(id); message.success('已确认'); load() }
async function markRevision(id: number) { await markRevisionApi(id); message.success('已标记修正'); load() }
async function batchConfirm() { await batchConfirmResultApi({ ids: selectedRows.value }); message.success('批量确认成功'); selectedRows.value = []; load() }
onMounted(() => { load(); loadTaskOptions() })
</script>
