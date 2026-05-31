import request from '@/utils/request'

export const getAlertRecordList = (params?: object) =>
  request.get('/api/v1/alerts/records', { params })

export const handleAlert = (id: number, data: { handleRemark: string }) =>
  request.patch(`/api/v1/alerts/records/${id}/handle`, data)
