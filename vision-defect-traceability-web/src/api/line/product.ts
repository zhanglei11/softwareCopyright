import request from '@/utils/request'

export const getProductList = (params?: object) =>
  request.get('/api/v1/products', { params })

export const addProduct = (data: object) =>
  request.post('/api/v1/products', data)

export const updateProduct = (id: number, data: object) =>
  request.put(`/api/v1/products/${id}`, data)

export const updateProductStatus = (id: number, status: number) =>
  request.put(`/api/v1/products/${id}/status`, { status })

export const deleteProduct = (id: number) =>
  request.delete(`/api/v1/products/${id}`)
