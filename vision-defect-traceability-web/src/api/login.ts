import request from '@/utils/request'
import type { LoginParams, LoginResult, ApiResponse } from '@/types'

export const login = (data: LoginParams) =>
  request.post<ApiResponse<LoginResult>>('/api/v1/auth/login', data)

export const logout = () =>
  request.post('/api/v1/auth/logout')
