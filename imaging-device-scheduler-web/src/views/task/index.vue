<template>
  <a-card :bordered="false" style="border-radius:8px">
    <div class="page-header">
      <a-space>
        <a-input v-model:value="query.keyword" placeholder="任务名称/编码" style="width:180px" @press-enter="loadList" />
        <a-select v-model:value="query.status" placeholder="状态" style="width:110px" allow-clear>
          <a-select-option :value="0">待执行</a-select-option>
          <a-select-option :value="1">进行中</a-select-option>
          <a-select-option :value="2">已完成</a-select-option>
          <a-select-option :value="3">已取消</a-select-option>
        </a-select>
        <a-button type="primary" @click="loadList">查询</a-button>
        <a-button @click="resetQuery">重置</a-button>
      </a-space>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增任务</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="pagination" @change="handleTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="statusColor(record.status)">{{ statusText(record.status) }}</a-tag>
        </template>
        <template v-if="column.key === 'priority'">
          <a-tag :color="priorityColor(record.priority)">{{ priorityText(record.priority) }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a @click="openEdit(record)">编辑</a>
            <a @click="openAssign(record)">分配设备</a>
            <a v-if="record.status === 0" @click="handleStart(record.id)" style="color:#52c41a">启动</a>
            <a v-if="record.status === 1" @click="handleComplete(record.id)" style="color:#1677ff">完成</a>
            <a v-if="[0,1].includes(record.status)" @click="handleCancel(record.id)" style="color:#ff4d4f">取消</a>
          </a-space>
        </template>
      </template>
    </a-table>
  </a-card>

  <a-modal v-model:open="modalOpen" :title="editId ? '编辑任务' : '新增任务'" width="640px" @ok="handleSubmit" :confirm-loading="saving">
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="任务编码" required><a-input v-model:value="form.taskCode" /></a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="任务名称" required><a-input v-model:value="form.taskName" /></a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="任务类型">
            <a-select v-model:value="form.taskType">
              <a-select-option :value="1">常规采集</a-select-option>
              <a-select-option :value="2">紧急检测</a-select-option>
              <a-select-option :value="3">周期巡检</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="优先级">
            <a-select v-model:value="form.priority">
              <a-select-option :value="1">低</a-select-option>
              <a-select-option :value="2">中</a-select-option>
              <a-select-option :value="3">高</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="计划开始"><a-input v-model:value="form.planStartTime" placeholder="yyyy-MM-dd HH:mm:ss" /></a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="计划结束"><a-input v-model:value="form.planEndTime" placeholder="yyyy-MM-dd HH:mm:ss" /></a-form-item>
        </a-col>
      </a-row>
      <a-form-item label="所需设备数"><a-input-number v-model:value="form.deviceCount" :min="1" style="width:100%" /></a-form-item>
      <a-form-item label="描述"><a-textarea v-model:value="form.description" :rows="3" /></a-form-item>
    </a-form>
  </a-modal>

  <!-- 分配设备弹窗 -->
  <a-modal v-model:open="assignOpen" title="分配设备" @ok="handleSaveAssign" :confirm-loading="savingAssign">
    <p>选择设备（可多选）：</p>
    <a-checkbox-group v-model:value="selectedDeviceIds" style="display:flex;flex-direction:column;gap:8px">
      <a-checkbox v-for="d in availableDevices" :key="d.id" :value="d.id">
        {{ d.deviceName }} ({{ d.deviceCode }}) - {{ d.ipAddress }}
      </a-checkbox>
    </a-checkbox-group>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getTaskListApi, addTaskApi, editTaskApi, startTaskApi, completeTaskApi, cancelTaskApi, assignDevicesApi } from '@/api/task'
import { getAvailableDevicesApi } from '@/api/device'

const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const query = reactive<any>({ keyword: '', status: undefined })
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ taskCode: '', taskName: '', taskType: 1, priority: 2, planStartTime: '', planEndTime: '', deviceCount: 1, description: '' })
const assignOpen = ref(false)
const assignTaskId = ref<number>(0)
const availableDevices = ref<any[]>([])
const selectedDeviceIds = ref<number[]>([])
const savingAssign = ref(false)

const columns = [
  { title: '任务编码', dataIndex: 'taskCode', width: 130 },
  { title: '任务名称', dataIndex: 'taskName' },
  { title: '优先级', key: 'priority', width: 80 },
  { title: '设备数', dataIndex: 'deviceCount', width: 70 },
  { title: '计划开始', dataIndex: 'planStartTime', width: 155 },
  { title: '计划结束', dataIndex: 'planEndTime', width: 155 },
  { title: '状态', key: 'status', width: 90 },
  { title: '操作', key: 'action', width: 200, fixed: 'right' },
]

function statusText(s: number) { return { 0: '待执行', 1: '进行中', 2: '已完成', 3: '已取消' }[s] ?? '-' }
function statusColor(s: number) { return { 0: 'default', 1: 'processing', 2: 'success', 3: 'error' }[s] ?? 'default' }
function priorityText(p: number) { return { 1: '低', 2: '中', 3: '高' }[p] ?? '-' }
function priorityColor(p: number) { return { 1: 'default', 2: 'blue', 3: 'red' }[p] ?? 'default' }

async function loadList() {
  loading.value = true
  try {
    const res: any = await getTaskListApi({ ...query, page: pagination.current, size: pagination.pageSize })
    const d = res.data
    list.value = d.records || d || []
    pagination.total = d.total || list.value.length
  } finally { loading.value = false }
}

function handleTableChange(p: any) { pagination.current = p.current; pagination.pageSize = p.pageSize; loadList() }
function resetQuery() { Object.assign(query, { keyword: '', status: undefined }); loadList() }
function openAdd() { editId.value = null; Object.assign(form, { taskCode: '', taskName: '', taskType: 1, priority: 2, planStartTime: '', planEndTime: '', deviceCount: 1, description: '' }); modalOpen.value = true }
function openEdit(record: any) { editId.value = record.id; Object.assign(form, record); modalOpen.value = true }

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) { await editTaskApi(editId.value, form); message.success('编辑成功') }
    else { await addTaskApi(form); message.success('新增成功') }
    modalOpen.value = false; loadList()
  } finally { saving.value = false }
}

async function handleStart(id: number) { await startTaskApi(id); message.success('任务已启动'); loadList() }
async function handleComplete(id: number) { await completeTaskApi(id); message.success('任务已完成'); loadList() }
async function handleCancel(id: number) { await cancelTaskApi(id); message.success('任务已取消'); loadList() }

async function openAssign(record: any) {
  assignTaskId.value = record.id
  selectedDeviceIds.value = []
  const res: any = await getAvailableDevicesApi()
  availableDevices.value = res.data || []
  assignOpen.value = true
}

async function handleSaveAssign() {
  savingAssign.value = true
  try {
    await assignDevicesApi(assignTaskId.value, selectedDeviceIds.value)
    message.success('设备分配成功')
    assignOpen.value = false
    loadList()
  } finally { savingAssign.value = false }
}

onMounted(loadList)
</script>
