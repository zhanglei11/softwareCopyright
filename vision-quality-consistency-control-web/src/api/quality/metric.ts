import http from '../http'

export const metricApi = {
  list: (params?: any) => http.get('/v1/quality/metrics', { params }),
  get: (id: number) => http.get(`/v1/quality/metrics/${id}`),
  create: (data: any) => http.post('/v1/quality/metrics', data),
  update: (id: number, data: any) => http.put(`/v1/quality/metrics/${id}`, data),
  remove: (id: number) => http.delete(`/v1/quality/metrics/${id}`),
  setStatus: (id: number, status: number) => http.patch(`/v1/quality/metrics/${id}/status`, { status }),
}
