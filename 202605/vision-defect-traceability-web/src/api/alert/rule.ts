import request from '@/utils/request'

export const getAlertRuleList = (params?: object) =>
  request.get('/api/v1/alerts/rules', { params })

export const addAlertRule = (data: object) =>
  request.post('/api/v1/alerts/rules', data)

export const updateAlertRule = (id: number, data: object) =>
  request.put(`/api/v1/alerts/rules/${id}`, data)

export const updateAlertRuleStatus = (id: number, status: number) =>
  request.put(`/api/v1/alerts/rules/${id}/status`, { status })

export const deleteAlertRule = (id: number) =>
  request.delete(`/api/v1/alerts/rules/${id}`)
