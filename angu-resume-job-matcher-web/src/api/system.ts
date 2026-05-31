import request from '@/utils/request'

export const systemApi = {
  listUsers: (params?: any) => request.get('/api/system/users', { params }),
  createUser: (data: any) => request.post('/api/system/users', data),
  updateUser: (id: number, data: any) => request.put(`/api/system/users/${id}`, data),
  deleteUser: (id: number) => request.delete(`/api/system/users/${id}`),
  resetPassword: (id: number, newPassword: string) => request.put(`/api/system/users/${id}/reset-password`, { newPassword }),
  updateUserStatus: (id: number, status: number) => request.put(`/api/system/users/${id}/status`, { status }),
  listRoles: () => request.get('/api/system/roles'),
  createRole: (data: any) => request.post('/api/system/roles', data),
  updateRole: (id: number, data: any) => request.put(`/api/system/roles/${id}`, data),
  deleteRole: (id: number) => request.delete(`/api/system/roles/${id}`),
  assignMenus: (id: number, menuIds: number[]) => request.put(`/api/system/roles/${id}/menus`, { menuIds }),
  menuTree: () => request.get('/api/system/menus/tree'),
  createMenu: (data: any) => request.post('/api/system/menus', data),
  updateMenu: (id: number, data: any) => request.put(`/api/system/menus/${id}`, data),
  deleteMenu: (id: number) => request.delete(`/api/system/menus/${id}`),
}