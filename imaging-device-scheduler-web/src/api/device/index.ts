import request from '@/utils/request'

export const getDeviceListApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/devices', { params })

export const getDeviceDetailApi = (id: number) =>
  request.get(`/api/v1/devices/${id}`)

export const addDeviceApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/devices', data)

export const editDeviceApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/devices/${id}`, data)

export const changeDeviceStatusApi = (id: number, status: number) =>
  request.put(`/api/v1/devices/${id}/status`, { status })

export const deleteDeviceApi = (id: number) =>
  request.delete(`/api/v1/devices/${id}`)

export const getAvailableDevicesApi = () =>
  request.get('/api/v1/devices/available')

export const getDeviceStatusOverviewApi = () =>
  request.get('/api/v1/devices/status-overview')

export const getDeviceParamsApi = (deviceId: number) =>
  request.get(`/api/v1/devices/${deviceId}/params`)

export const saveDeviceParamsApi = (deviceId: number, data: Record<string, unknown>[]) =>
  request.put(`/api/v1/devices/${deviceId}/params`, data)
