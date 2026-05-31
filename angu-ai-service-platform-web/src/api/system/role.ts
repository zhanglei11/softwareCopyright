import request from '@/utils/request'

export const getRoleListApi = (params?: any) => request.get('/api/system/roles', { params })
export const createRoleApi = (data: any) => request.post('/api/system/roles', data)
export const updateRoleApi = (id: number, data: any) => request.put(`/api/system/roles/${id}`, data)
export const deleteRoleApi = (id: number) => request.delete(`/api/system/roles/${id}`)
export const assignMenusApi = (id: number, menuIds: number[]) => request.put(`/api/system/roles/${id}/menus`, { menuIds })
export const assignScenesApi = (id: number, sceneIds: number[]) => request.put(`/api/system/roles/${id}/scenes`, { sceneIds })
