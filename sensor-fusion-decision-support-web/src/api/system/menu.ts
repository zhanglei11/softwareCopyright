import request from '@/utils/request'

export function getMenuTreeApi() {
  return request.get('/system/menus/tree')
}

export function getMenuDetailApi(id: number) {
  return request.get(`/system/menus/${id}`)
}

export function addMenuApi(data: any) {
  return request.post('/system/menus', data)
}

export function editMenuApi(id: number, data: any) {
  return request.put(`/system/menus/${id}`, data)
}

export function deleteMenuApi(id: number) {
  return request.delete(`/system/menus/${id}`)
}
