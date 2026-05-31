<template>
  <a-card :bordered="false" style="border-radius:8px">
    <div class="page-header">
      <a-space>
        <a-input v-model:value="query.keyword" placeholder="场景名称/编码" style="width:180px" @press-enter="loadList" />
        <a-select v-model:value="query.groupId" placeholder="场景分组" style="width:140px" allow-clear>
          <a-select-option v-for="g in groups" :key="g.id" :value="g.id">{{ g.groupName }}</a-select-option>
        </a-select>
        <a-select v-model:value="query.status" placeholder="状态" style="width:90px" allow-clear>
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="0">禁用</a-select-option>
        </a-select>
        <a-button type="primary" @click="loadList">查询</a-button>
        <a-button @click="resetQuery">重置</a-button>
      </a-space>
      <a-button type="primary" @click="openAdd"><PlusOutlined /> 新增场景</a-button>
    </div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="pagination" @change="handleTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-switch :checked="record.status === 1" @change="(v: boolean) => toggleStatus(record, v)" size="small" />
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

  <a-modal v-model:open="modalOpen" :title="editId ? '编辑场景' : '新增场景'" @ok="handleSubmit" :confirm-loading="saving">
    <a-form :model="form" label-col="{ span: 6 }" wrapper-col="{ span: 16 }">
      <a-form-item label="场景编码" required><a-input v-model:value="form.sceneCode" /></a-form-item>
      <a-form-item label="场景名称" required><a-input v-model:value="form.sceneName" /></a-form-item>
      <a-form-item label="场景类型">
        <a-select v-model:value="form.sceneType">
          <a-select-option :value="1">工件检测</a-select-option>
          <a-select-option :value="2">流水线监控</a-select-option>
          <a-select-option :value="3">仓储巡检</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="所属分组">
        <a-select v-model:value="form.groupId" allow-clear>
          <a-select-option v-for="g in groups" :key="g.id" :value="g.id">{{ g.groupName }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="描述"><a-textarea v-model:value="form.description" :rows="3" /></a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getSceneListApi, addSceneApi, editSceneApi, deleteSceneApi, changeSceneStatusApi, getSceneGroupListApi } from '@/api/scene'

const list = ref<any[]>([])
const loading = ref(false)
const groups = ref<any[]>([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const query = reactive<any>({ keyword: '', groupId: undefined, status: undefined })
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive<any>({ sceneCode: '', sceneName: '', sceneType: 1, groupId: undefined, description: '' })

const columns = [
  { title: '场景编码', dataIndex: 'sceneCode', width: 130 },
  { title: '场景名称', dataIndex: 'sceneName' },
  { title: '类型', dataIndex: 'sceneType', width: 100 },
  { title: '分组', dataIndex: 'groupName', width: 120 },
  { title: '描述', dataIndex: 'description', ellipsis: true },
  { title: '状态', key: 'status', width: 70 },
  { title: '操作', key: 'action', width: 100 },
]

async function loadList() {
  loading.value = true
  try {
    const res: any = await getSceneListApi({ ...query, page: pagination.current, size: pagination.pageSize })
    const d = res.data
    list.value = d.records || d || []
    pagination.total = d.total || list.value.length
  } finally { loading.value = false }
}

function handleTableChange(p: any) { pagination.current = p.current; pagination.pageSize = p.pageSize; loadList() }
function resetQuery() { Object.assign(query, { keyword: '', groupId: undefined, status: undefined }); loadList() }
function openAdd() { editId.value = null; Object.assign(form, { sceneCode: '', sceneName: '', sceneType: 1, groupId: undefined, description: '' }); modalOpen.value = true }
function openEdit(record: any) { editId.value = record.id; Object.assign(form, record); modalOpen.value = true }

async function handleSubmit() {
  saving.value = true
  try {
    if (editId.value) { await editSceneApi(editId.value, form); message.success('编辑成功') }
    else { await addSceneApi(form); message.success('新增成功') }
    modalOpen.value = false; loadList()
  } finally { saving.value = false }
}

async function toggleStatus(record: any, v: boolean) {
  await changeSceneStatusApi(record.id, v ? 1 : 0)
  record.status = v ? 1 : 0
}

async function handleDelete(id: number) { await deleteSceneApi(id); message.success('删除成功'); loadList() }

onMounted(async () => {
  const res: any = await getSceneGroupListApi()
  groups.value = res.data || []
  loadList()
})
</script>
