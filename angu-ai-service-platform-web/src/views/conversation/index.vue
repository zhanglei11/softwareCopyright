<template>
  <div>
    <a-card title="历史会话记录">
      <a-table :columns="columns" :data-source="list" :loading="loading" :pagination="pagination" row-key="id" @change="handleTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button size="small" @click="$router.push({ path: '/app/chat', query: { convId: record.id } })">继续对话</a-button>
              <a-button size="small" danger @click="handleDelete(record.id)">删除</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { getConversationListApi, deleteConversationApi } from '@/api/app/conversation'

const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showTotal: (t: number) => `共 ${t} 条` })
const columns = [
  { title: '会话标题', dataIndex: 'title', key: 'title' },
  { title: '关联场景', dataIndex: 'sceneName', key: 'sceneName' },
  { title: '最后更新', dataIndex: 'updatedTime', key: 'updatedTime' },
  { title: '创建时间', dataIndex: 'createdTime', key: 'createdTime' },
  { title: '操作', key: 'action' },
]
const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getConversationListApi({ page: pagination.current, size: pagination.pageSize })
    list.value = res.data?.list || res.data?.content || res.data || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}
const handleTableChange = (p: any) => { pagination.current = p.current; pagination.pageSize = p.pageSize; loadData() }
const handleDelete = (id: number) => Modal.confirm({ title: '确认删除此会话？', onOk: async () => { await deleteConversationApi(id); message.success('删除成功'); loadData() } })
onMounted(loadData)
</script>
