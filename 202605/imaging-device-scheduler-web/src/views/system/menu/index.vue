<template>
  <a-card :bordered="false" style="border-radius:8px">
    <div class="page-header">
      <span>菜单管理</span>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增菜单</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="false" :default-expand-all-rows="true">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'menuType'">
          <a-tag :color="{ 1: 'blue', 2: 'green', 3: 'orange' }[record.menuType]">{{ { 1: '目录', 2: '菜单', 3: '按钮' }[record.menuType] }}</a-tag>
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
  <a-modal v-model:open="modalOpen" :title="editId ? '编辑菜单' : '新增菜单'" @ok="handleSubmit" :confirm-loading="saving">
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="菜单名称" required><a-input v-model:value="form.menuName" /></a-form-item>
      <a-form-item label="菜单类型">
        <a-select v-model:value="form.menuType">
          <a-select-option :value="1">目录</a-select-option>
          <a-select-option :value="2">菜单</a-select-option>
          <a-select-option :value="3">按钮</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="路由路径"><a-input v-model:value="form.path" /></a-form-item>
      <a-form-item label="权限标识"><a-input v-model:value="form.perms" /></a-form-item>
      <a-form-item label="图标"><a-input v-model:value="form.icon" /></a-form-item>
      <a-form-item label="排序"><a-input-number v-model:value="form.orderNum" :min="0" style="width:100%" /></a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getMenuListApi, addMenuApi, editMenuApi, deleteMenuApi } from '@/api/system'

const list = ref<any[]>([])
const loading = ref(false)
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ menuName: '', menuType: 2, path: '', perms: '', icon: '', orderNum: 0 })

const columns = [
  { title: '菜单名称', dataIndex: 'menuName', width: 200 },
  { title: '类型', key: 'menuType', width: 80 },
  { title: '路由路径', dataIndex: 'path' },
  { title: '权限标识', dataIndex: 'perms' },
  { title: '排序', dataIndex: 'orderNum', width: 70 },
  { title: '操作', key: 'action', width: 100 },
]

async function loadList() {
  loading.value = true
  try {
    const res: any = await getMenuListApi()
    list.value = res.data || []
  } finally { loading.value = false }
}

function openAdd() { editId.value = null; Object.assign(form, { menuName: '', menuType: 2, path: '', perms: '', icon: '', orderNum: 0 }); modalOpen.value = true }
function openEdit(record: any) { editId.value = record.id; Object.assign(form, record); modalOpen.value = true }

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) { await editMenuApi(editId.value, form); message.success('编辑成功') }
    else { await addMenuApi(form); message.success('新增成功') }
    modalOpen.value = false; loadList()
  } finally { saving.value = false }
}

async function handleDelete(id: number) { await deleteMenuApi(id); message.success('删除成功'); loadList() }
onMounted(loadList)
</script>
