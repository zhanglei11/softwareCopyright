<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { systemApi } from '@/api/system'
import { pickList, pickPage } from '@/utils/common'

const loading = ref(false)
const saving = ref(false)
const modalOpen = ref(false)
const editingId = ref<number | null>(null)
const roles = ref<any[]>([])
const tableData = ref<any[]>([])
const total = ref(0)
const query = reactive({ username: '', phone: '', status: undefined as number | undefined, page: 1, size: 10 })
const form = reactive({ username: '', realName: '', phone: '', password: 'Admin@123', roleIds: [] as number[], status: 1 })
const roleOptions = computed(() => roles.value.map((role) => ({ label: role.roleName, value: role.id })))

function resetForm() {
  Object.assign(form, { username: '', realName: '', phone: '', password: 'Admin@123', roleIds: [], status: 1 })
}

async function loadData() {
  loading.value = true
  try {
    const [userResponse, roleResponse]: any = await Promise.all([systemApi.listUsers(query), systemApi.listRoles()])
    const pageData = pickPage(userResponse)
    tableData.value = pageData.list
    total.value = pageData.total
    roles.value = pickList(roleResponse)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingId.value = null
  resetForm()
  modalOpen.value = true
}

function openEdit(record: any) {
  editingId.value = record.id
  Object.assign(form, { username: record.username, realName: record.realName, phone: record.phone, password: 'Admin@123', roleIds: [], status: record.status })
  modalOpen.value = true
}

async function saveUser() {
  saving.value = true
  try {
    if (editingId.value) {
      await systemApi.updateUser(editingId.value, { realName: form.realName, phone: form.phone, roleIds: form.roleIds, status: form.status })
    } else {
      await systemApi.createUser({ ...form })
    }
    message.success('保存成功')
    modalOpen.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function toggleStatus(record: any) {
  await systemApi.updateUserStatus(record.id, record.status === 1 ? 0 : 1)
  message.success('状态已更新')
  await loadData()
}

async function removeUser(id: number) {
  await systemApi.deleteUser(id)
  message.success('删除成功')
  await loadData()
}

async function resetPassword(id: number) {
  const password = window.prompt('请输入新密码', 'Admin@123')
  if (!password) return
  await systemApi.resetPassword(id, password)
  message.success('密码已重置')
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>用户管理</h2><p>支持列表、新增、编辑、禁用、删除与密码重置。</p></div>
    <a-card class="glass-card" :bordered="false">
      <div class="toolbar-row">
        <div class="toolbar-filters">
          <a-input v-model:value="query.username" placeholder="用户名" style="width: 180px" />
          <a-input v-model:value="query.phone" placeholder="手机号" style="width: 180px" />
          <a-select v-model:value="query.status" allow-clear placeholder="状态" style="width: 140px">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </div>
        <div class="toolbar-actions">
          <a-button type="primary" @click="loadData">查询</a-button>
          <a-button v-permission="'system:user:add'" @click="openCreate">新增用户</a-button>
        </div>
      </div>
    </a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="用户名" data-index="username" />
        <a-table-column title="姓名" data-index="realName" />
        <a-table-column title="手机号" data-index="phone" />
        <a-table-column title="状态"><template #default="{ record }"><a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag></template></a-table-column>
        <a-table-column title="操作" width="280"><template #default="{ record }"><a-space>
          <a-button v-permission="'system:user:edit'" type="link" @click="openEdit(record)">编辑</a-button>
          <a-button v-permission="'system:user:edit'" type="link" @click="toggleStatus(record)">{{ record.status === 1 ? '禁用' : '启用' }}</a-button>
          <a-button v-permission="'system:user:edit'" type="link" @click="resetPassword(record.id)">重置密码</a-button>
          <a-popconfirm title="确认删除该用户吗？" @confirm="removeUser(record.id)"><a-button v-permission="'system:user:delete'" type="link" danger>删除</a-button></a-popconfirm>
        </a-space></template></a-table-column>
      </a-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 16px;"><a-pagination v-model:current="query.page" v-model:page-size="query.size" :total="total" :show-size-changer="true" @change="loadData" /></div>
    </a-card>
    <a-modal v-model:open="modalOpen" :title="editingId ? '编辑用户' : '新增用户'" width="680" :confirm-loading="saving" @ok="saveUser">
      <a-form layout="vertical">
        <div class="split-grid">
          <a-form-item label="用户名"><a-input v-model:value="form.username" :disabled="Boolean(editingId)" /></a-form-item>
          <a-form-item label="姓名"><a-input v-model:value="form.realName" /></a-form-item>
          <a-form-item label="手机号"><a-input v-model:value="form.phone" /></a-form-item>
          <a-form-item label="初始密码"><a-input-password v-model:value="form.password" :disabled="Boolean(editingId)" /></a-form-item>
        </div>
        <a-form-item label="角色"><a-select v-model:value="form.roleIds" mode="multiple" :options="roleOptions" placeholder="选择角色" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>