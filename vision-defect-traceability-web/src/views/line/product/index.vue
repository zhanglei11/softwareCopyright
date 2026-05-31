<template>
  <a-card title="产品型号管理">
    <div style="margin-bottom:12px">
      <a-button type="primary" @click="handleAdd"><PlusOutlined />新增</a-button>
    </div>
    <a-table :columns="columns" :data-source="tableData" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'lineId'">
          {{ lineOptions.find((l: any) => l.id === record.lineId)?.lineName || '-' }}
        </template>
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
          <a-popconfirm title="确认删除?" @confirm="handleDelete(record.id)">
            <a-button type="link" size="small" danger>删除</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑型号' : '新增型号'" @ok="submitForm" :confirm-loading="submitLoading">
      <a-form :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="型号名称" required><a-input v-model:value="form.typeName" /></a-form-item>
        <a-form-item label="型号编码"><a-input v-model:value="form.typeNo" placeholder="如: T001" /></a-form-item>
        <a-form-item label="关联产线">
          <a-select v-model:value="form.lineId" allow-clear placeholder="请选择产线">
            <a-select-option v-for="line in lineOptions" :key="line.id" :value="line.id">{{ line.lineName }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态"><a-switch v-model:checked="form.enabled" /></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getProductList, addProduct, updateProduct, deleteProduct } from '@/api/line/product'
import { getLineList } from '@/api/line'
const loading = ref(false)
const tableData = ref<any[]>([])
const lineOptions = ref<any[]>([])
const columns = [
  { title: '型号名称', dataIndex: 'typeName' },
  { title: '型号编码', dataIndex: 'typeNo' },
  { title: '关联产线', key: 'lineId', dataIndex: 'lineId' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action', width: 120 },
]
const loadData = async () => {
  loading.value = true
  try {
    const res = await getProductList({})
    tableData.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {} finally { loading.value = false }
}
const loadLines = async () => {
  try {
    const res = await getLineList({})
    lineOptions.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {}
}
const modalVisible = ref(false)
const submitLoading = ref(false)
const form = reactive<any>({ typeName: '', typeNo: '', lineId: undefined, enabled: true })
const handleAdd = () => {
  Object.assign(form, { id: undefined, typeName: '', typeNo: '', lineId: undefined, enabled: true })
  modalVisible.value = true
}
const handleEdit = (record: any) => {
  Object.assign(form, record, { enabled: record.status === 1 })
  modalVisible.value = true
}
const handleDelete = async (id: number) => {
  try { await deleteProduct(id); message.success('删除成功'); loadData() } catch {}
}
const submitForm = async () => {
  if (!form.typeName) { message.warning('请输入型号名称'); return }
  submitLoading.value = true
  const payload = {
    typeNo: form.typeNo || `T-${form.typeName}`,
    typeName: form.typeName,
    lineId: form.lineId,
    status: form.enabled ? 1 : 0
  }
  try {
    if (form.id) { await updateProduct(form.id, payload); message.success('更新成功') }
    else { await addProduct(payload); message.success('新增成功') }
    modalVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}
onMounted(() => { loadData(); loadLines() })
</script>
