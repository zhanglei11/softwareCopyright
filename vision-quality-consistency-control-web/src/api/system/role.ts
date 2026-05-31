import http from '../http'

export const roleApi = {
  list: (params?: any) => http.get('/v1/system/roles', { params }),
  get: (id: number) => http.get(`/v1/system/roles/${id}`),
  create: (data: any) => http.post('/v1/system/roles', data),
  update: (id: number, data: any) => http.put(`/v1/system/roles/${id}`, data),
  remove: (id: number) => http.delete(`/v1/system/roles/${id}`),
  getMenus: (id: number) => http.get(`/v1/system/roles/${id}/menus`),
  assignMenus: (id: number, menuIds: number[]) => http.put(`/v1/system/roles/${id}/menus`, { menuIds }),
}
