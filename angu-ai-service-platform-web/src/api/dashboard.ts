import request from '@/utils/request'

export function getDashboardStatsApi() {
  return request.get('/api/stats/dashboard')
}

export function getSceneCallStatsApi(params?: object) {
  return request.get('/api/stats/scene-calls', { params })
}
