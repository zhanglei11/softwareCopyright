import request from '@/utils/request'
export const getCategoryListApi = (params?: any) => request.get('/api/ai/categories', { params })
export const createCategoryApi = (data: any) => request.post('/api/ai/categories', data)
export const updateCategoryApi = (id: number, data: any) => request.put(`/api/ai/categories/${id}`, data)
export const deleteCategoryApi = (id: number) => request.delete(`/api/ai/categories/${id}`)
