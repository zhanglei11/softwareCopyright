import request from '@/utils/request'
export const getSceneListApi = (params?: any) => request.get('/api/ai/scenes', { params })
export const getSceneDetailApi = (id: number) => request.get(`/api/ai/scenes/${id}`)
export const createSceneApi = (data: any) => request.post('/api/ai/scenes', data)
export const updateSceneApi = (id: number, data: any) => request.put(`/api/ai/scenes/${id}`, data)
export const deleteSceneApi = (id: number) => request.delete(`/api/ai/scenes/${id}`)
export const publishSceneApi = (id: number) => request.put(`/api/ai/scenes/${id}/publish`)
export const offlineSceneApi = (id: number) => request.put(`/api/ai/scenes/${id}/offline`)
