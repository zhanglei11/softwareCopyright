<template>
  <div>
    <div style="margin-bottom:12px"><a-button type="primary" @click="openModal()">新增角色</a-button></div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="openModal(record)">编辑</a-button>
            <a-button size="small" type="link" @click="openAssign(record)">授权</a-button>
            <a-popconfirm title="确认删除?" @confirm="onDelete(record.id)">
              <a-button size="small" type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalOpen" :title="editId ? '编辑角色' : '新增角色'" @ok="onSave" :confirm-loading="saving">
      <a-form :model="form">
        <a-form-item label="角色名称"><a-input v-model:value="form.roleName" /></a-form-item>
        <a-form-item label="角色标识"><a-input v-model:value="form.roleCode" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.description" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:open="assignOpen" title="菜单授权" @ok="onAssignSave" width="500px">
      <a-tree v-model:checkedKeys="checkedMenuIds" :tree-data="menuTree" checkable :field-names="{title:'label',key:'id',children:'children'}" check-strictly />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { roleApi } from '@/api/system/role'
import { menuApi } from '@/api/system/menu'

const columns = [
  { title: '角色名称', dataIndex: 'roleName', key: 'roleName' },
  { title: '角色标识', dataIndex: 'roleCode', key: 'roleCode' },
  { title: '描述', dataIndex: 'description', key: 'description' },
  { title: '操作', key: 'action' },
]
const list = ref<any[]>([])
const loading = ref(false)
const modalOpen = ref(false)
const assignOpen = ref(false)
const editId = ref<number | null>(null)
const assignRoleId = ref<number | null>(null)
const saving = ref(false)
const form = reactive({ roleName: '', roleCode: '', description: '' })
const menuTree = ref<any[]>([])
const checkedMenuIds = ref<any>({ checked: [], halfChecked: [] })

async function loadData() {
  loading.value = true
  try {
    const res: any = await roleApi.list()
    list.value = res.data?.rows || res.data?.list || res.data || []
  } finally { loading.value = false }
}

function openModal(row?: any) {
  editId.value = row?.id || null
  Object.assign(form, row ? { roleName: row.roleName, roleCode: row.roleCode, description: row.description } : { roleName: '', roleCode: '', description: '' })
  modalOpen.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (editId.value) await roleApi.update(editId.value, form)
    else await roleApi.create(form)
    message.success('保存成功'); modalOpen.value = false; loadData()
  } finally { saving.value = false }
}

async function onDelete(id: number) {
  await roleApi.remove(id); message.success('删除成功'); loadData()
}

async function openAssign(row: any) {
  assignRoleId.value = row.id
  const res: any = await roleApi.getMenus(row.id)
  checkedMenuIds.value = { checked: res.data || [], halfChecked: [] }
  assignOpen.value = true
}

async function onAssignSave() {
  if (!assignRoleId.value) return
  const ids = Array.isArray(checkedMenuIds.value) ? checkedMenuIds.value : checkedMenuIds.value.checked
  await roleApi.assignMenus(assignRoleId.value, ids)
  message.success('授权成功'); assignOpen.value = false
}

onMounted(async () => {
  loadData()
  const res: any = await menuApi.tree()
  menuTree.value = res.data || []
})
</script>
