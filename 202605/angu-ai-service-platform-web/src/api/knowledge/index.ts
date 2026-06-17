import request from '@/utils/request'

export function getKnowledgeListApi(params?: object) {
  return request.get('/api/knowledge/bases', { params })
}

export function createKnowledgeApi(data: object) {
  return request.post('/api/knowledge/bases', data)
}

export function deleteKnowledgeApi(id: string | number) {
  return request.delete(`/api/knowledge/bases/${id}`)
}

export function getKnowledgeDocsApi(id: string | number, params?: object) {
  return request.get(`/api/knowledge/bases/${id}/documents`, { params })
}

export function uploadDocumentApi(id: string | number, data: FormData) {
  return request.post(`/api/knowledge/bases/${id}/documents`, data, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export function searchKnowledgeApi(data: object) {
  return request.post('/api/knowledge/search', data)
}
