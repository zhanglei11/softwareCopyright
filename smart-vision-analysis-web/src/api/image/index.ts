import request from '@/utils/request'
export const getImageListApi = (p?: object) => request.get('/api/image', { params: p })
export const uploadImageApi = (d: FormData) => request.post('/api/image/upload', d, { headers: { 'Content-Type': 'multipart/form-data' } })
export const uploadBatchImageApi = (d: FormData) => request.post('/api/image/upload/batch', d, { headers: { 'Content-Type': 'multipart/form-data' } })
export const deleteImageApi = (id: number) => request.delete(`/api/image/${id}`)
export const getImageCategoriesApi = (p?: object) => request.get('/api/image/categories/tree', { params: p })
export const createCategoryApi = (d: object) => request.post('/api/image/categories', d)
export const updateCategoryApi = (id: number, d: object) => request.put(`/api/image/categories/${id}`, d)
export const deleteCategoryApi = (id: number) => request.delete(`/api/image/categories/${id}`)
