import request from '@/utils/request'

export const getDeviceStatusStatsApi = () =>
  request.get('/api/v1/statistics/device/status')

export const getDeviceTrendApi = (params?: { days?: number }) =>
  request.get('/api/v1/statistics/device/trend', { params })

export const getTaskStatusStatsApi = () =>
  request.get('/api/v1/statistics/task/status')

export const getTaskTrendApi = (params?: { days?: number }) =>
  request.get('/api/v1/statistics/task/trend', { params })

export const getTaskBySceneApi = () =>
  request.get('/api/v1/statistics/task/by-scene')

export const getDeviceFaultStatsApi = () =>
  request.get('/api/v1/statistics/device/fault')
