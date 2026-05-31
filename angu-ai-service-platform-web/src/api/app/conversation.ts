import request from '@/utils/request'
export const getConversationListApi = (params?: any) => request.get('/api/app/conversations', { params })
export const createConversationApi = (data: any) => request.post('/api/app/conversations', data)
export const getMessageListApi = (conversationId: number) => request.get(`/api/app/conversations/${conversationId}/messages`)
export const deleteConversationApi = (id: number) => request.delete(`/api/app/conversations/${id}`)
export const sendMessageApi = (conversationId: number, data: any) => request.post(`/api/app/conversations/${conversationId}/messages`, data)
// SSE streaming URL builder
export const buildStreamUrl = (conversationId: number) =>
  `${import.meta.env.VITE_API_URL || ''}/api/app/conversations/${conversationId}/messages/stream`
