import request from '@/utils/request'

export const getDispatchOverviewApi = () =>
  request.get('/api/v1/dispatch/overview')

export const getGanttDataApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/dispatch/gantt', { params })

export const getTimeoutAlertsApi = () =>
  request.get('/api/v1/dispatch/alerts/timeout')

export const getDispatchConfigApi = () =>
  request.get('/api/v1/dispatch/config')

export const updateDispatchConfigApi = (data: Record<string, unknown>) =>
  request.put('/api/v1/dispatch/config', data)

export const getDispatchLogsApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/dispatch/logs', { params })
