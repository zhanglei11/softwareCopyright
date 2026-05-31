<template>
  <a-card :bordered="false" style="border-radius:8px">
    <div class="page-header">
      <a-space>
        <a-input v-model:value="query.keyword" placeholder="用户名/姓名" style="width:180px" @press-enter="loadList" />
        <a-button type="primary" @click="loadList">查询</a-button>
        <a-button @click="resetQuery">重置</a-button>
      </a-space>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增用户</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="pagination" @change="handleTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a @click="openEdit(record)">编辑</a>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id)">
              <a style="color:#ff4d4f">删除</a>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </a-card>
  <a-modal v-model:open="modalOpen" :title="editId ? '编辑用户' : '新增用户'" @ok="handleSubmit" :confirm-loading="saving">
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="用户名" required><a-input v-model:value="form.username" /></a-form-item>
      <a-form-item label="真实姓名"><a-input v-model:value="form.realName" /></a-form-item>
      <a-form-item v-if="!editId" label="密码" required><a-input-password v-model:value="form.password" /></a-form-item>
      <a-form-item label="邮箱"><a-input v-model:value="form.email" /></a-form-item>
      <a-form-item label="手机"><a-input v-model:value="form.phone" /></a-form-item>
      <a-form-item label="状态">
        <a-switch v-model:checked="formStatusBool" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getUserListApi, addUserApi, editUserApi, deleteUserApi } from '@/api/system'

const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const query = reactive({ keyword: '' })
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ username: '', realName: '', password: '', email: '', phone: '', status: 1 })
const formStatusBool = computed({ get: () => form.status === 1, set: (v: boolean) => { form.status = v ? 1 : 0 } })

const columns = [
  { title: '用户名', dataIndex: 'username' },
  { title: '真实姓名', dataIndex: 'realName' },
  { title: '邮箱', dataIndex: 'email' },
  { title: '手机', dataIndex: 'phone' },
  { title: '状态', key: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createTime', width: 165 },
  { title: '操作', key: 'action', width: 100 },
]

async function loadList() {
  loading.value = true
  try {
    const res: any = await getUserListApi({ ...query, page: pagination.current, size: pagination.pageSize })
    const d = res.data
    list.value = d.records || d || []
    pagination.total = d.total || list.value.length
  } finally { loading.value = false }
}

function handleTableChange(p: any) { pagination.current = p.current; pagination.pageSize = p.pageSize; loadList() }
function resetQuery() { query.keyword = ''; loadList() }
function openAdd() { editId.value = null; Object.assign(form, { username: '', realName: '', password: '', email: '', phone: '', status: 1 }); modalOpen.value = true }
function openEdit(record: any) { editId.value = record.id; Object.assign(form, { ...record, password: '' }); modalOpen.value = true }

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) { await editUserApi(editId.value, form); message.success('编辑成功') }
    else { await addUserApi(form); message.success('新增成功') }
    modalOpen.value = false; loadList()
  } finally { saving.value = false }
}

async function handleDelete(id: number) { await deleteUserApi(id); message.success('删除成功'); loadList() }

onMounted(loadList)
</script>
