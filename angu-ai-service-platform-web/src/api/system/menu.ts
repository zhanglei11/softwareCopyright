import request from '@/utils/request'

export const getMenuTreeApi = () => request.get('/api/system/menus/tree')
export const createMenuApi = (data: any) => request.post('/api/system/menus', data)
export const updateMenuApi = (id: number, data: any) => request.put(`/api/system/menus/${id}`, data)
export const deleteMenuApi = (id: number) => request.delete(`/api/system/menus/${id}`)
