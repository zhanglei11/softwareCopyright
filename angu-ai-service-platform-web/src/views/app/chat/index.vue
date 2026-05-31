<template>
  <div class="chat-container">
    <div class="chat-sidebar">
      <div style="padding:12px;border-bottom:1px solid #f0f0f0">
        <a-button type="primary" block @click="newConversation"><PlusOutlined /> 新建对话</a-button>
      </div>
      <div class="conv-list">
        <div v-for="conv in conversations" :key="conv.id"
          class="conv-item" :class="{ active: currentConvId === conv.id }"
          @click="selectConversation(conv.id)">
          <div class="conv-title">{{ conv.title || '新对话' }}</div>
          <div class="conv-time">{{ conv.updatedTime?.slice(0,10) }}</div>
        </div>
      </div>
    </div>
    <div class="chat-main">
      <div class="chat-messages" ref="msgContainer">
        <div v-for="(msg, i) in messages" :key="i" :class="['message', msg.role === 'USER' ? 'user' : 'assistant']">
          <div class="msg-bubble">
            <pre style="white-space:pre-wrap;word-break:break-word;margin:0">{{ msg.content }}</pre>
          </div>
        </div>
        <div v-if="streaming" class="message assistant">
          <div class="msg-bubble"><a-spin size="small" /> 思考中...</div>
        </div>
      </div>
      <div class="chat-input">
        <a-textarea v-model:value="inputText" placeholder="输入消息，Enter发送，Shift+Enter换行..." :auto-size="{ minRows: 2, maxRows: 6 }"
          @keydown.enter.exact.prevent="sendMessage" />
        <a-button type="primary" :disabled="!inputText.trim() || streaming" @click="sendMessage" style="margin-top:8px">
          <SendOutlined /> 发送
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { PlusOutlined, SendOutlined } from '@ant-design/icons-vue'
import { getConversationListApi, createConversationApi, getMessageListApi } from '@/api/app/conversation'
import { getToken } from '@/utils/common'

const route = useRoute()
const conversations = ref<any[]>([])
const messages = ref<any[]>([])
const currentConvId = ref<number | null>(null)
const inputText = ref('')
const streaming = ref(false)
const msgContainer = ref<HTMLElement>()

const scrollBottom = () => nextTick(() => { if (msgContainer.value) msgContainer.value.scrollTop = msgContainer.value.scrollHeight })

const loadConversations = async () => {
  const res: any = await getConversationListApi()
  conversations.value = res.data?.list || res.data || []
}

const selectConversation = async (id: number) => {
  currentConvId.value = id
  const res: any = await getMessageListApi(id)
  messages.value = res.data || []
  scrollBottom()
}

const newConversation = async () => {
  const sceneId = route.query.sceneId ? Number(route.query.sceneId) : undefined
  const res: any = await createConversationApi({ sceneId })
  currentConvId.value = res.data?.id || res.data
  messages.value = []
  await loadConversations()
}

const sendMessage = async () => {
  if (!inputText.value.trim() || streaming.value) return
  if (!currentConvId.value) { await newConversation() }
  const userMsg = inputText.value.trim()
  inputText.value = ''
  messages.value.push({ role: 'USER', content: userMsg })
  scrollBottom()

  streaming.value = true
  let assistantContent = ''
  messages.value.push({ role: 'ASSISTANT', content: '' })

  try {
    const token = getToken()
    const response = await fetch(`/api/app/conversations/${currentConvId.value}/messages`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
      body: JSON.stringify({ content: userMsg }),
    })
    if (!response.ok || !response.body) { streaming.value = false; message.error('发送失败'); return }
    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buf = ''
    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      buf += decoder.decode(value, { stream: true })
      const lines = buf.split('\n')
      buf = lines.pop() || ''
      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.slice(5).trim()
          if (data === '[DONE]') { streaming.value = false; scrollBottom(); continue }
          assistantContent += data
          const last = messages.value[messages.value.length - 1]
          if (last.role === 'ASSISTANT') last.content = assistantContent
          scrollBottom()
        }
      }
    }
    streaming.value = false
  } catch { streaming.value = false; message.error('发送失败') }
}

onMounted(async () => {
  await loadConversations()
  if (conversations.value.length > 0) await selectConversation(conversations.value[0].id)
})
</script>

<style scoped>
.chat-container { display:flex; height:calc(100vh - 120px); gap:0; border-radius:8px; overflow:hidden; border:1px solid #f0f0f0; background:#fff; }
.chat-sidebar { width:240px; border-right:1px solid #f0f0f0; display:flex; flex-direction:column; }
.conv-list { flex:1; overflow-y:auto; }
.conv-item { padding:12px; cursor:pointer; border-bottom:1px solid #f8f8f8; }
.conv-item:hover, .conv-item.active { background:#e6f7ff; }
.conv-title { font-size:14px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.conv-time { font-size:12px; color:#999; margin-top:2px; }
.chat-main { flex:1; display:flex; flex-direction:column; }
.chat-messages { flex:1; overflow-y:auto; padding:16px; display:flex; flex-direction:column; gap:12px; }
.message { display:flex; }
.message.user { justify-content:flex-end; }
.message.assistant { justify-content:flex-start; }
.msg-bubble { max-width:70%; padding:10px 14px; border-radius:12px; font-size:14px; line-height:1.6; }
.message.user .msg-bubble { background:#1890ff; color:#fff; border-bottom-right-radius:4px; }
.message.assistant .msg-bubble { background:#f5f5f5; color:#333; border-bottom-left-radius:4px; }
.chat-input { padding:16px; border-top:1px solid #f0f0f0; }
</style>
