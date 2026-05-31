import request from '@/utils/request'
export const getTaskReportApi = (taskId: number) => request.get(`/api/report/task/${taskId}`)
export const getSummaryReportApi = (p?: object) => request.get('/api/report/summary', { params: p })
export const exportTaskReportApi = (taskId: number, fmt: string) => request.get(`/api/report/task/${taskId}/export`, { params: { format: fmt }, responseType: 'blob' })
