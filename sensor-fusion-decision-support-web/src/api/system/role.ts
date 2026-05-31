import request from '@/utils/request'

export function getRoleListApi(params?: any) {
  return request.get('/system/roles', { params })
}

export function getRoleDetailApi(id: number) {
  return request.get(`/system/roles/${id}`)
}

export function addRoleApi(data: any) {
  return request.post('/system/roles', data)
}

export function editRoleApi(id: number, data: any) {
  return request.put(`/system/roles/${id}`, data)
}

export function deleteRoleApi(id: number) {
  return request.delete(`/system/roles/${id}`)
}

export function getRoleMenusApi(id: number) {
  return request.get(`/system/roles/${id}/menus`)
}

export function assignRoleMenusApi(id: number, menuIds: number[]) {
  return request.put(`/system/roles/${id}/menus`, { menuIds })
}
