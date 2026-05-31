<template>
  <div class="page-container">
    <div class="table-toolbar">
      <span>菜单管理</span>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增菜单</a-button>
    </div>
    <a-table :columns="columns" :data-source="treeData" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'icon'">
          <component v-if="record.icon" :is="record.icon" />
        </template>
        <template v-if="column.key === 'menuType'">
          <a-tag color="blue" v-if="record.menuType === 'M'">目录</a-tag>
          <a-tag color="green" v-else-if="record.menuType === 'C'">菜单</a-tag>
          <a-tag color="orange" v-else>按钮</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="openEdit(record)">编辑</a-button>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id)">
              <a-button size="small" type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="isEdit ? '编辑菜单' : '新增菜单'" @ok="handleSubmit" width="600px">
      <a-form :model="formData" :rules="rules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="菜单类型">
          <a-radio-group v-model:value="formData.menuType">
            <a-radio value="M">目录</a-radio>
            <a-radio value="C">菜单</a-radio>
            <a-radio value="F">按钮</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="菜单名称" name="menuName"><a-input v-model:value="formData.menuName" /></a-form-item>
        <a-form-item label="路由地址" v-if="formData.menuType !== 'F'" name="path">
          <a-input v-model:value="formData.path" />
        </a-form-item>
        <a-form-item label="权限标识" v-if="formData.menuType === 'F'">
          <a-input v-model:value="formData.perms" />
        </a-form-item>
        <a-form-item label="排序"><a-input-number v-model:value="formData.orderNum" style="width:100%" /></a-form-item>
        <a-form-item label="是否隐藏">
          <a-switch v-model:checked="formData.hidden" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getMenuTreeApi, createMenuApi, updateMenuApi, deleteMenuApi } from '@/api/system/menu'

const loading = ref(false)
const treeData = ref<any[]>([])
const modalVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const formData = reactive<any>({ menuName: '', menuType: 'C', path: '', perms: '', orderNum: 1, hidden: false })
const rules = { menuName: [{ required: true, message: '请输入菜单名称' }] }
const columns = [
  { title: '菜单名称', dataIndex: 'menuName', width: 200 },
  { title: '类型', key: 'menuType', width: 80 },
  { title: '路由', dataIndex: 'path' },
  { title: '权限标识', dataIndex: 'perms' },
  { title: '排序', dataIndex: 'orderNum', width: 60 },
  { title: '操作', key: 'action', width: 150 },
]

async function fetchData() {
  loading.value = true
  try {
    const res = await getMenuTreeApi()
    treeData.value = res.data || []
  } finally { loading.value = false }
}

function openAdd() { isEdit.value = false; Object.assign(formData, { id: undefined, menuName: '', menuType: 'C', path: '', perms: '', orderNum: 1, hidden: false }); modalVisible.value = true }
function openEdit(r: any) { isEdit.value = true; Object.assign(formData, r); modalVisible.value = true }

async function handleSubmit() {
  await formRef.value?.validate()
  if (isEdit.value) { await updateMenuApi(formData.id, formData) }
  else { await createMenuApi(formData) }
  message.success('操作成功')
  modalVisible.value = false
  fetchData()
}

async function handleDelete(id: string | number) {
  await deleteMenuApi(id)
  message.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>
