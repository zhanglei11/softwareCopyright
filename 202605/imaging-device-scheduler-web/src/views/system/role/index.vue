<template>
  <a-card :bordered="false" style="border-radius:8px">
    <div class="page-header">
      <span>角色管理</span>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增角色</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag>
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
  <a-modal v-model:open="modalOpen" :title="editId ? '编辑角色' : '新增角色'" @ok="handleSubmit" :confirm-loading="saving">
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="角色编码" required><a-input v-model:value="form.roleCode" /></a-form-item>
      <a-form-item label="角色名称" required><a-input v-model:value="form.roleName" /></a-form-item>
      <a-form-item label="备注"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getRoleListApi, addRoleApi, editRoleApi, deleteRoleApi } from '@/api/system'

const list = ref<any[]>([])
const loading = ref(false)
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ roleCode: '', roleName: '', remark: '', status: 1 })

const columns = [
  { title: '角色编码', dataIndex: 'roleCode' },
  { title: '角色名称', dataIndex: 'roleName' },
  { title: '备注', dataIndex: 'remark', ellipsis: true },
  { title: '状态', key: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createTime', width: 165 },
  { title: '操作', key: 'action', width: 100 },
]

async function loadList() {
  loading.value = true
  try {
    const res: any = await getRoleListApi()
    list.value = res.data?.records || res.data || []
  } finally { loading.value = false }
}

function openAdd() { editId.value = null; Object.assign(form, { roleCode: '', roleName: '', remark: '', status: 1 }); modalOpen.value = true }
function openEdit(record: any) { editId.value = record.id; Object.assign(form, record); modalOpen.value = true }

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) { await editRoleApi(editId.value, form); message.success('编辑成功') }
    else { await addRoleApi(form); message.success('新增成功') }
    modalOpen.value = false; loadList()
  } finally { saving.value = false }
}

async function handleDelete(id: number) { await deleteRoleApi(id); message.success('删除成功'); loadList() }
onMounted(loadList)
</script>
