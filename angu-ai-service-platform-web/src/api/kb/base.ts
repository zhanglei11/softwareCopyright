import request from '@/utils/request'
export const getKbListApi = (params?: any) => request.get('/api/kb', { params })
export const createKbApi = (data: any) => request.post('/api/kb', data)
export const updateKbApi = (id: number, data: any) => request.put(`/api/kb/${id}`, data)
export const deleteKbApi = (id: number) => request.delete(`/api/kb/${id}`)
