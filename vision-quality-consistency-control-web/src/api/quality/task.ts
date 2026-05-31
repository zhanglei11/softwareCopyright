import http from '../http'

export const taskApi = {
  list: (params?: any) => http.get('/v1/quality/tasks', { params }),
  get: (id: number) => http.get(`/v1/quality/tasks/${id}`),
  create: (data: any) => http.post('/v1/quality/tasks', data),
  start: (id: number) => http.post(`/v1/quality/tasks/${id}/start`),
  complete: (id: number) => http.post(`/v1/quality/tasks/${id}/complete`),
  cancel: (id: number) => http.post(`/v1/quality/tasks/${id}/cancel`),
  records: (id: number, params?: any) => http.get(`/v1/quality/tasks/${id}/records`, { params }),
  submitRecord: (data: any) => http.post('/v1/quality/tasks/records', data),
}
