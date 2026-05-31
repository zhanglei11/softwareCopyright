import request from '@/utils/request'
export const getUserListApi = (p?: object) => request.get('/api/system/users', { params: p })
export const createUserApi = (d: object) => request.post('/api/system/users', d)
export const updateUserApi = (id: number, d: object) => request.put(`/api/system/users/${id}`, d)
export const deleteUserApi = (id: number) => request.delete(`/api/system/users/${id}`)
