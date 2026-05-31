import http from './http'

export interface LoginReq { username: string; password: string }
export interface LoginRes { accessToken: string; tokenType: string; expiresIn: number }

export const authApi = {
  login: (data: LoginReq) => http.post<any, { code: number; data: LoginRes }>('/v1/auth/login', data),
  logout: () => http.post('/v1/auth/logout'),
  userinfo: () => http.get<any, { code: number; data: any }>('/v1/auth/userinfo'),
}
