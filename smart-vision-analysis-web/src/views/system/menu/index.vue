<template>
  <div class="page-container">
    <div class="table-toolbar"><span>菜单管理</span><a-button type="primary" @click="open()"><PlusOutlined /> 新增</a-button></div>
    <a-table :columns="cols" :data-source="list" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="open(record)">编辑</a-button>
          <a-popconfirm title="确认删除？" @confirm="del(record.id)"><a-button size="small" type="link" danger>删除</a-button></a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="mOpen" :title="editId?'编辑菜单':'新增菜单'" @ok="save" width="560px">
      <a-form :model="form" :rules="rules" ref="fRef">
        <a-form-item label="菜单名称" name="menuName"><a-input v-model:value="form.menuName" /></a-form-item>
        <a-form-item label="路由地址"><a-input v-model:value="form.path" /></a-form-item>
        <a-form-item label="权限标识"><a-input v-model:value="form.perms" /></a-form-item>
        <a-form-item label="排序"><a-input-number v-model:value="form.orderNum" style="width:100%" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getMenuTreeApi, createMenuApi, updateMenuApi, deleteMenuApi } from '@/api/system/menu'
const loading = ref(false); const list = ref<any[]>([])
const mOpen = ref(false); const editId = ref<number|null>(null); const fRef = ref()
const form = reactive<any>({ menuName: '', path: '', perms: '', orderNum: 1 })
const rules = { menuName: [{ required: true }] }
const cols = [{ title: '菜单名称', dataIndex: 'menuName', width: 200 }, { title: '路由', dataIndex: 'path' }, { title: '权限标识', dataIndex: 'perms' }, { title: '排序', dataIndex: 'orderNum', width: 60 }, { title: '操作', key: 'action', width: 140 }]
async function load() { loading.value = true; try { const r = await getMenuTreeApi(); list.value = r.data || [] } finally { loading.value = false } }
function open(row?: any) { editId.value = row?.id || null; Object.assign(form, row || { menuName: '', path: '', perms: '', orderNum: 1 }); mOpen.value = true }
async function save() { await fRef.value?.validate(); editId.value ? await updateMenuApi(editId.value, form) : await createMenuApi(form); message.success('操作成功'); mOpen.value = false; load() }
async function del(id: number) { await deleteMenuApi(id); message.success('删除成功'); load() }
onMounted(load)
</script>
