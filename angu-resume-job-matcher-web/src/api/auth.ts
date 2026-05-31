import request from '@/utils/request'

export const authApi = {
  login: (data: any) => request.post('/api/auth/login', data),
  me: () => request.get('/api/auth/me'),
  myMenus: () => request.get('/api/system/menus/my-tree'),
  logout: () => request.post('/api/auth/logout'),
}