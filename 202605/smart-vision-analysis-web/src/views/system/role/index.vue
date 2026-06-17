<template>
  <div class="page-container">
    <div class="table-toolbar"><span>角色列表</span><a-button type="primary" @click="open()"><PlusOutlined /> 新增</a-button></div>
    <a-table :columns="cols" :data-source="list" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="open(record)">编辑</a-button>
          <a-popconfirm title="确认删除？" @confirm="del(record.id)"><a-button size="small" type="link" danger>删除</a-button></a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="mOpen" :title="editId?'编辑角色':'新增角色'" @ok="save">
      <a-form :model="form" :rules="rules" ref="fRef">
        <a-form-item label="角色名称" name="roleName"><a-input v-model:value="form.roleName" /></a-form-item>
        <a-form-item label="角色编码" name="roleCode"><a-input v-model:value="form.roleCode" :disabled="!!editId" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.description" :rows="3" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getRoleListApi, createRoleApi, updateRoleApi, deleteRoleApi } from '@/api/system/role'
const loading = ref(false); const list = ref<any[]>([])
const mOpen = ref(false); const editId = ref<number|null>(null); const fRef = ref()
const form = reactive<any>({ roleName: '', roleCode: '', description: '' })
const rules = { roleName: [{ required: true }], roleCode: [{ required: true }] }
const cols = [{ title: '角色名称', dataIndex: 'roleName' }, { title: '角色编码', dataIndex: 'roleCode' }, { title: '描述', dataIndex: 'description' }, { title: '操作', key: 'action' }]
async function load() { loading.value = true; try { const r = await getRoleListApi(); list.value = r.data?.rows || r.data?.list || r.data || [] } finally { loading.value = false } }
function open(row?: any) { editId.value = row?.id || null; Object.assign(form, row || { roleName: '', roleCode: '', description: '' }); mOpen.value = true }
async function save() { await fRef.value?.validate(); editId.value ? await updateRoleApi(editId.value, form) : await createRoleApi(form); message.success('操作成功'); mOpen.value = false; load() }
async function del(id: number) { await deleteRoleApi(id); message.success('删除成功'); load() }
onMounted(load)
</script>
