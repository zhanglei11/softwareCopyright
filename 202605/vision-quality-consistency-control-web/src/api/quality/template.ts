import http from '../http'

export const templateApi = {
  list: (params?: any) => http.get('/v1/quality/templates', { params }),
  get: (id: number) => http.get(`/v1/quality/templates/${id}`),
  create: (data: any) => http.post('/v1/quality/templates', data),
  update: (id: number, data: any) => http.put(`/v1/quality/templates/${id}`, data),
  remove: (id: number) => http.delete(`/v1/quality/templates/${id}`),
  setStatus: (id: number, status: number) => http.patch(`/v1/quality/templates/${id}/status`, { status }),
}
