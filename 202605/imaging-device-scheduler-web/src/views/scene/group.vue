<template>
  <a-card :bordered="false" style="border-radius:8px">
    <div class="page-header">
      <span>场景分组管理</span>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增分组</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
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

  <a-modal v-model:open="modalOpen" :title="editId ? '编辑分组' : '新增分组'" @ok="handleSubmit" :confirm-loading="saving">
    <a-form :model="form" label-col="{ span: 6 }" wrapper-col="{ span: 16 }">
      <a-form-item label="分组编码" required><a-input v-model:value="form.groupCode" /></a-form-item>
      <a-form-item label="分组名称" required><a-input v-model:value="form.groupName" /></a-form-item>
      <a-form-item label="备注"><a-textarea v-model:value="form.remark" :rows="3" /></a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getSceneGroupListApi, addSceneGroupApi, editSceneGroupApi, deleteSceneGroupApi } from '@/api/scene'

const list = ref<any[]>([])
const loading = ref(false)
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ groupCode: '', groupName: '', remark: '' })

const columns = [
  { title: '分组编码', dataIndex: 'groupCode' },
  { title: '分组名称', dataIndex: 'groupName' },
  { title: '备注', dataIndex: 'remark' },
  { title: '创建时间', dataIndex: 'createTime' },
  { title: '操作', key: 'action', width: 100 },
]

async function loadList() {
  loading.value = true
  try {
    const res: any = await getSceneGroupListApi()
    list.value = res.data || []
  } finally { loading.value = false }
}

function openAdd() { editId.value = null; Object.assign(form, { groupCode: '', groupName: '', remark: '' }); modalOpen.value = true }
function openEdit(record: any) { editId.value = record.id; Object.assign(form, record); modalOpen.value = true }

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) { await editSceneGroupApi(editId.value, form); message.success('编辑成功') }
    else { await addSceneGroupApi(form); message.success('新增成功') }
    modalOpen.value = false; loadList()
  } finally { saving.value = false }
}

async function handleDelete(id: number) {
  await deleteSceneGroupApi(id); message.success('删除成功'); loadList()
}

onMounted(loadList)
</script>
