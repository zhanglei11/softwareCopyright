<template>
  <div>
    <div style="margin-bottom:12px"><a-button type="primary" @click="openModal()">新增菜单</a-button></div>
    <a-table :columns="columns" :data-source="treeData" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'menuType'">
          <a-tag :color="record.menuType===1?'blue':record.menuType===2?'orange':'purple'">{{ record.menuType===0?'目录':record.menuType===1?'菜单':'按鈕' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="openModal(record)">编辑</a-button>
            <a-popconfirm title="确认删除?" @confirm="onDelete(record.id)">
              <a-button size="small" type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalOpen" :title="editId ? '编辑菜单' : '新增菜单'" @ok="onSave" :confirm-loading="saving">
      <a-form :model="form" label-col="{ span: 6 }">
        <a-form-item label="节点类型">
          <a-radio-group v-model:value="form.menuType">
            <a-radio :value="0">目录</a-radio>
            <a-radio :value="1">菜单</a-radio>
            <a-radio :value="2">按鈕</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="菜单名称"><a-input v-model:value="form.menuName" /></a-form-item>
        <a-form-item label="路由路径"><a-input v-model:value="form.path" /></a-form-item>
        <a-form-item label="权限标识"><a-input v-model:value="form.permission" /></a-form-item>
        <a-form-item label="排序"><a-input-number v-model:value="form.sort" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { menuApi } from '@/api/system/menu'

const columns = [
  { title: '菜单名称', dataIndex: 'menuName', key: 'menuName' },
  { title: '类型', dataIndex: 'menuType', key: 'menuType' },
  { title: '路径', dataIndex: 'path', key: 'path' },
  { title: '权限标识', dataIndex: 'permission', key: 'permission' },
  { title: '排序', dataIndex: 'sort', key: 'sort' },
  { title: '操作', key: 'action' },
]
const treeData = ref<any[]>([])
const loading = ref(false)
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive({ menuName: '', path: '', menuType: 1, permission: '', sort: 0, parentId: null as number | null })

async function loadData() {
  loading.value = true
  try {
    const res: any = await menuApi.tree()
    treeData.value = res.data || []
  } finally { loading.value = false }
}

function openModal(row?: any) {
  editId.value = row?.id || null
  Object.assign(form, row ? { ...row } : { menuName: '', path: '', menuType: 1, permission: '', sort: 0, parentId: null })
  modalOpen.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (editId.value) await menuApi.update(editId.value, form)
    else await menuApi.create(form)
    message.success('保存成功'); modalOpen.value = false; loadData()
  } finally { saving.value = false }
}

async function onDelete(id: number) {
  await menuApi.remove(id); message.success('删除成功'); loadData()
}

onMounted(loadData)
</script>
