<template>
  <div class="page-container">
    <div class="table-toolbar">
      <span>场景配置列表</span>
      <a-button type="primary" @click="$router.push({ name: 'AiSceneDetail', params: { id: 'new' } })">
        <PlusOutlined /> 新建场景
      </a-button>
    </div>
    <a-table :columns="columns" :data-source="dataList" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 'ONLINE' ? 'success' : record.status === 'DRAFT' ? 'warning' : 'default'">{{ record.status === 'ONLINE' ? '已发布' : record.status === 'DRAFT' ? '草稿' : '已下线' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="$router.push({ name: 'AiSceneDetail', params: { id: record.id } })">详情/编辑</a-button>
            <a-button v-if="record.status !== 'ONLINE'" size="small" type="link" @click="handlePublish(record.id)">发布</a-button>
            <a-button v-if="record.status === 'ONLINE'" size="small" type="link" @click="handleOffline(record.id)">下线</a-button>
            <a-popconfirm title="确认删除？" @confirm="handleDelete(record.id)">
              <a-button size="small" type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getSceneListApi, deleteSceneApi, publishSceneApi, offlineSceneApi } from '@/api/ai/scene'

const loading = ref(false)
const dataList = ref<any[]>([])
const columns = [
  { title: '场景名称', dataIndex: 'name' },
  { title: '场景分类', dataIndex: 'categoryName' },
  { title: '描述', dataIndex: 'description', ellipsis: true },
  { title: '状态', key: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createdTime', width: 160 },
  { title: '操作', key: 'action', width: 220 },
]

async function fetchData() {
  loading.value = true
  try {
    const res = await getSceneListApi()
    dataList.value = res.data?.rows || res.data?.list || res.data || []
  } finally { loading.value = false }
}

async function handleDelete(id: string | number) {
  await deleteSceneApi(id)
  message.success('删除成功')
  fetchData()
}

async function handlePublish(id: number) {
  await publishSceneApi(id)
  message.success('发布成功')
  fetchData()
}

async function handleOffline(id: number) {
  await offlineSceneApi(id)
  message.success('下线成功')
  fetchData()
}

onMounted(fetchData)
</script>
