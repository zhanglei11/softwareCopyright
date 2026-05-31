import request from '@/utils/request'

export const getSceneGroupListApi = () =>
  request.get('/api/v1/scene-groups')

export const addSceneGroupApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/scene-groups', data)

export const editSceneGroupApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/scene-groups/${id}`, data)

export const deleteSceneGroupApi = (id: number) =>
  request.delete(`/api/v1/scene-groups/${id}`)

export const getSceneListApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/scenes', { params })

export const getSceneDetailApi = (id: number) =>
  request.get(`/api/v1/scenes/${id}`)

export const addSceneApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/scenes', data)

export const editSceneApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/scenes/${id}`, data)

export const changeSceneStatusApi = (id: number, status: number) =>
  request.put(`/api/v1/scenes/${id}/status`, { status })

export const deleteSceneApi = (id: number) =>
  request.delete(`/api/v1/scenes/${id}`)
