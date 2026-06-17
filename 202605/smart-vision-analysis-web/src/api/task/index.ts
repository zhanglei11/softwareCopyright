import request from '@/utils/request'
export const getTaskListApi = (p?: object) => request.get('/api/task', { params: p })
export const getTaskDetailApi = (id: number) => request.get(`/api/task/${id}`)
export const createTaskApi = (d: object) => request.post('/api/task', d)
export const submitTaskApi = (id: number) => request.post(`/api/task/${id}/submit`)
export const cancelTaskApi = (id: number) => request.post(`/api/task/${id}/cancel`)
export const deleteTaskApi = (id: number) => request.delete(`/api/task/${id}`)
