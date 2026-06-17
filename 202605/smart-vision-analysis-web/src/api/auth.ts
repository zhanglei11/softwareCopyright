import request from '@/utils/request'
import type { LoginParams } from '@/types'
export const loginApi = (data: LoginParams) => request.post('/api/auth/login', data)
export const getUserInfoApi = () => request.get('/api/auth/userInfo')
export const logoutApi = () => request.post('/api/auth/logout')
