import request from '@/utils/request'
import type { LoginParams } from '@/types'

export function loginApi(data: LoginParams) {
  return request.post('/api/auth/login', data)
}

export function getUserInfoApi() {
  return request.get('/api/auth/userInfo')
}

export function logoutApi() {
  return request.post('/api/auth/logout')
}

export function getMenusApi() {
  return request.get('/api/auth/menus')
}
