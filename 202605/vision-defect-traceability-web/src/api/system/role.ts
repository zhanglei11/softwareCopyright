import request from '@/utils/request'

export const getRoleList = (params?: object) =>
  request.get('/api/v1/system/roles', { params })

export const addRole = (data: object) =>
  request.post('/api/v1/system/roles', data)

export const updateRole = (id: number, data: object) =>
  request.put(`/api/v1/system/roles/${id}`, data)

export const deleteRole = (id: number) =>
  request.delete(`/api/v1/system/roles/${id}`)
