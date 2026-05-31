<template>
  <div>
    <a-page-header title="文档分析" style="padding:0 0 16px" />
    <a-card>
      <a-upload-dragger :before-upload="beforeUpload" accept=".pdf,.docx,.txt" :show-upload-list="false">
        <p class="ant-upload-drag-icon"><InboxOutlined /></p>
        <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
        <p class="ant-upload-hint">支持 PDF、Word、TXT 格式</p>
      </a-upload-dragger>
      <div v-if="docContent" style="margin-top:16px">
        <a-divider>文档内容</a-divider>
        <a-textarea :value="docContent" :rows="10" readonly />
        <a-divider>AI分析</a-divider>
        <a-textarea v-model:value="analysisPrompt" :rows="3" placeholder="请输入分析指令，如：提取关键信息、总结要点..." />
        <a-button type="primary" :loading="analyzing" style="margin-top:8px" @click="analyze"><ThunderboltOutlined /> 开始分析</a-button>
        <div v-if="analysisResult" style="margin-top:16px">
          <a-alert type="info" message="分析结果" :description="analysisResult" show-icon />
        </div>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { InboxOutlined, ThunderboltOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import request from '@/utils/request'

const docContent = ref('')
const analysisPrompt = ref('')
const analysisResult = ref('')
const analyzing = ref(false)

const beforeUpload = async (file: File) => {
  if (file.type === 'text/plain') {
    const text = await file.text()
    docContent.value = text
  } else {
    const formData = new FormData()
    formData.append('file', file)
    try {
      const res: any = await request.post('/api/app/doc/parse', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
      docContent.value = res.data?.content || ''
    } catch { message.error('文档解析失败') }
  }
  return false
}

const analyze = async () => {
  if (!docContent.value || !analysisPrompt.value) { message.warning('请先上传文档并填写分析指令'); return }
  analyzing.value = true
  try {
    const res: any = await request.post('/api/app/doc/analyze', { content: docContent.value, prompt: analysisPrompt.value })
    analysisResult.value = res.data?.result || ''
  } catch { message.error('分析失败，该功能暂未开放') } finally { analyzing.value = false }
}
</script>
