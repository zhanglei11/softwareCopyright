<template>
  <div>
    <a-card title="场景分类管理">
      <template #extra>
        <a-button type="primary" v-permission="'ai:category:create'" @click="openCreate"><PlusOutlined /> 新增分类</a-button>
      </template>
      <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button size="small" v-permission="'ai:category:update'" @click="openEdit(record)">编辑</a-button>
              <a-button size="small" danger v-permission="'ai:category:delete'" @click="handleDelete(record.id)">删除</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:open="modalVisible" :title="editId ? '编辑分类' : '新增分类'" @ok="handleSubmit" :confirm-loading="submitting">
      <a-form :model="form" :rules="rules" ref="formRef">
        <a-form-item label="分类名称" name="name"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="图标"><a-input v-model:value="form.icon" /></a-form-item>
        <a-form-item label="排序"><a-input-number v-model:value="form.sort" style="width:100%" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getCategoryListApi, createCategoryApi, updateCategoryApi, deleteCategoryApi } from '@/api/ai/category'

const list = ref<any[]>([])
const loading = ref(false)
const submitting = ref(false)
const modalVisible = ref(false)
const editId = ref<number | null>(null)
const formRef = ref()
const form = reactive<any>({ name: '', icon: '', sort: 0 })
const rules = { name: [{ required: true, message: '请输入分类名称' }] }
const columns = [
  { title: '分类名称', dataIndex: 'name', key: 'name' },
  { title: '图标', dataIndex: 'icon', key: 'icon' },
  { title: '排序', dataIndex: 'sort', key: 'sort' },
  { title: '操作', key: 'action' },
]
const loadData = async () => {
  loading.value = true
  try { const res: any = await getCategoryListApi(); list.value = res.data?.list || res.data || [] }
  finally { loading.value = false }
}
const openCreate = () => { editId.value = null; Object.assign(form, { name: '', icon: '', sort: 0 }); modalVisible.value = true }
const openEdit = (r: any) => { editId.value = r.id; Object.assign(form, r); modalVisible.value = true }
const handleSubmit = async () => {
  await formRef.value?.validate(); submitting.value = true
  try {
    if (editId.value) await updateCategoryApi(editId.value, form)
    else await createCategoryApi(form)
    message.success('操作成功'); modalVisible.value = false; loadData()
  } finally { submitting.value = false }
}
const handleDelete = (id: number) => Modal.confirm({ title: '确认删除？', onOk: async () => { await deleteCategoryApi(id); message.success('删除成功'); loadData() } })
onMounted(loadData)
</script>
