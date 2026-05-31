<template>
  <div>
    <a-page-header title="内容生成" style="padding:0 0 16px" />
    <a-row :gutter="16">
      <a-col :span="10">
        <a-card title="生成配置">
          <a-form :model="form" :label-col="{ span: 8 }">
            <a-form-item label="选择场景">
              <a-select v-model:value="form.sceneId" placeholder="请选择场景" @change="loadSceneInfo">
                <a-select-option v-for="s in scenes" :key="s.id" :value="s.id">{{ s.name }}</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="生成指令">
              <a-textarea v-model:value="form.prompt" :rows="6" placeholder="请输入生成内容的具体要求..." />
            </a-form-item>
            <a-form-item :label-col="{ offset: 8 }">
              <a-button type="primary" block :loading="generating" @click="generate">
                <ThunderboltOutlined /> 生成内容
              </a-button>
            </a-form-item>
          </a-form>
        </a-card>
      </a-col>
      <a-col :span="14">
        <a-card title="生成结果" :extra="result ? '复制' : ''">
          <a-spin :spinning="generating">
            <a-textarea v-if="result" :value="result" :rows="20" readonly />
            <a-empty v-else description="暂无内容，请先生成" />
          </a-spin>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ThunderboltOutlined } from '@ant-design/icons-vue'
import { getAppSceneListApi } from '@/api/app/scene'
import { createConversationApi } from '@/api/app/conversation'
import { message } from 'ant-design-vue'
import request from '@/utils/request'

const scenes = ref<any[]>([])
const result = ref('')
const generating = ref(false)
const form = reactive({ sceneId: undefined as any, prompt: '' })

onMounted(async () => {
  const res: any = await getAppSceneListApi()
  scenes.value = res.data?.list || res.data || []
})

const loadSceneInfo = () => {}

const generate = async () => {
  if (!form.prompt.trim()) { message.warning('请输入生成指令'); return }
  generating.value = true
  try {
    const res: any = await request.post('/api/app/generate', { sceneId: form.sceneId, prompt: form.prompt })
    result.value = res.data?.content || ''
  } catch { message.error('生成失败，该功能暂未开放') } finally { generating.value = false }
}
</script>
