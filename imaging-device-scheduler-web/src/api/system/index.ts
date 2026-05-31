import request from '@/utils/request'

// Users
export const getUserListApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/system/users', { params })

export const addUserApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/system/users', data)

export const editUserApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/system/users/${id}`, data)

export const deleteUserApi = (id: number) =>
  request.delete(`/api/v1/system/users/${id}`)

export const changeUserStatusApi = (id: number, status: number) =>
  request.put(`/api/v1/system/users/${id}/status`, { status })

export const resetPasswordApi = (id: number, password: string) =>
  request.put(`/api/v1/system/users/${id}/password`, { password })

// Roles
export const getRoleListApi = (params?: Record<string, unknown>) =>
  request.get('/api/v1/system/roles', { params })

export const addRoleApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/system/roles', data)

export const editRoleApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/system/roles/${id}`, data)

export const deleteRoleApi = (id: number) =>
  request.delete(`/api/v1/system/roles/${id}`)

export const assignMenusApi = (id: number, menuIds: number[]) =>
  request.put(`/api/v1/system/roles/${id}/menus`, { menuIds })

// Menus
export const getMenuListApi = () =>
  request.get('/api/v1/system/menus/tree')

export const addMenuApi = (data: Record<string, unknown>) =>
  request.post('/api/v1/system/menus', data)

export const editMenuApi = (id: number, data: Record<string, unknown>) =>
  request.put(`/api/v1/system/menus/${id}`, data)

export const deleteMenuApi = (id: number) =>
  request.delete(`/api/v1/system/menus/${id}`)
