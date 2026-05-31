import request from '@/utils/request'

export const loginApi = (data: { username: string; password: string }) =>
  request.post('/api/v1/auth/login', data)

export const refreshTokenApi = (data: { refreshToken: string }) =>
  request.post('/api/v1/auth/refresh', data)

export const logoutApi = () =>
  request.post('/api/v1/auth/logout')
