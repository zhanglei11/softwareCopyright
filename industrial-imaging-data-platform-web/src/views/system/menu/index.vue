<template>
  <div class="page-container">
    <div class="table-toolbar">
      <span>菜单列表</span>
      <a-button type="primary" @click="showAdd"><PlusOutlined /> 新增</a-button>
    </div>
    <a-table :dataSource="list" :columns="columns" :loading="loading" rowKey="id" :defaultExpandAllRows="true" size="middle" style="background:#fff;border-radius:8px">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="showEdit(record)">编辑</a-button>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id!)">
              <a-button type="link" size="small" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑菜单' : '新增菜单'" @ok="handleSave" :confirm-loading="saving">
      <a-form :model="form" ref="formRef" :label-col="{span:5}">
        <a-form-item label="菜单名称" name="menuName"><a-input v-model:value="form.menuName" /></a-form-item>
        <a-form-item label="菜单类型">
          <a-select v-model:value="form.menuType" style="width:100%">
            <a-select-option value="M">目录</a-select-option>
            <a-select-option value="C">菜单</a-select-option>
            <a-select-option value="F">按钮</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="父级ID"><a-input-number v-model:value="form.parentId" style="width:100%" /></a-form-item>
        <a-form-item label="路由路径"><a-input v-model:value="form.path" /></a-form-item>
        <a-form-item label="权限标识"><a-input v-model:value="form.perms" /></a-form-item>
        <a-form-item label="图标"><a-input v-model:value="form.icon" /></a-form-item>
        <a-form-item label="排序"><a-input-number v-model:value="form.sortOrder" style="width:100%" /></a-form-item>
        <a-form-item label="显示状态">
          <a-radio-group v-model:value="form.visible">
            <a-radio :value="1">显示</a-radio>
            <a-radio :value="0">隐藏</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/system/menu'
import type { SysMenu } from '@/types'

const loading = ref(false); const saving = ref(false)
const list = ref<SysMenu[]>([])
const modalVisible = ref(false); const formRef = ref()
const form = reactive<Partial<SysMenu>>({})
const columns = [
  { title: '菜单名称', dataIndex: 'menuName' },
  { title: '类型', dataIndex: 'menuType', width: 70,
    customRender: ({ text }: { text: string }) => ({ M: '目录', C: '菜单', F: '按钮' }[text] ?? text) },
  { title: '路由路径', dataIndex: 'path' },
  { title: '权限标识', dataIndex: 'perms' },
  { title: '图标', dataIndex: 'icon' },
  { title: '排序', dataIndex: 'sortOrder', width: 70 },
  { title: '操作', key: 'action', width: 120 },
]
const load = async () => {
  loading.value = true
  try { const res = await getMenuTree(); list.value = res.data }
  finally { loading.value = false }
}
const showAdd = () => { Object.assign(form, { id: undefined, menuName: '', menuType: 'C', parentId: 0, path: '', perms: '', icon: '', sortOrder: 0, visible: 1 }); modalVisible.value = true }
const showEdit = (r: SysMenu) => { Object.assign(form, { ...r }); modalVisible.value = true }
const handleSave = async () => {
  saving.value = true
  try {
    if (form.id) await updateMenu(form.id, form as SysMenu); else await addMenu(form as SysMenu)
    message.success('保存成功'); modalVisible.value = false; load()
  } finally { saving.value = false }
}
const handleDelete = async (id: number) => { await deleteMenu(id); message.success('删除成功'); load() }
onMounted(load)
</script>
