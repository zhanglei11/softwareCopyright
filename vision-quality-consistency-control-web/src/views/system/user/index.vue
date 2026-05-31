<template>
  <div>
    <a-form layout="inline" :model="query" style="margin-bottom:16px" @finish="loadData">
      <a-form-item><a-input v-model:value="query.username" placeholder="账号" allow-clear /></a-form-item>
      <a-form-item><a-input v-model:value="query.realName" placeholder="姓名" allow-clear /></a-form-item>
      <a-form-item>
        <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:100px">
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="0">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-space>
          <a-button type="primary" html-type="submit">查询</a-button>
          <a-button @click="onReset">重置</a-button>
        </a-space>
      </a-form-item>
    </a-form>
    <div style="margin-bottom:12px">
      <a-button type="primary" @click="openModal()">新增用户</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" :pagination="pagination" @change="onTableChange" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="openModal(record)">编辑</a-button>
            <a-popconfirm title="确认重置密码?" @confirm="onResetPwd(record.id)"><a-button size="small" type="link">重置密码</a-button></a-popconfirm>
            <a-popconfirm :title="`确认${record.status===1?'禁用':'启用'}?`" @confirm="onToggle(record)">
              <a-button size="small" type="link" :danger="record.status===1">{{ record.status === 1 ? '禁用' : '启用' }}</a-button>
            </a-popconfirm>
            <a-popconfirm title="确认删除?" @confirm="onDelete(record.id)"><a-button size="small" type="link" danger>删除</a-button></a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalOpen" :title="editId ? '编辑用户' : '新增用户'" @ok="onSave" :confirm-loading="saving">
      <a-form :model="form" label-col="{ span: 6 }" wrapper-col="{ span: 16 }">
        <a-form-item label="登录账号"><a-input v-model:value="form.username" :disabled="!!editId" /></a-form-item>
        <a-form-item label="姓名"><a-input v-model:value="form.realName" /></a-form-item>
        <a-form-item label="手机号"><a-input v-model:value="form.phone" /></a-form-item>
        <a-form-item label="部门"><a-input v-model:value="form.dept" /></a-form-item>
        <a-form-item v-if="!editId" label="初始密码"><a-input-password v-model:value="form.password" /></a-form-item>
        <a-form-item label="角色">
          <a-select v-model:value="form.roleIds" mode="multiple" :options="roleOptions" placeholder="选择角色" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { userApi } from '@/api/system/user'
import { roleApi } from '@/api/system/role'

const columns = [
  { title: '账号', dataIndex: 'username', key: 'username' },
  { title: '姓名', dataIndex: 'realName', key: 'realName' },
  { title: '手机号', dataIndex: 'phone', key: 'phone' },
  { title: '部门', dataIndex: 'dept', key: 'dept' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', key: 'createdAt', customRender: ({ record }: any) => record.createdAt ? record.createdAt.replace('T', ' ').slice(0, 19) : '-' },
  { title: '操作', key: 'action' },
]
const query = reactive({ username: '', realName: '', status: undefined as number | undefined })
const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive({ username: '', realName: '', phone: '', dept: '', password: '', roleIds: [] as number[] })
const roleOptions = ref<any[]>([])

async function loadData() {
  loading.value = true
  try {
    const res: any = await userApi.list({ ...query, page: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data?.rows || res.data?.list || res.data?.records || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

function onReset() { Object.assign(query, { username: '', realName: '', status: undefined }); loadData() }
function onTableChange(p: any) { pagination.current = p.current; loadData() }

function openModal(row?: any) {
  editId.value = row?.id || null
  Object.assign(form, row ? { ...row } : { username: '', realName: '', phone: '', dept: '', password: '', roleIds: [] })
  modalOpen.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (editId.value) await userApi.update(editId.value, form)
    else await userApi.create(form)
    message.success('保存成功')
    modalOpen.value = false
    loadData()
  } finally { saving.value = false }
}

async function onResetPwd(id: number) {
  await userApi.resetPassword(id)
  message.success('密码已重置为 Abc@12345')
}

async function onToggle(row: any) {
  await userApi.toggleStatus(row.id, row.status === 1 ? 0 : 1)
  message.success('操作成功')
  loadData()
}

async function onDelete(id: number) {
  await userApi.remove(id)
  message.success('删除成功')
  loadData()
}

onMounted(async () => {
  loadData()
  const res: any = await roleApi.list()
  roleOptions.value = (res.data?.rows || res.data?.rows || res.data?.list || res.data || []).map((r: any) => ({ label: r.roleName, value: r.id }))
})
</script>
