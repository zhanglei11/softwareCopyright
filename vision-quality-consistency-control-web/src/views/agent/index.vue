<template>
  <div>
    <!-- 搜索栏 -->
    <a-form layout="inline" :model="query" style="margin-bottom:16px" @finish="loadData">
      <a-form-item>
        <a-input v-model:value="query.agentName" placeholder="智能体名称" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-select v-model:value="query.agentType" placeholder="类型" allow-clear style="width:130px">
          <a-select-option :value="1">视觉检测</a-select-option>
          <a-select-option :value="2">尺寸测量</a-select-option>
          <a-select-option :value="3">缺陷识别</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:110px">
          <a-select-option :value="0">离线</a-select-option>
          <a-select-option :value="1">空闲</a-select-option>
          <a-select-option :value="2">运行中</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">查询</a-button>
      </a-form-item>
    </a-form>

    <div style="margin-bottom:12px">
      <a-button type="primary" @click="openRegister()">注册智能体</a-button>
    </div>

    <!-- 列表 -->
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'agentType'">
          <a-tag :color="typeColor(record.agentType)">{{ typeLabel(record.agentType) }}</a-tag>
        </template>
        <template v-if="column.key === 'status'">
          <a-badge :status="statusBadge(record.status)" :text="statusLabel(record.status)" />
        </template>
        <template v-if="column.key === 'lastHeartbeat'">
          {{ record.lastHeartbeat ? record.lastHeartbeat.replace('T', ' ').slice(0, 19) : '-' }}
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="openHistory(record)">任务历史</a-button>
            <a-button size="small" type="link" @click="openDispatch(record)">调度任务</a-button>
            <a-button size="small" type="link" @click="openEdit(record)">编辑</a-button>
            <a-button v-if="record.status === 0" size="small" type="link" style="color:#52c41a"
              @click="toggleStatus(record, 1)">上线</a-button>
            <a-button v-else size="small" type="link" style="color:#ff4d4f"
              @click="toggleStatus(record, 0)">下线</a-button>
            <a-popconfirm title="确认删除此智能体？" @confirm="onDelete(record.id)">
              <a-button size="small" type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 注册/编辑弹窗 -->
    <a-modal v-model:open="formOpen" :title="formMode === 'create' ? '注册智能体' : '编辑智能体'"
      @ok="onFormSave" :confirm-loading="saving" width="560px">
      <a-form :model="form" label-col="{ span: 7 }" wrapper-col="{ span: 15 }">
        <a-form-item label="智能体名称">
          <a-input v-model:value="form.agentName" placeholder="请输入名称" />
        </a-form-item>
        <a-form-item label="智能体编码" v-if="formMode === 'create'">
          <a-input v-model:value="form.agentCode" placeholder="全局唯一，如 AGENT-VD-002" />
        </a-form-item>
        <a-form-item label="类型">
          <a-select v-model:value="form.agentType">
            <a-select-option :value="1">视觉检测</a-select-option>
            <a-select-option :value="2">尺寸测量</a-select-option>
            <a-select-option :value="3">缺陷识别</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="接入端点URL">
          <a-input v-model:value="form.endpointUrl" placeholder="http://host:port/api" />
        </a-form-item>
        <a-form-item label="认证Token">
          <a-input-password v-model:value="form.authToken" placeholder="Bearer token（可选）" />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea v-model:value="form.remark" :rows="2" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 调度任务弹窗 -->
    <a-modal v-model:open="dispatchOpen" title="调度检测任务" @ok="onDispatchSave"
      :confirm-loading="saving" width="480px">
      <a-form :model="dispatchForm" label-col="{ span: 7 }" wrapper-col="{ span: 15 }">
        <a-form-item label="目标智能体">
          <a-input :value="dispatchForm.agentName" disabled />
        </a-form-item>
        <a-form-item label="选择检测任务">
          <a-select v-model:value="dispatchForm.taskId" :options="taskOptions"
            placeholder="选择待分配的检测任务" show-search optionFilterProp="label" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 任务历史抽屉 -->
    <a-drawer v-model:open="historyOpen" :title="`任务历史 - ${historyAgent?.agentName}`" width="700">
      <a-table :columns="taskCols" :data-source="agentTasks" :pagination="false"
        size="small" row-key="id" :loading="historyLoading" />
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { agentApi } from '@/api/agent'
import { taskApi } from '@/api/quality/task'

const typeLabel = (t: number) => ({ 1: '视觉检测', 2: '尺寸测量', 3: '缺陷识别' }[t] ?? '-')
const typeColor  = (t: number) => ({ 1: 'blue', 2: 'purple', 3: 'orange' }[t] ?? 'default')
const statusLabel = (s: number) => ({ 0: '离线', 1: '空闲', 2: '运行中' }[s] ?? '-')
const statusBadge = (s: number) => ({ 0: 'error', 1: 'default', 2: 'processing' }[s] ?? 'default') as any
const dispatchLabel = (s: number) => ({ 0: '待执行', 1: '执行中', 2: '已完成', 3: '失败' }[s] ?? '-')

