<template>
  <a-card title="产线管理">
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
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑产线' : '新增产线'" @ok="submitForm" :confirm-loading="submitLoading">
      <a-form :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="产线名称" required><a-input v-model:value="form.lineName" /></a-form-item>
        <a-form-item label="产线编号" required><a-input v-model:value="form.lineNo" placeholder="如: L001" /></a-form-item>
        <a-form-item label="所属车间"><a-input v-model:value="form.workshop" /></a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item>
        <a-form-item label="状态"><a-switch v-model:checked="form.enabled" /></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getLineList, addLine, updateLine, deleteLine } from '@/api/line'
const loading = ref(false)
const tableData = ref<any[]>([])
const columns = [
  { title: '产线名称', dataIndex: 'lineName' },
  { title: '产线编号', dataIndex: 'lineNo' },
  { title: '所属车间', dataIndex: 'workshop' },
  { title: '备注', dataIndex: 'remark' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action', width: 120 },
]
const loadData = async () => {
  loading.value = true
  try {
    const res = await getLineList({})
    tableData.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {} finally { loading.value = false }
}
const modalVisible = ref(false)
const submitLoading = ref(false)
const form = reactive<any>({ lineName: '', lineNo: '', workshop: '', remark: '', enabled: true })
const handleAdd = () => {
  Object.assign(form, { id: undefined, lineName: '', lineNo: '', workshop: '', remark: '', enabled: true })
  modalVisible.value = true
}
const handleEdit = (record: any) => {
  Object.assign(form, record, { enabled: record.status === 1 })
  modalVisible.value = true
}
const handleDelete = async (id: number) => {
  try { await deleteLine(id); message.success('删除成功'); loadData() } catch {}
}
const submitForm = async () => {
  if (!form.lineName) { message.warning('请输入产线名称'); return }
  if (!form.lineNo) { message.warning('请输入产线编号'); return }
  submitLoading.value = true
  const payload = {
    lineNo: form.lineNo,
    lineName: form.lineName,
    workshop: form.workshop || '',
    managerId: 1,
    remark: form.remark || '',
    status: form.enabled ? 1 : 0
  }
  try {
    if (form.id) { await updateLine(form.id, payload); message.success('更新成功') }
    else { await addLine(payload); message.success('新增成功') }
    modalVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}
onMounted(loadData)
</script>
