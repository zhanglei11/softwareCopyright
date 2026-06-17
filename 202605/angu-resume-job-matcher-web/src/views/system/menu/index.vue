<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { systemApi } from '@/api/system'

const loading = ref(false)
const saving = ref(false)
const modalOpen = ref(false)
const editingId = ref<number | null>(null)
const tableData = ref<any[]>([])
const form = reactive({ parentId: 0, menuType: 1, menuName: '', path: '', permCode: '', icon: '', sort: 1 })

const parentOptions = computed(() => {
  const walk = (nodes: any[]): any[] => nodes.map((node) => ({ label: node.menuName, value: node.id, children: walk(node.children || []) }))
  return [{ label: '根节点', value: 0 }, ...walk(tableData.value)]
})

function resetForm() {
  Object.assign(form, { parentId: 0, menuType: 1, menuName: '', path: '', permCode: '', icon: '', sort: 1 })
}

async function loadData() {
  loading.value = true
  try {
    const response: any = await systemApi.menuTree()
    tableData.value = response.data || []
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
  Object.assign(form, { parentId: record.parentId, menuType: record.menuType, menuName: record.menuName, path: record.path, permCode: record.permCode, icon: record.icon, sort: record.sort })
  modalOpen.value = true
}

async function saveMenu() {
  saving.value = true
  try {
    if (editingId.value) {
      await systemApi.updateMenu(editingId.value, { ...form })
    } else {
      await systemApi.createMenu({ ...form })
    }
    message.success('保存成功')
    modalOpen.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function removeMenu(id: number) {
  await systemApi.deleteMenu(id)
  message.success('删除成功')
  await loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>菜单管理</h2><p>展示并维护后端菜单树。</p></div>
    <a-card class="glass-card" :bordered="false"><div class="toolbar-row"><div /><div class="toolbar-actions"><a-button type="primary" v-permission="'system:menu:add'" @click="openCreate">新增菜单</a-button></div></div></a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="菜单名称" data-index="menuName" />
        <a-table-column title="类型"><template #default="{ record }">{{ ['目录', '菜单', '按钮'][record.menuType] || '-' }}</template></a-table-column>
        <a-table-column title="路由" data-index="path" />
        <a-table-column title="权限标识" data-index="permCode" />
        <a-table-column title="图标" data-index="icon" />
        <a-table-column title="操作" width="180"><template #default="{ record }"><a-space>
          <a-button type="link" v-permission="'system:menu:edit'" @click="openEdit(record)">编辑</a-button>
          <a-popconfirm title="确认删除该菜单吗？" @confirm="removeMenu(record.id)"><a-button type="link" danger v-permission="'system:menu:delete'">删除</a-button></a-popconfirm>
        </a-space></template></a-table-column>
      </a-table>
    </a-card>
    <a-modal v-model:open="modalOpen" :title="editingId ? '编辑菜单' : '新增菜单'" width="600" :confirm-loading="saving" @ok="saveMenu">
      <a-form layout="vertical">
        <a-form-item label="父级菜单"><a-tree-select v-model:value="form.parentId" :tree-data="parentOptions" tree-default-expand-all /></a-form-item>
        <div class="split-grid">
          <a-form-item label="类型"><a-select v-model:value="form.menuType"><a-select-option :value="0">目录</a-select-option><a-select-option :value="1">菜单</a-select-option><a-select-option :value="2">按钮</a-select-option></a-select></a-form-item>
          <a-form-item label="排序"><a-input-number v-model:value="form.sort" style="width: 100%" /></a-form-item>
        </div>
        <a-form-item label="菜单名称"><a-input v-model:value="form.menuName" /></a-form-item>
        <a-form-item label="路由 path"><a-input v-model:value="form.path" /></a-form-item>
        <a-form-item label="权限标识"><a-input v-model:value="form.permCode" /></a-form-item>
        <a-form-item label="图标"><a-input v-model:value="form.icon" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>