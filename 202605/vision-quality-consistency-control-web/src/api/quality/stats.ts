import http from '../http'

export const statsApi = {
  dashboard: () => http.get('/v1/stats/dashboard'),
  trend: (params?: any) => http.get('/v1/stats/trend', { params }),
}