const columns = [
  { title: '编码',      dataIndex: 'agentCode',      key: 'agentCode' },
  { title: '名称',      dataIndex: 'agentName',      key: 'agentName' },
  { title: '类型',      dataIndex: 'agentType',      key: 'agentType' },
  { title: '状态',      dataIndex: 'status',         key: 'status' },
  { title: '端点URL',   dataIndex: 'endpointUrl',    key: 'endpointUrl', ellipsis: true },
  { title: '最后心跳',  dataIndex: 'lastHeartbeat',  key: 'lastHeartbeat' },
  { title: '备注',      dataIndex: 'remark',         key: 'remark', ellipsis: true },
  { title: '操作',      key: 'action',               width: 260 },
]
const taskCols = [
  { title: '任务编号', dataIndex: 'taskCode',  key: 'taskCode' },
  { title: '任务名称', dataIndex: 'taskName',  key: 'taskName' },
  { title: '调度状态', dataIndex: 'dispatchStatus', key: 'dispatchStatus',
    customRender: ({ text }: any) => dispatchLabel(text) },
  { title: '调度时间', dataIndex: 'dispatchAt', key: 'dispatchAt' },
  { title: '完成时间', dataIndex: 'completeAt', key: 'completeAt' },
  { title: '结果摘要', dataIndex: 'resultSummary', key: 'resultSummary', ellipsis: true },
]

const query = reactive({ agentName: '', agentType: undefined as number | undefined, status: undefined as number | undefined })
const list = ref<any[]>([])
const loading = ref(false)

const formOpen = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const saving = ref(false)
const form = reactive({ id: undefined as number | undefined, agentName: '', agentCode: '', agentType: 1, endpointUrl: '', authToken: '', remark: '' })

const dispatchOpen = ref(false)
const dispatchForm = reactive({ agentId: 0, agentName: '', taskId: undefined as number | undefined })
const taskOptions = ref<any[]>([])

const historyOpen = ref(false)
const historyAgent = ref<any>(null)
const agentTasks = ref<any[]>([])
const historyLoading = ref(false)

async function loadData() {
  loading.value = true
  try {
    const res: any = await agentApi.list(query)
    list.value = res.data?.rows || res.data?.rows || res.data?.list || res.data || []
  } finally { loading.value = false }
}

function openRegister() {
  formMode.value = 'create'
  Object.assign(form, { id: undefined, agentName: '', agentCode: '', agentType: 1, endpointUrl: '', authToken: '', remark: '' })
  formOpen.value = true
}

function openEdit(record: any) {
  formMode.value = 'edit'
  Object.assign(form, { id: record.id, agentName: record.agentName, agentCode: record.agentCode, agentType: record.agentType, endpointUrl: record.endpointUrl, authToken: '', remark: record.remark })
  formOpen.value = true
}

async function onFormSave() {
  if (!form.agentName) { message.warning('请填写智能体名称'); return }
  saving.value = true
  try {
    if (formMode.value === 'create') {
      if (!form.agentCode) { message.warning('请填写智能体编码'); return }
      await agentApi.register(form)
      message.success('注册成功')
    } else {
      await agentApi.update(form)
      message.success('更新成功')
    }
    formOpen.value = false
    loadData()
  } finally { saving.value = false }
}

async function onDelete(id: number) {
  await agentApi.delete(id)
  message.success('删除成功')
  loadData()
}

async function toggleStatus(record: any, targetStatus: number) {
  await agentApi.toggleStatus(record.id, targetStatus)
  message.success(targetStatus === 1 ? '已上线' : '已下线')
  loadData()
}

function openDispatch(record: any) {
  dispatchForm.agentId = record.id
  dispatchForm.agentName = record.agentName
  dispatchForm.taskId = undefined
  dispatchOpen.value = true
}

async function onDispatchSave() {
  if (!dispatchForm.taskId) { message.warning('请选择检测任务'); return }
  saving.value = true
  try {
    await agentApi.dispatch({ agentId: dispatchForm.agentId, taskId: dispatchForm.taskId })
    message.success('任务已调度')
    dispatchOpen.value = false
    loadData()
  } finally { saving.value = false }
}

async function openHistory(record: any) {
  historyAgent.value = record
  historyOpen.value = true
  historyLoading.value = true
  try {
    const res: any = await agentApi.agentTasks(record.id)
    agentTasks.value = res.data || []
  } finally { historyLoading.value = false }
}

onMounted(async () => {
  loadData()
  const res: any = await taskApi.list({ pageSize: 100 })
  const tasks = res.data?.rows || res.data?.list || res.data?.records || res.data || []
  taskOptions.value = tasks.map((t: any) => ({ label: `${t.taskCode || ''} ${t.taskName}`, value: t.id }))
})
</script>
