<template>
  <a-card title="用户管理">
    <div style="margin-bottom:12px">
      <a-button type="primary" @click="handleAdd"><PlusOutlined />新增</a-button>
    </div>
    <a-table :columns="columns" :data-source="tableData" :loading="loading"
      :pagination="pagination" row-key="id" @change="handleTableChange">
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
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑用户' : '新增用户'" @ok="submitForm" :confirm-loading="submitLoading">
      <a-form :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="登录账号" required><a-input v-model:value="form.username" :disabled="!!form.id" /></a-form-item>
        <a-form-item label="真实姓名" required><a-input v-model:value="form.realName" /></a-form-item>
        <a-form-item label="手机号" required><a-input v-model:value="form.phone" placeholder="13xxxxxxxxx" /></a-form-item>
        <a-form-item label="部门"><a-input v-model:value="form.department" /></a-form-item>
        <a-form-item v-if="!form.id" label="登录密码" required><a-input-password v-model:value="form.password" placeholder="至少8位" /></a-form-item>
        <a-form-item label="角色" required>
          <a-select v-model:value="form.roleIds" mode="multiple" placeholder="请选择角色">
            <a-select-option v-for="role in roleOptions" :key="role.id" :value="role.id">{{ role.roleName }}</a-select-option>
          </a-select>
        </a-form-item>
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
import { getUserList, addUser, updateUser, deleteUser } from '@/api/system/user'
import request from '@/utils/request'
const loading = ref(false)
const tableData = ref<any[]>([])
const roleOptions = ref<any[]>([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const columns = [
  { title: '登录账号', dataIndex: 'username' },
  { title: '真实姓名', dataIndex: 'realName' },
  { title: '手机号', dataIndex: 'phone' },
  { title: '部门', dataIndex: 'department' },
  { title: '状态', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', customRender: ({value}: any) => value?.replace('T', ' ').slice(0, 19) || '-' },
  { title: '操作', key: 'action', width: 120 },
]
const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserList({ page: pagination.current, pageSize: pagination.pageSize })
    const d = (res as any).data ?? res as any
    tableData.value = d.rows ?? d.records ?? []
    pagination.total = d.total ?? tableData.value.length
  } catch {} finally { loading.value = false }
}
const loadRoles = async () => {
  try {
    const res = await request.get('/api/v1/system/roles', { params: { pageSize: 100 } })
    roleOptions.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {}
}
const handleTableChange = (p: any) => { pagination.current = p.current; pagination.pageSize = p.pageSize; loadData() }
const modalVisible = ref(false)
const submitLoading = ref(false)
const form = reactive<any>({ username: '', realName: '', phone: '', department: '', password: '', roleIds: [], status: 1 })
const handleAdd = () => {
  Object.assign(form, { id: undefined, username: '', realName: '', phone: '', department: '', password: '', roleIds: [], status: 1 })
  modalVisible.value = true
}
const handleEdit = (record: any) => {
  Object.assign(form, record, { password: '', roleIds: record.roleIds ?? [] })
  modalVisible.value = true
}
const handleDelete = async (id: number) => {
  try { await deleteUser(id); message.success('删除成功'); loadData() } catch {}
}
const submitForm = async () => {
  if (!form.username) { message.warning('请输入登录账号'); return }
  if (!form.realName) { message.warning('请输入真实姓名'); return }
  if (!form.phone) { message.warning('请输入手机号'); return }
  if (!form.id && !form.password) { message.warning('请输入登录密码'); return }
  if (!form.roleIds?.length) { message.warning('请选择角色'); return }
  submitLoading.value = true
  const payload: any = {
    username: form.username,
    realName: form.realName,
    phone: form.phone,
    department: form.department || '',
    roleIds: form.roleIds,
    status: form.status
  }
  if (!form.id) payload.password = form.password
  try {
    if (form.id) { await updateUser(form.id, payload); message.success('更新成功') }
    else { await addUser(payload); message.success('新增成功') }
    modalVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}
onMounted(() => { loadData(); loadRoles() })
</script>
