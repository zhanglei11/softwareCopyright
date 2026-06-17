<template>
  <div>
    <a-card>
      <div class="search-bar" style="margin-bottom:16px">
        <a-space wrap>
          <a-input v-model:value="query.username" placeholder="用户名" allow-clear style="width:160px" @press-enter="loadData" />
          <a-input v-model:value="query.realName" placeholder="真实姓名" allow-clear style="width:160px" @press-enter="loadData" />
          <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:100px">
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="resetQuery"><reload-outlined />重置</a-button>
        </a-space>
      </div>
      <div style="margin-bottom:12px">
        <a-button type="primary" @click="handleAdd"><plus-outlined />新增用户</a-button>
      </div>
      <a-table :columns="columns" :data-source="tableData" :loading="loading" :pagination="pagination" row-key="id" @change="handleTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-switch :checked="record.status === 1" checked-children="正常" un-checked-children="禁用" @change="(v: boolean) => handleStatusChange(record, v)" />
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-button type="link" size="small" @click="handleResetPwd(record)">重置密码</a-button>
              <a-popconfirm title="确认删除该用户？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="modalVisible" :title="modalTitle" @ok="handleSubmit" :confirm-loading="submitLoading">
      <a-form ref="formRef" :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="用户名" name="username" :rules="[{required:true,message:'请输入用户名'}]">
          <a-input v-model:value="form.username" :disabled="!!form.id" />
        </a-form-item>
        <a-form-item label="真实姓名" name="realName">
          <a-input v-model:value="form.realName" />
        </a-form-item>
        <a-form-item v-if="!form.id" label="密码" name="password" :rules="[{required:true,message:'请输入密码'}]">
          <a-input-password v-model:value="form.password" />
        </a-form-item>
        <a-form-item label="手机号" name="phone">
          <a-input v-model:value="form.phone" />
        </a-form-item>
        <a-form-item label="部门" name="department">
          <a-input v-model:value="form.department" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-radio-group v-model:value="form.status">
            <a-radio :value="1">正常</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getUserListApi, addUserApi, editUserApi, deleteUserApi, updateUserStatusApi, resetPasswordApi } from '@/api/system/user'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<any[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增用户')
const formRef = ref()

const query = reactive({ username: '', realName: '', status: undefined as number | undefined, pageNum: 1, pageSize: 10 })
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showQuickJumper: true })
const form = reactive<any>({ id: null, username: '', realName: '', password: '', phone: '', department: '', status: 1 })

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '真实姓名', dataIndex: 'realName', key: 'realName' },
  { title: '手机号', dataIndex: 'phone', key: 'phone' },
  { title: '部门', dataIndex: 'department', key: 'department' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', width: 200 },
]

async function loadData() {
  loading.value = true
  try {
    const res: any = await getUserListApi({ ...query, pageNum: pagination.current, pageSize: pagination.pageSize })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

function resetQuery() {
  query.username = ''; query.realName = ''; query.status = undefined
  pagination.current = 1; loadData()
}

function handleTableChange(pag: any) {
  pagination.current = pag.current; pagination.pageSize = pag.pageSize; loadData()
}

function handleAdd() {
  modalTitle.value = '新增用户'
  Object.assign(form, { id: null, username: '', realName: '', password: '', phone: '', department: '', status: 1 })
  modalVisible.value = true
}

function handleEdit(record: any) {
  modalTitle.value = '编辑用户'
  Object.assign(form, { id: record.id, username: record.username, realName: record.realName, phone: record.phone, department: record.department, status: record.status, password: '' })
  modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await editUserApi(form.id, form)
    } else {
      await addUserApi(form)
    }
    message.success('操作成功')
    modalVisible.value = false
    loadData()
  } finally { submitLoading.value = false }
}

async function handleDelete(record: any) {
  await deleteUserApi(record.id)
  message.success('删除成功')
  loadData()
}

async function handleStatusChange(record: any, val: boolean) {
  await updateUserStatusApi(record.id, val ? 1 : 0)
  record.status = val ? 1 : 0
  message.success('状态已更新')
}

async function handleResetPwd(record: any) {
  await resetPasswordApi(record.id)
  message.success('密码已重置为默认密码')
}

onMounted(loadData)
</script>
