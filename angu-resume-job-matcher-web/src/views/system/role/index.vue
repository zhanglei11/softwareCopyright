<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { systemApi } from '@/api/system'
import { pickList } from '@/utils/common'

const loading = ref(false)
const saving = ref(false)
const modalOpen = ref(false)
const authOpen = ref(false)
const editingId = ref<number | null>(null)
const authRole = ref<any>(null)
const tableData = ref<any[]>([])
const menuTree = ref<any[]>([])
const checkedMenuIds = ref<number[]>([])
const form = reactive({ roleName: '', roleCode: '', status: 1, remark: '' })

const treeData = computed(() => {
  const walk = (nodes: any[]): any[] => nodes.map((node) => ({ title: node.menuName, key: node.id, children: walk(node.children || []) }))
  return walk(menuTree.value)
})

function resetForm() {
  Object.assign(form, { roleName: '', roleCode: '', status: 1, remark: '' })
}

async function loadData() {
  loading.value = true
  try {
    const [rolesResponse, menuResponse]: any = await Promise.all([systemApi.listRoles(), systemApi.menuTree()])
    tableData.value = pickList(rolesResponse)
    menuTree.value = menuResponse.data || []
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
  Object.assign(form, record)
  modalOpen.value = true
}

async function saveRole() {
  saving.value = true
  try {
    if (editingId.value) {
      await systemApi.updateRole(editingId.value, { ...form })
    } else {
      await systemApi.createRole({ ...form })
    }
    message.success('保存成功')
    modalOpen.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function removeRole(id: number) {
  await systemApi.deleteRole(id)
  message.success('删除成功')
  await loadData()
}

function openAuthorization(record: any) {
  authRole.value = record
  checkedMenuIds.value = []
  authOpen.value = true
}

async function saveAuthorization() {
  if (!authRole.value) return
  await systemApi.assignMenus(authRole.value.id, checkedMenuIds.value)
  message.success('授权已保存')
  authOpen.value = false
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>角色管理</h2><p>覆盖角色列表、增删改和菜单授权入口。</p></div>
    <a-card class="glass-card" :bordered="false"><div class="toolbar-row"><div /><div class="toolbar-actions"><a-button type="primary" v-permission="'system:role:add'" @click="openCreate">新增角色</a-button></div></div></a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="角色名称" data-index="roleName" />
        <a-table-column title="角色标识" data-index="roleCode" />
        <a-table-column title="状态"><template #default="{ record }"><a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag></template></a-table-column>
        <a-table-column title="备注" data-index="remark" />
        <a-table-column title="操作" width="220"><template #default="{ record }"><a-space>
          <a-button type="link" v-permission="'system:role:edit'" @click="openEdit(record)">编辑</a-button>
          <a-button type="link" v-permission="'system:role:edit'" @click="openAuthorization(record)">授权</a-button>
          <a-popconfirm title="确认删除该角色吗？" @confirm="removeRole(record.id)"><a-button type="link" danger v-permission="'system:role:delete'">删除</a-button></a-popconfirm>
        </a-space></template></a-table-column>
      </a-table>
    </a-card>
    <a-modal v-model:open="modalOpen" :title="editingId ? '编辑角色' : '新增角色'" width="560" :confirm-loading="saving" @ok="saveRole">
      <a-form layout="vertical">
        <a-form-item label="角色名称"><a-input v-model:value="form.roleName" /></a-form-item>
        <a-form-item label="角色标识"><a-input v-model:value="form.roleCode" :disabled="Boolean(editingId)" /></a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="form.remark" :auto-size="{ minRows: 3, maxRows: 5 }" /></a-form-item>
      </a-form>
    </a-modal>
    <a-drawer v-model:open="authOpen" title="菜单授权" width="420">
      <p class="muted-text">当前角色：{{ authRole?.roleName }}</p>
      <a-tree v-model:checkedKeys="checkedMenuIds" checkable :tree-data="treeData" />
      <div style="margin-top: 18px; display: flex; justify-content: flex-end;"><a-button type="primary" @click="saveAuthorization">保存授权</a-button></div>
    </a-drawer>
  </div>
</template>