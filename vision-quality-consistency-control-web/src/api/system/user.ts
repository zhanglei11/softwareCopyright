import http from '../http'

export const userApi = {
  list: (params?: any) => http.get('/v1/system/users', { params }),
  get: (id: number) => http.get(`/v1/system/users/${id}`),
  create: (data: any) => http.post('/v1/system/users', data),
  update: (id: number, data: any) => http.put(`/v1/system/users/${id}`, data),
  remove: (id: number) => http.delete(`/v1/system/users/${id}`),
  resetPassword: (id: number) => http.post(`/v1/system/users/${id}/reset-password`),
  toggleStatus: (id: number, status: number) => http.patch(`/v1/system/users/${id}/status`, { status }),
}
