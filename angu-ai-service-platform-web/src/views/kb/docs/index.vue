<template>
  <div>
    <a-page-header title="知识库文档管理" @back="$router.back()" style="padding:0 0 16px" />
    <a-card>
      <template #extra>
        <a-upload :before-upload="uploadDoc" accept=".pdf,.docx,.txt" :show-upload-list="false">
          <a-button type="primary"><UploadOutlined /> 上传文档</a-button>
        </a-upload>
      </template>
      <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'parseStatus'">
            <a-tag :color="record.parseStatus === 'SUCCESS' ? 'green' : record.parseStatus === 'PENDING' ? 'orange' : 'red'">{{ record.parseStatus }}</a-tag>
          </template>
          <template v-if="column.key === 'fileSize'">
            {{ record.fileSize ? (record.fileSize / 1024).toFixed(1) + ' KB' : '-' }}
          </template>
          <template v-if="column.key === 'action'">
            <a-button size="small" danger @click="handleDelete(record.id)">删除</a-button>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { UploadOutlined } from '@ant-design/icons-vue'
import { getDocListApi, uploadDocApi, deleteDocApi } from '@/api/kb/document'

const route = useRoute()
const kbId = Number(route.params.id)
const list = ref<any[]>([])
const loading = ref(false)
const columns = [
  { title: '文件名', dataIndex: 'fileName', key: 'fileName' },
  { title: '文件大小', key: 'fileSize' },
  { title: '解析状态', key: 'parseStatus' },
  { title: '上传时间', dataIndex: 'createdTime', key: 'createdTime' },
  { title: '操作', key: 'action' },
]
const loadData = async () => { loading.value = true; try { const res: any = await getDocListApi(kbId); list.value = res.data?.list || res.data || [] } finally { loading.value = false } }
const uploadDoc = async (file: File) => {
  const fd = new FormData(); fd.append('files', file)
  try { await uploadDocApi(kbId, fd); message.success('上传成功'); loadData() } catch { }
  return false
}
const handleDelete = (docId: number) => Modal.confirm({ title: '确认删除？', onOk: async () => { await deleteDocApi(kbId, docId); message.success('删除成功'); loadData() } })
onMounted(loadData)
</script>
