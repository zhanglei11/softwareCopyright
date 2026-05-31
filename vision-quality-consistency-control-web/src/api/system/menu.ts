import http from '../http'

export const menuApi = {
  tree: () => http.get('/v1/system/menus/tree'),
  create: (data: any) => http.post('/v1/system/menus', data),
  update: (id: number, data: any) => http.put(`/v1/system/menus/${id}`, data),
  remove: (id: number) => http.delete(`/v1/system/menus/${id}`),
}
