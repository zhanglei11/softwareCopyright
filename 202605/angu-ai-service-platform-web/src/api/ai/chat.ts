import request from '@/utils/request'

export function sendChatApi(data: object) {
  return request.post('/api/ai/chat/send', data)
}

export function getChatHistoryApi(params?: object) {
  return request.get('/api/ai/chat/history', { params })
}

export function getChatSessionListApi(params?: object) {
  return request.get('/api/ai/chat/sessions', { params })
}

export function deleteChatSessionApi(id: string | number) {
  return request.delete(`/api/ai/chat/sessions/${id}`)
}
