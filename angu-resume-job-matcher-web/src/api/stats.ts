import request from '@/utils/request'

export const statsApi = {
  dashboard: () => request.get('/api/stats/dashboard'),
  source: (params?: any) => request.get('/api/stats/source', { params }),
}