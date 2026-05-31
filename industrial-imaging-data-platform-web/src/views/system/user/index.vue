<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline" :model="query">
        <a-form-item label="用户名"><a-input v-model:value="query.username" allowClear /></a-form-item>
        <a-form-item><a-button type="primary" @click="load">查询</a-button></a-form-item>
      </a-form>
    </div>
    <div class="table-toolbar">
      <span>用户列表</span>
      <a-button type="primary" @click="showAdd"><PlusOutlined /> 新增</a-button>
    </div>
    <a-table :dataSource="list" :columns="columns" :loading="loading" rowKey="id" :pagination="pagination" @change="handlePageChange" size="middle" style="background:#fff;border-radius:8px">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-switch :checked="record.status === 1" @change="(v: boolean) => toggleStatus(record, v)" size="small" />
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="showEdit(record)">编辑</a-button>
            <a-button type="link" size="small" @click="resetPwd(record.id!)">重置密码</a-button>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id!)">
              <a-button type="link" size="small" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑用户' : '新增用户'" @ok="handleSave" :confirm-loading="saving">
      <a-form :model="form" :rules="rules" ref="formRef" :label-col="{span:5}">
        <a-form-item label="用户名" name="username"><a-input v-model:value="form.username" /></a-form-item>
        <a-form-item v-if="!form.id" label="密码" name="password"><a-input-password v-model:value="form.password" /></a-form-item>
        <a-form-item label="真实姓名"><a-input v-model:value="form.realName" /></a-form-item>
        <a-form-item label="邮箱"><a-input v-model:value="form.email" /></a-form-item>
        <a-form-item label="手机"><a-input v-model:value="form.phone" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getUserList, addUser, updateUser, deleteUser, updateUserStatus, resetPassword } from '@/api/system/user'
import type { SysUser } from '@/types'

const loading = ref(false); const saving = ref(false)
const list = ref<SysUser[]>([])
const modalVisible = ref(false); const formRef = ref()
const query = reactive({ username: '', pageNum: 1, pageSize: 10 })
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const form = reactive<Partial<SysUser> & { password?: string }>({})
const rules = { username: [{ required: true }] }
const columns = [
  { title: '用户名', dataIndex: 'username' },
  { title: '真实姓名', dataIndex: 'realName' },
  { title: '邮箱', dataIndex: 'email' },
  { title: '状态', key: 'status' },
  { title: '创建时间', dataIndex: 'createTime' },
  { title: '操作', key: 'action', width: 220 },
]
const load = async () => {
  loading.value = true
  try {
    const res = await getUserList({ username: query.username, pageNum: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data.rows; pagination.total = res.data.total
  } finally { loading.value = false }
}
const handlePageChange = (p: typeof pagination) => { pagination.current = p.current; pagination.pageSize = p.pageSize; load() }
const showAdd = () => { Object.assign(form, { id: undefined, username: '', password: '', realName: '', email: '', phone: '' }); modalVisible.value = true }
const showEdit = (r: SysUser) => { Object.assign(form, { ...r, password: '' }); modalVisible.value = true }
const handleSave = async () => {
  await formRef.value?.validate(); saving.value = true
  try {
    if (form.id) await updateUser(form.id!, form as SysUser)
    else await addUser(form as SysUser & { password: string })
    message.success('保存成功'); modalVisible.value = false; load()
  } finally { saving.value = false }
}
const handleDelete = async (id: number) => { await deleteUser(id); message.success('删除成功'); load() }
const toggleStatus = async (record: SysUser, v: boolean) => { await updateUserStatus(record.id!, v ? 1 : 0); record.status = v ? 1 : 0 }
const resetPwd = async (id: number) => {
  const newPwd = 'Reset@123'
  await resetPassword(id, { newPassword: newPwd })
  message.success(`密码已重置为: ${newPwd}`)
}
onMounted(load)
</script>
