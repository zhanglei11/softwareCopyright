<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline" :model="query">
        <a-form-item label="名称"><a-input v-model:value="query.name" placeholder="数据源名称" allowClear /></a-form-item>
        <a-form-item label="类型">
          <a-select v-model:value="query.type" style="width:140px" allowClear placeholder="全部">
            <a-select-option value="FTP">FTP</a-select-option>
            <a-select-option value="SFTP">SFTP</a-select-option>
            <a-select-option value="SMB">SMB</a-select-option>
            <a-select-option value="S3">S3</a-select-option>
            <a-select-option value="LOCAL">LOCAL</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="load">查询</a-button>
          <a-button style="margin-left:8px" @click="resetQuery">重置</a-button>
        </a-form-item>
      </a-form>
    </div>

    <div class="table-toolbar">
      <span>数据源列表</span>
      <a-button type="primary" @click="showAdd"><PlusOutlined /> 新增</a-button>
    </div>

    <a-table :dataSource="list" :columns="columns" :pagination="pagination" :loading="loading" rowKey="id" @change="handlePageChange" size="middle" :bordered="false" style="background:#fff;border-radius:8px">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'type'">
          <a-tag>{{ record.type }}</a-tag>
        </template>
        <template v-if="column.key === 'enabled'">
          <a-switch :checked="record.enabled" @change="(v: boolean) => toggleStatus(record, v)" size="small" />
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="testConn(record)">测试</a-button>
            <a-button type="link" size="small" @click="showEdit(record)">编辑</a-button>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id!)">
              <a-button type="link" size="small" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 新增/编辑弹窗 -->
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑数据源' : '新增数据源'" @ok="handleSave" :confirm-loading="saving" width="600px">
      <a-form :model="form" :rules="formRules" ref="formRef" label-col="{span:5}">
        <a-form-item label="名称" name="name"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="类型" name="type">
          <a-select v-model:value="form.type">
            <a-select-option v-for="t in ['FTP','SFTP','SMB','S3','LOCAL']" :key="t" :value="t">{{ t }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="主机" name="host"><a-input v-model:value="form.host" /></a-form-item>
        <a-form-item label="端口" name="port"><a-input-number v-model:value="form.port" style="width:100%" /></a-form-item>
        <a-form-item label="路径" name="basePath"><a-input v-model:value="form.basePath" /></a-form-item>
        <a-form-item label="用户名"><a-input v-model:value="form.username" /></a-form-item>
        <a-form-item label="密码"><a-input-password v-model:value="form.password" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.description" :rows="2" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getDatasourceList, addDatasource, updateDatasource, deleteDatasource, testDatasourceConn as testConnection, updateDatasourceStatus } from '@/api/datasource'
import type { DatasourceConfig } from '@/types'

const loading = ref(false)
const saving = ref(false)
const list = ref<DatasourceConfig[]>([])
const modalVisible = ref(false)
const formRef = ref()

const query = reactive({ name: '', type: undefined as string | undefined, pageNum: 1, pageSize: 10 })
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })

const form = reactive<Partial<DatasourceConfig>>({})
const formRules = {
  name: [{ required: true, message: '请输入名称' }],
  type: [{ required: true, message: '请选择类型' }],
  host: [{ required: true, message: '请输入主机' }],
}

const columns = [
  { title: '名称', dataIndex: 'name', key: 'name' },
  { title: '类型', dataIndex: 'type', key: 'type' },
  { title: '主机', dataIndex: 'host', key: 'host' },
  { title: '端口', dataIndex: 'port', key: 'port' },
  { title: '路径', dataIndex: 'basePath', key: 'basePath' },
  { title: '状态', dataIndex: 'enabled', key: 'enabled' },
  { title: '操作', key: 'action', width: 160 },
]

const load = async () => {
  loading.value = true
  try {
    const res = await getDatasourceList({ name: query.name, type: query.type, pageNum: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data.rows
    pagination.total = res.data.total
  } finally { loading.value = false }
}

const resetQuery = () => { query.name = ''; query.type = undefined; pagination.current = 1; load() }
const handlePageChange = (p: typeof pagination) => { pagination.current = p.current; pagination.pageSize = p.pageSize; load() }

const showAdd = () => { Object.assign(form, { id: undefined, name: '', type: 'FTP', host: '', port: 21, basePath: '/', username: '', password: '', description: '' }); modalVisible.value = true }
const showEdit = (record: DatasourceConfig) => { Object.assign(form, { ...record }); modalVisible.value = true }

const handleSave = async () => {
  await formRef.value?.validate()
  saving.value = true
  try {
    if (form.id) { await updateDatasource(form.id, form as DatasourceConfig) } else { await addDatasource(form as DatasourceConfig) }
    message.success('保存成功')
    modalVisible.value = false
    load()
  } finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  await deleteDatasource(id)
  message.success('删除成功')
  load()
}

const toggleStatus = async (record: DatasourceConfig, enabled: boolean) => {
  await updateDatasourceStatus(record.id!, enabled ? 1 : 0)
  record.enabled = enabled
}

const testConn = async (record: DatasourceConfig) => {
  try {
    await testConnection(record.id!)
    message.success('连接成功')
  } catch { message.error('连接失败') }
}

onMounted(load)
</script>
