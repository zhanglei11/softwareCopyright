import request from '@/utils/request'
export const getMenuTreeApi = () => request.get('/api/system/menus/tree')
export const createMenuApi = (d: object) => request.post('/api/system/menus', d)
export const updateMenuApi = (id: number, d: object) => request.put(`/api/system/menus/${id}`, d)
export const deleteMenuApi = (id: number) => request.delete(`/api/system/menus/${id}`)
