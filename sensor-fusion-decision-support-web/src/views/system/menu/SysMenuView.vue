<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-button type="primary" @click="handleAdd(null)"><plus-outlined />新增根菜单</a-button>
      </div>
      <a-table :columns="columns" :data-source="menuTree" :loading="loading" row-key="id" :default-expand-all-rows="true">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'menuType'">
            <a-tag :color="record.menuType===0?'blue':record.menuType===1?'green':'orange'">
              {{ record.menuType===0?'目录':record.menuType===1?'菜单':'按钮' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleAdd(record)">新增子菜单</a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-popconfirm title="确认删除？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="modalVisible" :title="modalTitle" @ok="handleSubmit" :confirm-loading="submitLoading">
      <a-form ref="formRef" :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="上级菜单" name="parentId"><a-input-number v-model:value="form.parentId" style="width:100%" :min="0" /></a-form-item>
        <a-form-item label="菜单名称" name="menuName" :rules="[{required:true,message:'请输入菜单名称'}]"><a-input v-model:value="form.menuName" /></a-form-item>
        <a-form-item label="菜单类型" name="menuType">
          <a-radio-group v-model:value="form.menuType">
            <a-radio :value="0">目录</a-radio><a-radio :value="1">菜单</a-radio><a-radio :value="2">按钮</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="路由路径" name="path"><a-input v-model:value="form.path" /></a-form-item>
        <a-form-item label="组件路径" name="component"><a-input v-model:value="form.component" /></a-form-item>
        <a-form-item label="权限标识" name="perms"><a-input v-model:value="form.perms" /></a-form-item>
        <a-form-item label="图标" name="icon"><a-input v-model:value="form.icon" /></a-form-item>
        <a-form-item label="排序" name="orderNum"><a-input-number v-model:value="form.orderNum" style="width:100%" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getMenuTreeApi, addMenuApi, editMenuApi, deleteMenuApi } from '@/api/system/menu'

const loading = ref(false)
const submitLoading = ref(false)
const menuTree = ref<any[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增菜单')
const formRef = ref()
const form = reactive<any>({ id: null, parentId: 0, menuName: '', menuType: 1, path: '', component: '', perms: '', icon: '', orderNum: 0 })

const columns = [
  { title: '菜单名称', dataIndex: 'menuName', key: 'menuName' },
  { title: '类型', dataIndex: 'menuType', key: 'menuType' },
  { title: '路由路径', dataIndex: 'path', key: 'path' },
  { title: '权限标识', dataIndex: 'perms', key: 'perms' },
  { title: '图标', dataIndex: 'icon', key: 'icon' },
  { title: '排序', dataIndex: 'orderNum', key: 'orderNum' },
  { title: '操作', key: 'action', width: 220 },
]

async function loadData() {
  loading.value = true
  try { const res: any = await getMenuTreeApi(); menuTree.value = res.data || [] }
  finally { loading.value = false }
}

function handleAdd(parent: any) {
  modalTitle.value = '新增菜单'
  Object.assign(form, { id: null, parentId: parent?.id || 0, menuName: '', menuType: 1, path: '', component: '', perms: '', icon: '', orderNum: 0 })
  modalVisible.value = true
}

function handleEdit(record: any) {
  modalTitle.value = '编辑菜单'; Object.assign(form, record); modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    form.id ? await editMenuApi(form.id, form) : await addMenuApi(form)
    message.success('操作成功'); modalVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

async function handleDelete(record: any) {
  await deleteMenuApi(record.id); message.success('删除成功'); loadData()
}

onMounted(loadData)
</script>
