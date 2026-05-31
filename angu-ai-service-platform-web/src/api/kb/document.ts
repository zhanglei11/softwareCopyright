import request from '@/utils/request'
export const getDocListApi = (kbId: number, params?: any) => request.get(`/api/kb/${kbId}/documents`, { params })
export const uploadDocApi = (kbId: number, formData: FormData) =>
  request.post(`/api/kb/${kbId}/documents`, formData, { headers: { 'Content-Type': 'multipart/form-data' } })
export const deleteDocApi = (kbId: number, docId: number) => request.delete(`/api/kb/${kbId}/documents/${docId}`)
