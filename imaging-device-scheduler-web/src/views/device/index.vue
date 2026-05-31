<template>
  <div>
    <a-card :bordered="false" style="border-radius:8px">
      <div class="page-header">
        <a-space>
          <a-input v-model:value="query.keyword" placeholder="设备名称/编码" style="width:200px" @press-enter="loadList" />
          <a-select v-model:value="query.status" placeholder="状态" style="width:100px" allow-clear>
            <a-select-option :value="1">在线</a-select-option>
            <a-select-option :value="0">离线</a-select-option>
            <a-select-option :value="2">故障</a-select-option>
          </a-select>
          <a-button type="primary" @click="loadList"><SearchOutlined /> 查询</a-button>
          <a-button @click="resetQuery">重置</a-button>
        </a-space>
        <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增设备</a-button>
      </div>

      <a-table
        :columns="columns"
        :data-source="list"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="statusColor(record.status)">{{ statusText(record.status) }}</a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a @click="openEdit(record)">编辑</a>
              <a @click="openParams(record)">参数</a>
              <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id)">
                <a style="color:#ff4d4f">删除</a>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal v-model:open="modalOpen" :title="editId ? '编辑设备' : '新增设备'" @ok="handleSubmit" :confirm-loading="saving">
      <a-form :model="form" label-col="{ span: 6 }" wrapper-col="{ span: 16 }">
        <a-form-item label="设备编码" required>
          <a-input v-model:value="form.deviceCode" />
        </a-form-item>
        <a-form-item label="设备名称" required>
          <a-input v-model:value="form.deviceName" />
        </a-form-item>
        <a-form-item label="设备类型">
          <a-select v-model:value="form.deviceType">
            <a-select-option :value="1">工业相机</a-select-option>
            <a-select-option :value="2">激光雷达</a-select-option>
            <a-select-option :value="3">X射线仪</a-select-option>
            <a-select-option :value="4">超声波探头</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="型号规格">
          <a-input v-model:value="form.modelSpec" />
        </a-form-item>
        <a-form-item label="IP地址">
          <a-input v-model:value="form.ipAddress" />
        </a-form-item>
        <a-form-item label="安装位置">
          <a-input v-model:value="form.location" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 设备参数弹窗 -->
    <a-modal v-model:open="paramsOpen" title="设备参数" width="600px" @ok="handleSaveParams" :confirm-loading="savingParams">
      <a-button size="small" style="margin-bottom:8px" @click="addParamRow"><PlusOutlined />添加参数</a-button>
      <a-table :data-source="paramRows" :columns="paramCols" row-key="paramKey" size="small" :pagination="false">
        <template #bodyCell="{ column, record, index }">
          <template v-if="column.key === 'paramKey'">
            <a-input v-model:value="paramRows[index].paramKey" size="small" />
          </template>
          <template v-if="column.key === 'paramValue'">
            <a-input v-model:value="paramRows[index].paramValue" size="small" />
          </template>
          <template v-if="column.key === 'paramDesc'">
            <a-input v-model:value="paramRows[index].paramDesc" size="small" />
          </template>
          <template v-if="column.key === 'action'">
            <a style="color:#ff4d4f" @click="paramRows.splice(index, 1)">删除</a>
          </template>
        </template>
      </a-table>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { SearchOutlined, PlusOutlined } from '@ant-design/icons-vue'
import {
  getDeviceListApi, addDeviceApi, editDeviceApi, deleteDeviceApi,
  getDeviceParamsApi, saveDeviceParamsApi,
} from '@/api/device'

const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const query = reactive<any>({ keyword: '', status: undefined })

const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ deviceCode: '', deviceName: '', deviceType: 1, modelSpec: '', ipAddress: '', location: '' })

const paramsOpen = ref(false)
const currentDeviceId = ref<number>(0)
const paramRows = ref<any[]>([])
const savingParams = ref(false)

const columns = [
  { title: '设备编码', dataIndex: 'deviceCode', width: 130 },
  { title: '设备名称', dataIndex: 'deviceName' },
  { title: '型号规格', dataIndex: 'modelSpec' },
  { title: 'IP地址', dataIndex: 'ipAddress', width: 130 },
  { title: '安装位置', dataIndex: 'location' },
  { title: '最后心跳', dataIndex: 'lastHeartbeat', width: 160 },
  { title: '状态', key: 'status', width: 80 },
  { title: '操作', key: 'action', width: 140, fixed: 'right' },
]

const paramCols = [
  { title: '参数键', key: 'paramKey', dataIndex: 'paramKey' },
  { title: '参数值', key: 'paramValue', dataIndex: 'paramValue' },
  { title: '描述', key: 'paramDesc', dataIndex: 'paramDesc' },
  { title: '操作', key: 'action', width: 60 },
]

function statusText(s: number) { return { 0: '离线', 1: '在线', 2: '故障' }[s] ?? '-' }
function statusColor(s: number) { return { 0: 'default', 1: 'green', 2: 'red' }[s] ?? 'default' }

async function loadList() {
  loading.value = true
  try {
    const res: any = await getDeviceListApi({ ...query, page: pagination.current, size: pagination.pageSize })
    const d = res.data
    list.value = d.records || d || []
    pagination.total = d.total || list.value.length
  } finally { loading.value = false }
}

function handleTableChange(p: any) {
  pagination.current = p.current
  pagination.pageSize = p.pageSize
  loadList()
}

function resetQuery() { Object.assign(query, { keyword: '', status: undefined }); loadList() }

function openAdd() {
  editId.value = null
  Object.assign(form, { deviceCode: '', deviceName: '', deviceType: 1, modelSpec: '', ipAddress: '', location: '' })
  modalOpen.value = true
}

function openEdit(record: any) {
  editId.value = record.id
  Object.assign(form, record)
  modalOpen.value = true
}

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) {
      await editDeviceApi(editId.value, form)
      message.success('编辑成功')
    } else {
      await addDeviceApi(form)
      message.success('新增成功')
    }
    modalOpen.value = false
    loadList()
  } finally { saving.value = false }
}

async function handleDelete(id: number) {
  await deleteDeviceApi(id)
  message.success('删除成功')
  loadList()
}

async function openParams(record: any) {
  currentDeviceId.value = record.id
  const res: any = await getDeviceParamsApi(record.id)
  paramRows.value = res.data || []
  paramsOpen.value = true
}

function addParamRow() {
  paramRows.value.push({ paramKey: '', paramValue: '', paramDesc: '' })
}

async function handleSaveParams() {
  savingParams.value = true
  try {
    await saveDeviceParamsApi(currentDeviceId.value, paramRows.value)
    message.success('保存成功')
    paramsOpen.value = false
  } finally { savingParams.value = false }
}

onMounted(loadList)
</script>
