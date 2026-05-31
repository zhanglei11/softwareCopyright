import request from '@/utils/request'

export const jobApi = {
  list: (params?: any) => request.get('/api/jobs', { params }),
  create: (data: any) => request.post('/api/jobs', data),
  update: (id: number, data: any) => request.put(`/api/jobs/${id}`, data),
  remove: (id: number) => request.delete(`/api/jobs/${id}`),
  publish: (id: number) => request.put(`/api/jobs/${id}/publish`),
  close: (id: number) => request.put(`/api/jobs/${id}/close`),
}