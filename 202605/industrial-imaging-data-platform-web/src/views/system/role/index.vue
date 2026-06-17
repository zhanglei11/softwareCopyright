<template>
  <div class="page-container">
    <div class="table-toolbar">
      <span>角色列表</span>
      <a-button type="primary" @click="showAdd"><PlusOutlined /> 新增</a-button>
    </div>
    <a-table :dataSource="list" :columns="columns" :loading="loading" rowKey="id" :pagination="pagination" @change="handlePageChange" size="middle" style="background:#fff;border-radius:8px">
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
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑角色' : '新增角色'" @ok="handleSave" :confirm-loading="saving">
      <a-form :model="form" :rules="rules" ref="formRef" :label-col="{span:5}">
        <a-form-item label="角色名称" name="roleName"><a-input v-model:value="form.roleName" /></a-form-item>
        <a-form-item label="角色编码" name="roleCode"><a-input v-model:value="form.roleCode" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/system/role'
import type { SysRole } from '@/types'

const loading = ref(false); const saving = ref(false)
const list = ref<SysRole[]>([])
const modalVisible = ref(false); const formRef = ref()
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const form = reactive<Partial<SysRole>>({})
const rules = { roleName: [{ required: true, message: '请输入角色名称' }], roleCode: [{ required: true, message: '请输入角色编码' }] }
const columns = [
  { title: '角色名称', dataIndex: 'roleName' },
  { title: '角色编码', dataIndex: 'roleCode' },
  { title: '备注', dataIndex: 'remark' },
  { title: '创建时间', dataIndex: 'createTime' },
  { title: '操作', key: 'action', width: 120 },
]
const load = async () => {
  loading.value = true
  try {
    const res = await getRoleList({ pageNum: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data.rows; pagination.total = res.data.total
  } finally { loading.value = false }
}
const handlePageChange = (p: typeof pagination) => { pagination.current = p.current; pagination.pageSize = p.pageSize; load() }
const showAdd = () => { Object.assign(form, { id: undefined, roleName: '', roleCode: '', remark: '' }); modalVisible.value = true }
const showEdit = (r: SysRole) => { Object.assign(form, { ...r }); modalVisible.value = true }
const handleSave = async () => {
  await formRef.value?.validate(); saving.value = true
  try {
    if (form.id) await updateRole(form.id, form as SysRole); else await addRole(form as SysRole)
    message.success('保存成功'); modalVisible.value = false; load()
  } finally { saving.value = false }
}
const handleDelete = async (id: number) => { await deleteRole(id); message.success('删除成功'); load() }
onMounted(load)
</script>
