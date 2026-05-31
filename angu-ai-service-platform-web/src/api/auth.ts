import request from '@/utils/request'

export const loginApi = (data: { username: string; password: string }) =>
  request.post('/api/auth/login', data)

export const getMeApi = () => request.get('/api/auth/me')

export const logoutApi = () => request.post('/api/auth/logout')
