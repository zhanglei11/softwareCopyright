<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline">
        <a-form-item label="用户名">
          <a-input v-model:value="query.username" placeholder="请输入用户名" allow-clear />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="query.status" style="width: 120px" allow-clear placeholder="全部">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="fetchData"><SearchOutlined /> 查询</a-button>
            <a-button @click="resetQuery"><ReloadOutlined /> 重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
    <div class="table-toolbar">
      <span>用户列表</span>
      <a-button type="primary" v-permission="'system:user:add'" @click="openAdd">
        <PlusOutlined /> 新增
      </a-button>
    </div>
    <a-table
      :columns="columns"
      :data-source="dataList"
      :loading="loading"
      :pagination="pagination"
      row-key="id"
      @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'success' : 'error'">
            {{ record.status === 1 ? '启用' : '禁用' }}
          </a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" v-permission="'system:user:edit'" @click="openEdit(record)">编辑</a-button>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id)">
              <a-button size="small" type="link" danger v-permission="'system:user:delete'">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 新增/编辑弹窗 -->
    <a-modal v-model:open="modalVisible" :title="isEdit ? '编辑用户' : '新增用户'" @ok="handleSubmit" :confirm-loading="submitLoading">
      <a-form :model="formData" :rules="rules" ref="formRef" label-col="{ span: 6 }">
        <a-form-item label="用户名" name="username">
          <a-input v-model:value="formData.username" :disabled="isEdit" />
        </a-form-item>
        <a-form-item label="真实姓名" name="realName">
          <a-input v-model:value="formData.realName" />
        </a-form-item>
        <a-form-item label="手机号" name="phone">
          <a-input v-model:value="formData.phone" />
        </a-form-item>
        <a-form-item label="邮箱" name="email">
          <a-input v-model:value="formData.email" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-radio-group v-model:value="formData.status">
            <a-radio :value="1">启用</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getUserListApi, createUserApi, updateUserApi, deleteUserApi } from '@/api/system/user'

const loading = ref(false)
const dataList = ref<any[]>([])
const query = reactive({ username: '', status: undefined as number | undefined })
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const modalVisible = ref(false)
const submitLoading = ref(false)
const isEdit = ref(false)
const formRef = ref()
const formData = reactive<any>({ username: '', realName: '', phone: '', email: '', status: 1 })

const rules = {
  username: [{ required: true, message: '请输入用户名' }],
}

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '真实姓名', dataIndex: 'realName', key: 'realName' },
  { title: '手机号', dataIndex: 'phone', key: 'phone' },
  { title: '邮箱', dataIndex: 'email', key: 'email' },
  { title: '状态', key: 'status' },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
  { title: '操作', key: 'action', width: 150 },
]

async function fetchData() {
  loading.value = true
  try {
    const res = await getUserListApi({ pageNum: pagination.current, pageSize: pagination.pageSize, ...query })
    dataList.value = res.data?.rows || res.data?.list || []
    pagination.total = res.data?.total || 0
  } catch (e) {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.username = ''
  query.status = undefined
  fetchData()
}

function handleTableChange(page: any) {
  pagination.current = page.current
  pagination.pageSize = page.pageSize
  fetchData()
}

function openAdd() {
  isEdit.value = false
  Object.assign(formData, { id: undefined, username: '', realName: '', phone: '', email: '', status: 1 })
  modalVisible.value = true
}

function openEdit(record: any) {
  isEdit.value = true
  Object.assign(formData, record)
  modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateUserApi(formData.id, formData)
    } else {
      await createUserApi(formData)
    }
    message.success('操作成功')
    modalVisible.value = false
    fetchData()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(id: string | number) {
  await deleteUserApi(id)
  message.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>
