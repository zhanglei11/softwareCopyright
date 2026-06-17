<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-space>
          <a-input v-model:value="query.keyword" placeholder="数据源名称" allow-clear style="width:200px" @press-enter="loadData" />
          <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:100px">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="resetQuery"><reload-outlined />重置</a-button>
          <a-button type="primary" @click="handleAdd"><plus-outlined />新增数据源</a-button>
        </a-space>
      </div>
      <a-table :columns="columns" :data-source="tableData" :loading="loading" :pagination="pagination" row-key="id" @change="handleTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-switch :checked="record.status===1" checked-children="启用" un-checked-children="禁用" @change="(v:boolean) => handleStatusChange(record, v)" />
          </template>
          <template v-if="column.key === 'connStatus'">
            <a-tag :color="record.connStatus===1?'green':record.connStatus===0?'default':'orange'">
              {{ record.connStatus===1?'在线':record.connStatus===0?'未知':'离线' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'conn'">
            {{ record.connHost }}{{ record.connPort ? ':'+record.connPort : '' }}
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-button type="link" size="small" @click="handleTestConn(record)">测试连接</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="modalVisible" :title="modalTitle" @ok="handleSubmit" :confirm-loading="submitLoading" width="620px">
      <a-form ref="formRef" :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="数据源编码" name="dsCode" :rules="[{required:true,message:'请输入编码'}]">
          <a-input v-model:value="form.dsCode" :disabled="!!form.id" />
        </a-form-item>
        <a-form-item label="数据源名称" name="dsName" :rules="[{required:true,message:'请输入名称'}]">
          <a-input v-model:value="form.dsName" />
        </a-form-item>
        <a-form-item label="传感器类型" name="dsType" :rules="[{required:true,message:'请选择类型'}]">
          <a-select v-model:value="form.dsType">
            <a-select-option value="HTTP_PUSH">HTTP推送</a-select-option>
            <a-select-option value="MQTT">MQTT</a-select-option>
            <a-select-option value="TCP">TCP</a-select-option>
            <a-select-option value="UDP">UDP</a-select-option>
            <a-select-option value="FILE">文件</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="连接主机" name="connHost">
          <a-input v-model:value="form.connHost" placeholder="IP或域名" />
        </a-form-item>
        <a-form-item label="连接端口" name="connPort">
          <a-input-number v-model:value="form.connPort" style="width:100%" :min="1" :max="65535" />
        </a-form-item>
        <a-form-item label="认证类型" name="authType">
          <a-select v-model:value="form.authType">
            <a-select-option value="NONE">无</a-select-option>
            <a-select-option value="TOKEN">Token</a-select-option>
            <a-select-option value="BASIC">Basic</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="form.remark" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getDatasourceListApi, addDatasourceApi, editDatasourceApi, updateDatasourceStatusApi, testConnApi } from '@/api/datasource'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<any[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增数据源')
const formRef = ref()
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true })
const query = reactive({ keyword: '', status: undefined as number | undefined })
const emptyForm = () => ({ id: null as any, dsCode: '', dsName: '', dsType: '', connHost: '', connPort: null as any, authType: 'NONE', remark: '' })
const form = reactive<any>(emptyForm())

const columns = [
  { title: '编码', dataIndex: 'dsCode', key: 'dsCode', width: 140 },
  { title: '名称', dataIndex: 'dsName', key: 'dsName' },
  { title: '类型', dataIndex: 'dsType', key: 'dsType', width: 100 },
  { title: '连接地址', key: 'conn', width: 180 },
  { title: '连接状态', dataIndex: 'connStatus', key: 'connStatus', width: 90 },
  { title: '启用状态', dataIndex: 'status', key: 'status', width: 90 },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt', width: 160 },
  { title: '操作', key: 'action', width: 140 },
]

async function loadData() {
  loading.value = true
  try {
    const res: any = await getDatasourceListApi({ ...query, pageNum: pagination.current, pageSize: pagination.pageSize })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

function resetQuery() {
  Object.assign(query, { keyword: '', status: undefined })
  pagination.current = 1
  loadData()
}

function handleTableChange(pag: any) {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
}

function handleAdd() {
  modalTitle.value = '新增数据源'
  Object.assign(form, emptyForm())
  modalVisible.value = true
}

function handleEdit(record: any) {
  modalTitle.value = '编辑数据源'
  Object.assign(form, emptyForm(), record)
  modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    form.id ? await editDatasourceApi(form.id, form) : await addDatasourceApi(form)
    message.success('操作成功'); modalVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

async function handleStatusChange(record: any, val: boolean) {
  await updateDatasourceStatusApi(record.id, val ? 1 : 0)
  record.status = val ? 1 : 0
  message.success('状态已更新')
}

async function handleTestConn(record: any) {
  const hide = message.loading('测试连接中...')
  try {
    const res: any = await testConnApi(record.id)
    hide(); message.success(res.data?.message || '连接成功')
  } catch { hide() }
}

onMounted(loadData)
</script>
