import request from '@/utils/request'

export const getUserListApi = (params?: any) => request.get('/api/system/users', { params })
export const createUserApi = (data: any) => request.post('/api/system/users', data)
export const updateUserApi = (id: number, data: any) => request.put(`/api/system/users/${id}`, data)
export const deleteUserApi = (id: number) => request.delete(`/api/system/users/${id}`)
export const resetPasswordApi = (id: number, data: any) => request.put(`/api/system/users/${id}/password`, data)
export const getUserRolesApi = (id: number) => request.get(`/api/system/users/${id}/roles`)
export const assignRolesApi = (id: number, roleIds: number[]) => request.put(`/api/system/users/${id}/roles`, { roleIds })
