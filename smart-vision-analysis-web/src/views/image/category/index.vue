<template>
  <div class="page-container">
    <div class="table-toolbar"><span>影像分类</span><a-button type="primary" @click="open()"><PlusOutlined /> 新增分类</a-button></div>
    <a-table :columns="cols" :data-source="list" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="open(record)">编辑</a-button>
          <a-popconfirm title="确认删除？注意：有影像关联的分类不可删" @confirm="del(record.id)">
            <a-button size="small" type="link" danger>删除</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="mOpen" :title="editId?'编辑分类':'新增分类'" @ok="save">
      <a-form :model="form" :rules="rules" ref="fRef">
        <a-form-item label="分类名称" name="categoryName"><a-input v-model:value="form.categoryName" /></a-form-item>
        <a-form-item label="父级分类">
          <a-select v-model:value="form.parentId" allow-clear placeholder="不选为一级分类" :options="parentOptions" style="width:100%" />
        </a-form-item>
        <a-form-item label="排序"><a-input-number v-model:value="form.sortOrder" style="width:100%" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getImageCategoriesApi, createCategoryApi, updateCategoryApi, deleteCategoryApi } from '@/api/image'
const loading = ref(false); const list = ref<any[]>([])
const mOpen = ref(false); const editId = ref<number|null>(null); const fRef = ref()
const form = reactive<any>({ categoryName: '', parentId: undefined, sortOrder: 1 })
const rules = { categoryName: [{ required: true, message: '请输入分类名称' }] }
const cols = [{ title: '分类名称', dataIndex: 'categoryName', width: 200 }, { title: '父级', dataIndex: 'parentName' }, { title: '排序', dataIndex: 'sortOrder', width: 60 }, { title: '影像数', dataIndex: 'imageCount', width: 80 }, { title: '操作', key: 'action', width: 140 }]
const parentOptions = computed(() => list.value.filter(c => !c.parentId || c.parentId === 0).map(c => ({ label: c.categoryName, value: c.id })))
async function load() { loading.value = true; try { const r = await getImageCategoriesApi(); list.value = r.data || [] } finally { loading.value = false } }
function open(row?: any) { editId.value = row?.id || null; Object.assign(form, row || { categoryName: '', parentId: undefined, sortOrder: 1 }); mOpen.value = true }
async function save() { await fRef.value?.validate(); editId.value ? await updateCategoryApi(editId.value, form) : await createCategoryApi(form); message.success('操作成功'); mOpen.value = false; load() }
async function del(id: number) { await deleteCategoryApi(id); message.success('删除成功'); load() }
onMounted(load)
</script>
