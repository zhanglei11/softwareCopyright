import request from '@/utils/request'
export const getResultListApi = (p?: object) => request.get('/api/result', { params: p })
export const getResultDetailApi = (id: number) => request.get(`/api/result/${id}`)
export const updateResultBoxesApi = (id: number, d: object) => request.put(`/api/result/${id}/boxes`, d)
export const confirmResultApi = (id: number) => request.post(`/api/result/${id}/confirm`)
export const markRevisionApi = (id: number) => request.post(`/api/result/${id}/reject`)
export const batchConfirmResultApi = (d: object) => request.post('/api/result/batch/review', d)
export const addBoxApi = (resultId: number, d: object) => request.post(`/api/result/${resultId}/boxes`, d)
export const deleteBoxApi = (resultId: number, boxId: number) => request.delete(`/api/result/${resultId}/boxes/${boxId}`)
