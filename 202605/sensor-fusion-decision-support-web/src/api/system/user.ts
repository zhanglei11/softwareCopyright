import request from '@/utils/request'

export function getUserListApi(params: any) {
  return request.get('/system/users', { params })
}

export function getUserDetailApi(id: number) {
  return request.get(`/system/users/${id}`)
}

export function addUserApi(data: any) {
  return request.post('/system/users', data)
}

export function editUserApi(id: number, data: any) {
  return request.put(`/system/users/${id}`, data)
}

export function deleteUserApi(id: number) {
  return request.delete(`/system/users/${id}`)
}

export function updateUserStatusApi(id: number, status: number) {
  return request.put(`/system/users/${id}/status`, null, { params: { status } })
}

export function resetPasswordApi(id: number) {
  return request.put(`/system/users/${id}/reset-password`)
}
