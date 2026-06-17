import request from '@/utils/request'
export const getRoleListApi = (p?: object) => request.get('/api/system/roles', { params: p })
export const createRoleApi = (d: object) => request.post('/api/system/roles', d)
export const updateRoleApi = (id: number, d: object) => request.put(`/api/system/roles/${id}`, d)
export const deleteRoleApi = (id: number) => request.delete(`/api/system/roles/${id}`)
