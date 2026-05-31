<template>
  <div>
    <a-card title="知识库管理">
      <template #extra>
        <a-button type="primary" v-permission="'kb:base:create'" @click="openCreate"><PlusOutlined /> 新建知识库</a-button>
      </template>
      <a-row :gutter="[16,16]">
        <a-col :span="8" v-for="kb in list" :key="kb.id">
          <a-card hoverable>
            <template #title>{{ kb.name }}</template>
            <template #extra><router-link :to="`/kb/docs/${kb.id}`">管理文档</router-link></template>
            <p style="color:#666">{{ kb.description || '暂无描述' }}</p>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <a-tag color="blue">{{ kb.docCount || 0 }} 个文档</a-tag>
              <a-space>
                <a-button size="small" v-permission="'kb:base:update'" @click="openEdit(kb)">编辑</a-button>
                <a-button size="small" danger v-permission="'kb:base:delete'" @click="handleDelete(kb.id)">删除</a-button>
              </a-space>
            </div>
          </a-card>
        </a-col>
        <a-col :span="24" v-if="list.length === 0 && !loading">
          <a-empty description="暂无知识库" />
        </a-col>
      </a-row>
    </a-card>
    <a-modal v-model:open="modalVisible" :title="editId ? '编辑知识库' : '新建知识库'" @ok="handleSubmit" :confirm-loading="submitting">
      <a-form :model="form" :rules="rules" ref="formRef">
        <a-form-item label="名称" name="name"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="form.description" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getKbListApi, createKbApi, updateKbApi, deleteKbApi } from '@/api/kb/base'

const list = ref<any[]>([])
const loading = ref(false)
const submitting = ref(false)
const modalVisible = ref(false)
const editId = ref<number | null>(null)
const formRef = ref()
const form = reactive<any>({ name: '', description: '' })
const rules = { name: [{ required: true, message: '请输入名称' }] }
const loadData = async () => { loading.value = true; try { const res: any = await getKbListApi(); list.value = res.data?.list || res.data || [] } finally { loading.value = false } }
const openCreate = () => { editId.value = null; Object.assign(form, { name: '', description: '' }); modalVisible.value = true }
const openEdit = (r: any) => { editId.value = r.id; Object.assign(form, r); modalVisible.value = true }
const handleSubmit = async () => {
  await formRef.value?.validate(); submitting.value = true
  try { if (editId.value) await updateKbApi(editId.value, form); else await createKbApi(form); message.success('操作成功'); modalVisible.value = false; loadData() }
  finally { submitting.value = false }
}
const handleDelete = (id: number) => Modal.confirm({ title: '确认删除？', onOk: async () => { await deleteKbApi(id); message.success('删除成功'); loadData() } })
onMounted(loadData)
</script>
