import request from '@/utils/request'

export const getCategoryList = (params?: object) =>
  request.get('/api/v1/defect/categories', { params })

export const addCategory = (data: object) =>
  request.post('/api/v1/defect/categories', data)

export const updateCategory = (id: number, data: object) =>
  request.put(`/api/v1/defect/categories/${id}`, data)

export const updateCategoryStatus = (id: number, status: number) =>
  request.put(`/api/v1/defect/categories/${id}/status`, { status })

export const deleteCategory = (id: number) =>
  request.delete(`/api/v1/defect/categories/${id}`)
