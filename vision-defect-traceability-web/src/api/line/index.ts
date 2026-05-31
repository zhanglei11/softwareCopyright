import request from '@/utils/request'

export const getLineList = (params?: object) =>
  request.get('/api/v1/lines', { params })

export const getLineDetail = (id: number) =>
  request.get(`/api/v1/lines/${id}`)

export const addLine = (data: object) =>
  request.post('/api/v1/lines', data)

export const updateLine = (id: number, data: object) =>
  request.put(`/api/v1/lines/${id}`, data)

export const updateLineStatus = (id: number, status: number) =>
  request.put(`/api/v1/lines/${id}/status`, { status })

export const deleteLine = (id: number) =>
  request.delete(`/api/v1/lines/${id}`)
