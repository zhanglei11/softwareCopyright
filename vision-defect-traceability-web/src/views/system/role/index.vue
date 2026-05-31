<template>
  <a-card title="角色管理">
    <div style="margin-bottom:12px">
      <a-button type="primary" @click="handleAdd"><PlusOutlined />新增</a-button>
    </div>
    <a-table :columns="columns" :data-source="tableData" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'red'">{{ record.status === 1 ? '正常' : '禁用' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
          <a-popconfirm title="确认删除?" @confirm="handleDelete(record.id)">
            <a-button type="link" size="small" danger>删除</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑角色' : '新增角色'" @ok="submitForm" :confirm-loading="submitLoading">
      <a-form :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="角色名称" required><a-input v-model:value="form.roleName" /></a-form-item>
        <a-form-item label="角色标识" required><a-input v-model:value="form.roleKey" placeholder="如: operator" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="form.status">
            <a-radio :value="1">正常</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/system/role'
const loading = ref(false)
const tableData = ref<any[]>([])
const columns = [
  { title: '角色名称', dataIndex: 'roleName' },
  { title: '角色标识', dataIndex: 'roleKey' },
  { title: '描述', dataIndex: 'remark' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action', width: 120 },
]
const loadData = async () => {
  loading.value = true
  try {
    const res = await getRoleList({})
    tableData.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {} finally { loading.value = false }
}
const modalVisible = ref(false)
const submitLoading = ref(false)
const form = reactive<any>({ roleName: '', roleKey: '', remark: '', status: 1 })
const handleAdd = () => {
  Object.assign(form, { id: undefined, roleName: '', roleKey: '', remark: '', status: 1 })
  modalVisible.value = true
}
const handleEdit = (record: any) => {
  Object.assign(form, record)
  modalVisible.value = true
}
const handleDelete = async (id: number) => {
  try { await deleteRole(id); message.success('删除成功'); loadData() } catch {}
}
const submitForm = async () => {
  if (!form.roleName) { message.warning('请输入角色名称'); return }
  if (!form.roleKey) { message.warning('请输入角色标识'); return }
  submitLoading.value = true
  const payload = { roleName: form.roleName, roleKey: form.roleKey, remark: form.remark || '', status: form.status }
  try {
    if (form.id) { await updateRole(form.id, payload); message.success('更新成功') }
    else { await addRole(payload); message.success('新增成功') }
    modalVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}
onMounted(loadData)
</script>
