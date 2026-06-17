import request from '@/utils/request'

export const login = (data: { username: string; password: string }) =>
  request({ url: '/api/auth/login', method: 'post', data })

export const refreshToken = (data: { refreshToken: string }) =>
  request({ url: '/api/auth/refresh', method: 'post', data })

export const logout = () =>
  request({ url: '/api/auth/logout', method: 'post' })

export const getMenus = () =>
  request({ url: '/api/auth/menus', method: 'get' })
