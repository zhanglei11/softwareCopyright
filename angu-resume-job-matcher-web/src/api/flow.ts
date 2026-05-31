import request from '@/utils/request'

export const applicationApi = {
  list: (params?: any) => request.get('/api/applications', { params }),
  create: (data: any) => request.post('/api/applications', data),
  updateStatus: (id: number, data: any) => request.put(`/api/applications/${id}/status`, data),
  logs: (id: number) => request.get(`/api/applications/${id}/logs`),
}

export const interviewApi = {
  list: (params?: any) => request.get('/api/interviews', { params }),
  create: (data: any) => request.post('/api/interviews', data),
  update: (id: number, data: any) => request.put(`/api/interviews/${id}`, data),
  updateResult: (id: number, data: any) => request.put(`/api/interviews/${id}/result`, data),
}