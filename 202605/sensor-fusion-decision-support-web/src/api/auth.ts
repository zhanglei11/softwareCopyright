import request from '@/utils/request'

export function loginApi(data: { username: string; password: string }) {
  return request.post('/auth/login', data)
}

export function logoutApi() {
  return request.post('/auth/logout')
}

export function getUserInfoApi() {
  return request.get('/auth/info')
}
