<template>
  <div>
    <a-form layout="inline" :model="query" style="margin-bottom:16px" @finish="loadData">
      <a-form-item><a-input v-model:value="query.taskName" placeholder="任务名称" allow-clear /></a-form-item>
      <a-form-item>
        <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:120px">
          <a-select-option :value="1">待执行</a-select-option>
          <a-select-option :value="2">执行中</a-select-option>
          <a-select-option :value="3">已完成</a-select-option>
          <a-select-option :value="4">已取消</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" html-type="submit">查询</a-button></a-form-item>
    </a-form>
    <div style="margin-bottom:12px"><a-button type="primary" @click="openCreate()">创建任务</a-button></div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="pagination" @change="onTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="statusColor(record.status)">{{ statusLabel(record.status) }}</a-tag>
        </template>
        <template v-if="column.key === 'priority'">
          <a-tag :color="record.priority===2?'red':record.priority===1?'orange':'blue'">{{ record.priority===2?'高':record.priority===1?'中':'低' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="viewTask(record)">详情</a-button>
            <a-button v-if="record.status===1" size="small" type="link" @click="onStart(record.id)">开始</a-button>
            <a-button v-if="record.status===2" size="small" type="link" @click="onComplete(record.id)">完成</a-button>
            <a-popconfirm v-if="[1,2].includes(record.status)" title="确认取消?" @confirm="onCancel(record.id)">
              <a-button size="small" type="link" danger>取消</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="createOpen" title="创建检测任务" @ok="onCreateSave" :confirm-loading="saving" width="640px">
      <a-form :model="createForm" label-col="{ span: 7 }" wrapper-col="{ span: 15 }">
        <a-form-item label="任务名称"><a-input v-model:value="createForm.taskName" /></a-form-item>
        <a-form-item label="检测对象"><a-input v-model:value="createForm.detectionTarget" /></a-form-item>
        <a-form-item label="质量标准模板">
          <a-select v-model:value="createForm.templateId" :options="templateOptions" placeholder="选择模板" />
        </a-form-item>
        <a-form-item label="影像数量"><a-input-number v-model:value="createForm.imageCount" style="width:100%" /></a-form-item>
        <a-form-item label="优先级">
          <a-select v-model:value="createForm.priority">
            <a-select-option :value="2">高</a-select-option>
            <a-select-option :value="1">中</a-select-option>
            <a-select-option :value="0">低</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="createForm.remark" /></a-form-item>
      </a-form>
    </a-modal>

    <a-drawer v-model:open="detailOpen" title="任务详情" width="600">
      <a-descriptions :column="2" bordered v-if="detailData">
        <a-descriptions-item label="任务编号" :span="2">{{ detailData.taskCode }}</a-descriptions-item>
        <a-descriptions-item label="任务名称" :span="2">{{ detailData.taskName }}</a-descriptions-item>
        <a-descriptions-item label="检测对象">{{ detailData.detectionTarget }}</a-descriptions-item>
        <a-descriptions-item label="状态"><a-tag :color="statusColor(detailData.status)">{{ statusLabel(detailData.status) }}</a-tag></a-descriptions-item>
        <a-descriptions-item label="影像总数">{{ detailData.imageCount }}</a-descriptions-item>
        <a-descriptions-item label="合格率">{{ detailData.qualifiedRate != null ? detailData.qualifiedRate + '%' : '-' }}</a-descriptions-item>
      </a-descriptions>
      <a-divider>检测记录</a-divider>
      <a-table :columns="recordCols" :data-source="records" :pagination="false" size="small" row-key="id" :loading="recordLoading" />
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { taskApi } from '@/api/quality/task'
import { templateApi } from '@/api/quality/template'

const statusColor = (s: number) => ({ 1: 'default', 2: 'processing', 3: 'success', 4: 'error' } as any)[s] || 'default'
const statusLabel = (s: number) => ({ 1: '待执行', 2: '执行中', 3: '已完成', 4: '已取消' } as any)[s] || s

const columns = [
  { title: '任务编号', dataIndex: 'taskCode', key: 'taskCode' },
  { title: '任务名称', dataIndex: 'taskName', key: 'taskName' },
  { title: '检测对象', dataIndex: 'detectionTarget', key: 'detectionTarget' },
  { title: '优先级', dataIndex: 'priority', key: 'priority' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '合格率', dataIndex: 'qualifiedRate', key: 'qualifiedRate', customRender: ({ text }: any) => text != null ? text + '%' : '-' },
  { title: '创建时间', key: 'createdAt', customRender: ({ record }: any) => record.createdAt ? record.createdAt.replace('T', ' ').slice(0, 19) : '-' },
  { title: '操作', key: 'action' },
]
const recordCols = [
  { title: '影像标识', dataIndex: 'imageId' },
  { title: '检测时间', key: 'detectedAt', customRender: ({ record }: any) => record.detectedAt ? record.detectedAt.replace('T', ' ').slice(0, 19) : '-' },
  { title: '是否合格', dataIndex: 'isQualified', customRender: ({ text }: any) => text ? '✓ 合格' : '✗ 不合格' },
]
const query = reactive({ taskName: '', status: undefined as string | undefined })
const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const createOpen = ref(false)
const detailOpen = ref(false)
const detailData = ref<any>(null)
const records = ref<any[]>([])
const recordLoading = ref(false)
const saving = ref(false)
const createForm = reactive({ taskName: '', detectionTarget: '', templateId: undefined as number | undefined, imageCount: undefined as number | undefined, priority: 1, remark: '' })
const templateOptions = ref<any[]>([])

async function loadData() {
  loading.value = true
  try {
    const res: any = await taskApi.list({ ...query, page: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data?.rows || res.data?.list || res.data?.records || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}
function onTableChange(p: any) { pagination.current = p.current; loadData() }

function openCreate() {
  Object.assign(createForm, { taskName: '', detectionTarget: '', templateId: undefined, imageCount: undefined, priority: 1, remark: '' })
  createOpen.value = true
}

async function onCreateSave() {
  saving.value = true
  try {
    await taskApi.create(createForm)
    message.success('任务创建成功'); createOpen.value = false; loadData()
  } finally { saving.value = false }
}

async function viewTask(row: any) {
  const res: any = await taskApi.get(row.id)
  detailData.value = res.data
  detailOpen.value = true
  recordLoading.value = true
  try {
    const r: any = await taskApi.records(row.id)
    records.value = r.data?.list || r.data || []
  } finally { recordLoading.value = false }
}

async function onStart(id: number) { await taskApi.start(id); message.success('任务已开始'); loadData() }
async function onComplete(id: number) { await taskApi.complete(id); message.success('任务已完成'); loadData() }
async function onCancel(id: number) { await taskApi.cancel(id); message.success('任务已取消'); loadData() }

onMounted(async () => {
  loadData()
  const res: any = await templateApi.list({ status: 1 })
  templateOptions.value = (res.data?.rows || res.data?.rows || res.data?.list || res.data || []).map((t: any) => ({ label: t.templateName, value: t.id }))
})
</script>
