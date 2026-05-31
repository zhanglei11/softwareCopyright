<template>
  <div class="page-container">
    <div class="table-toolbar">
      <span>角色列表</span>
      <a-button type="primary" v-permission="'system:role:add'" @click="openAdd">
        <PlusOutlined /> 新增角色
      </a-button>
    </div>
    <a-table :columns="columns" :data-source="dataList" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'success' : 'error'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag>
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

    <a-modal v-model:open="modalVisible" :title="isEdit ? '编辑角色' : '新增角色'" @ok="handleSubmit">
      <a-form :model="formData" :rules="rules" ref="formRef">
        <a-form-item label="角色名称" name="roleName"><a-input v-model:value="formData.roleName" /></a-form-item>
        <a-form-item label="角色编码" name="roleCode"><a-input v-model:value="formData.roleCode" :disabled="isEdit" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="formData.remark" :rows="3" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getRoleListApi, createRoleApi, updateRoleApi, deleteRoleApi } from '@/api/system/role'

const loading = ref(false)
const dataList = ref<any[]>([])
const modalVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const formData = reactive<any>({ roleName: '', roleCode: '', remark: '', status: 1 })
const rules = {
  roleName: [{ required: true, message: '请输入角色名称' }],
  roleCode: [{ required: true, message: '请输入角色编码' }],
}
const columns = [
  { title: '角色名称', dataIndex: 'roleName' },
  { title: '角色编码', dataIndex: 'roleCode' },
  { title: '描述', dataIndex: 'remark' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action', width: 150 },
]

async function fetchData() {
  loading.value = true
  try {
    const res = await getRoleListApi()
    dataList.value = res.data?.rows || res.data?.list || res.data || []
  } finally {
    loading.value = false
  }
}

function openAdd() { isEdit.value = false; Object.assign(formData, { id: undefined, roleName: '', roleCode: '', remark: '' }); modalVisible.value = true }
function openEdit(r: any) { isEdit.value = true; Object.assign(formData, r); modalVisible.value = true }

async function handleSubmit() {
  await formRef.value?.validate()
  if (isEdit.value) { await updateRoleApi(formData.id, formData) }
  else { await createRoleApi(formData) }
  message.success('操作成功')
  modalVisible.value = false
  fetchData()
}

async function handleDelete(id: string | number) {
  await deleteRoleApi(id)
  message.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>
