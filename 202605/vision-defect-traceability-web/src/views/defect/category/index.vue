<template>
  <a-card title="缺陷分类管理">
    <div style="margin-bottom:12px">
      <a-button type="primary" @click="handleAdd"><PlusOutlined />新增</a-button>
    </div>
    <a-table :columns="columns" :data-source="tableData" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
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
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑分类' : '新增分类'" @ok="submitForm" :confirm-loading="submitLoading">
      <a-form :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="分类名称" required><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="分类编码"><a-input v-model:value="form.code" placeholder="如: DEF-SCRATCH" /></a-form-item>
        <a-form-item label="缺陷等级" required>
          <a-select v-model:value="form.level" placeholder="请选择等级">
            <a-select-option :value="1">轻微</a-select-option>
            <a-select-option :value="2">一般</a-select-option>
            <a-select-option :value="3">严重</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.description" :rows="3" /></a-form-item>
        <a-form-item label="状态">
          <a-switch v-model:checked="form.enabled" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/defect/category'
import type { DefectCategory } from '@/types'
const loading = ref(false)
const tableData = ref<DefectCategory[]>([])
const columns = [
  { title: '分类名称', dataIndex: 'name' },
  { title: '分类编码', dataIndex: 'code' },
  { title: '等级', dataIndex: 'level', customRender: ({value}: any) => ['','轻微','一般','严重'][value] || value },
  { title: '描述', dataIndex: 'description' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action', width: 120 },
]
const loadData = async () => {
  loading.value = true
  try { const res = await getCategoryList({}); tableData.value = (res as any).data?.rows ?? (res as any).data?.data ?? (res as any).rows ?? [] } catch {} finally { loading.value = false }
}
const modalVisible = ref(false)
const submitLoading = ref(false)
const form = reactive<any>({ name: '', code: '', level: 1, description: '', enabled: true, status: 1 })
const handleAdd = () => { Object.assign(form, { id: undefined, name: '', code: '', level: 1, description: '', enabled: true, status: 1 }); modalVisible.value = true }
const handleEdit = (record: any) => { Object.assign(form, record, { enabled: record.status === 1 }); modalVisible.value = true }
const handleDelete = async (id: number) => {
  try { await deleteCategory(id); message.success('删除成功'); loadData() } catch {}
}
const submitForm = async () => {
  if (!form.name) { message.warning('请输入分类名称'); return }
  if (!form.level) { message.warning('请选择缺陷等级'); return }
  submitLoading.value = true
  const payload = { name: form.name, code: form.code || `DEF-${form.name}`, level: form.level, description: form.description, status: form.enabled ? 1 : 0 }
  try {
    if (form.id) { await updateCategory(form.id, payload); message.success('更新成功') }
    else { await addCategory(payload); message.success('新增成功') }
    modalVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}
onMounted(loadData)
</script>
