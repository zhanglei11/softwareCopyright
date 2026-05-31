import request from '@/utils/request'

export const getUserList = (params?: object) =>
  request.get('/api/v1/system/users', { params })

export const addUser = (data: object) =>
  request.post('/api/v1/system/users', data)

export const updateUser = (id: number, data: object) =>
  request.put(`/api/v1/system/users/${id}`, data)

export const updateUserStatus = (id: number, status: number) =>
  request.put(`/api/v1/system/users/${id}/status`, { status })

export const resetPassword = (id: number) =>
  request.post(`/api/v1/system/users/${id}/reset-password`)

export const deleteUser = (id: number) =>
  request.delete(`/api/v1/system/users/${id}`)
