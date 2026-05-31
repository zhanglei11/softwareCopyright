import http from '../http'

export const analysisApi = {
  taskReport: (taskId: number) => http.get(`/v1/quality/analysis/tasks/${taskId}`),
  trend: (params?: any) => http.get('/v1/quality/analysis/trend', { params }),
}
