import request from '@/utils/request'
export const getModelListApi = (params?: any) => request.get('/api/ai/models', { params })
export const createModelApi = (data: any) => request.post('/api/ai/models', data)
export const updateModelApi = (id: number, data: any) => request.put(`/api/ai/models/${id}`, data)
export const deleteModelApi = (id: number) => request.delete(`/api/ai/models/${id}`)
export const updateModelStatusApi = (id: number, status: number) => request.put(`/api/ai/models/${id}/status/${status}`)
