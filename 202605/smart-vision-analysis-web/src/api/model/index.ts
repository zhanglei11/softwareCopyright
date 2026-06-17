import request from '@/utils/request'
export const getModelListApi = (p?: object) => request.get('/api/model', { params: p })
export const getModelDetailApi = (id: number) => request.get(`/api/model/${id}`)
export const createModelApi = (d: object) => request.post('/api/model', d)
export const updateModelApi = (id: number, d: object) => request.put(`/api/model/${id}`, d)
export const deprecateModelApi = (id: number) => request.post(`/api/model/${id}/deprecate`)
export const restoreModelApi = (id: number) => request.post(`/api/model/${id}/restore`)
export const deleteModelApi = (id: number) => request.delete(`/api/model/${id}`)
export const getModelTasksApi = (id: number) => request.get(`/api/model/${id}/tasks`)
