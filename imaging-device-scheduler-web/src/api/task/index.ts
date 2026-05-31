import request from '@/utils/request'

export const getTaskListApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/tasks', { params })

export const getTaskDetailApi = (id: number) =>
  request.get(`/api/v1/tasks/${id}`)

export const addTaskApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/tasks', data)

export const editTaskApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/tasks/${id}`, data)

export const assignDevicesApi = (id: number, deviceIds: number[]) =>
  request.post(`/api/v1/tasks/${id}/devices`, { deviceIds })

export const unbindDevicesApi = (id: number) =>
  request.delete(`/api/v1/tasks/${id}/devices`)

export const startTaskApi = (id: number) =>
  request.post(`/api/v1/tasks/${id}/start`)

export const completeTaskApi = (id: number) =>
  request.post(`/api/v1/tasks/${id}/complete`)

export const cancelTaskApi = (id: number) =>
  request.post(`/api/v1/tasks/${id}/cancel`)
