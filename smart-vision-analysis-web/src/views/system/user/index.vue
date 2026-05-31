<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline">
        <a-form-item label="用户名"><a-input v-model:value="q.username" placeholder="用户名" allow-clear /></a-form-item>
        <a-form-item><a-button type="primary" @click="load"><SearchOutlined /> 查询</a-button><a-button style="margin-left:8px" @click="()=>{q.username='';load()}">重置</a-button></a-form-item>
      </a-form>
    </div>
    <div class="table-toolbar"><span>用户列表</span><a-button type="primary" @click="open()"><PlusOutlined /> 新增</a-button></div>
    <a-table :columns="cols" :data-source="list" :loading="loading" :pagination="page" @change="c => { page.current=c.current; load() }" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='status'"><a-tag :color="record.status===1?'success':'error'">{{ record.status===1?'启用':'禁用' }}</a-tag></template>
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="open(record)">编辑</a-button>
          <a-popconfirm title="确认删除？" @confirm="del(record.id)"><a-button size="small" type="link" danger>删除</a-button></a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="mOpen" :title="editId?'编辑用户':'新增用户'" @ok="save" :confirm-loading="saving">
      <a-form :model="form" :rules="rules" ref="fRef">
        <a-form-item label="用户名" name="username"><a-input v-model:value="form.username" :disabled="!!editId" /></a-form-item>
        <a-form-item label="真实姓名"><a-input v-model:value="form.realName" /></a-form-item>
        <a-form-item label="手机号"><a-input v-model:value="form.phone" /></a-form-item>
        <a-form-item label="邮箱"><a-input v-model:value="form.email" /></a-form-item>
        <a-form-item label="状态"><a-radio-group v-model:value="form.status"><a-radio :value="1">启用</a-radio><a-radio :value="0">禁用</a-radio></a-radio-group></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { SearchOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getUserListApi, createUserApi, updateUserApi, deleteUserApi } from '@/api/system/user'
const loading = ref(false); const list = ref<any[]>([]); const q = reactive({ username: '' })
const page = reactive({ current: 1, pageSize: 10, total: 0 })
const mOpen = ref(false); const saving = ref(false); const editId = ref<number|null>(null); const fRef = ref()
const form = reactive<any>({ username: '', realName: '', phone: '', email: '', status: 1 })
const rules = { username: [{ required: true, message: '请输入用户名' }] }
const cols = [
  { title: '用户名', dataIndex: 'username' }, { title: '真实姓名', dataIndex: 'realName' },
  { title: '手机号', dataIndex: 'phone' }, { title: '状态', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', customRender: ({ text }: any) => text ? text.replace('T', ' ').slice(0, 19) : '-' }, { title: '操作', key: 'action', width: 140 },
]
async function load() { loading.value = true; try { const r = await getUserListApi({ pageNum: page.current, pageSize: page.pageSize, ...q }); list.value = r.data?.rows || r.data?.list || []; page.total = r.data?.total || 0 } finally { loading.value = false } }
function open(row?: any) { editId.value = row?.id || null; Object.assign(form, row || { username: '', realName: '', phone: '', email: '', status: 1 }); mOpen.value = true }
async function save() { await fRef.value?.validate(); saving.value = true; try { editId.value ? await updateUserApi(editId.value, form) : await createUserApi(form); message.success('操作成功'); mOpen.value = false; load() } finally { saving.value = false } }
async function del(id: number) { await deleteUserApi(id); message.success('删除成功'); load() }
onMounted(load)
</script>
