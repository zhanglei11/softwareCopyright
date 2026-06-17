<template>
  <div>
    <a-card title="AI模型配置">
      <template #extra>
        <a-button type="primary" v-permission="'ai:model:create'" @click="openCreate"><PlusOutlined /> 新增模型</a-button>
      </template>
      <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-switch :checked="record.status === 1" @change="(v: boolean) => handleStatusChange(record.id, v ? 1 : 0)" />
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button size="small" v-permission="'ai:model:update'" @click="openEdit(record)">编辑</a-button>
              <a-button size="small" danger v-permission="'ai:model:delete'" @click="handleDelete(record.id)">删除</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:open="modalVisible" :title="editId ? '编辑模型' : '新增模型'" @ok="handleSubmit" :confirm-loading="submitting" width="640px">
      <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 6 }">
        <a-form-item label="模型名称" name="modelName"><a-input v-model:value="form.modelName" /></a-form-item>
        <a-form-item label="模型ID" name="modelId"><a-input v-model:value="form.modelId" /></a-form-item>
        <a-form-item label="提供商" name="provider"><a-input v-model:value="form.provider" /></a-form-item>
        <a-form-item label="API地址" name="apiUrl"><a-input v-model:value="form.apiUrl" /></a-form-item>
        <a-form-item label="API Key"><a-input-password v-model:value="form.apiKey" /></a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="form.remark" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getModelListApi, createModelApi, updateModelApi, deleteModelApi, updateModelStatusApi } from '@/api/ai/model'

const list = ref<any[]>([])
const loading = ref(false)
const submitting = ref(false)
const modalVisible = ref(false)
const editId = ref<number | null>(null)
const formRef = ref()
const form = reactive<any>({ modelName: '', modelId: '', provider: '', apiUrl: '', apiKey: '', remark: '' })
const rules = {
  modelName: [{ required: true, message: '请输入模型名称' }],
  modelId: [{ required: true, message: '请输入模型ID' }],
  provider: [{ required: true, message: '请输入提供商' }],
  apiUrl: [{ required: true, message: '请输入API地址' }],
}
const columns = [
  { title: '模型名称', dataIndex: 'modelName', key: 'modelName' },
  { title: '模型ID', dataIndex: 'modelId', key: 'modelId' },
  { title: '提供商', dataIndex: 'provider', key: 'provider' },
  { title: 'API地址', dataIndex: 'apiUrl', key: 'apiUrl', ellipsis: true },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action' },
]
const loadData = async () => {
  loading.value = true
  try { const res: any = await getModelListApi(); list.value = res.data?.list || res.data || [] }
  finally { loading.value = false }
}
const openCreate = () => { editId.value = null; Object.assign(form, { modelName: '', modelId: '', provider: '', apiUrl: '', apiKey: '', remark: '' }); modalVisible.value = true }
const openEdit = (r: any) => { editId.value = r.id; Object.assign(form, r); modalVisible.value = true }
const handleSubmit = async () => {
  await formRef.value?.validate(); submitting.value = true
  try {
    if (editId.value) await updateModelApi(editId.value, form)
    else await createModelApi(form)
    message.success('操作成功'); modalVisible.value = false; loadData()
  } finally { submitting.value = false }
}
const handleStatusChange = async (id: number, status: number) => {
  try { await updateModelStatusApi(id, status); message.success('状态更新成功'); loadData() }
  catch { /* ignore */ }
}
const handleDelete = (id: number) => Modal.confirm({ title: '确认删除？', onOk: async () => { await deleteModelApi(id); message.success('删除成功'); loadData() } })
onMounted(loadData)
</script>
