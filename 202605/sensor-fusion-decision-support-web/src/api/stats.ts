import request from '@/utils/request'

export function getFusionSummaryApi(params?: any) {
  return request.get('/stats/fusion/summary', { params })
}

export function getDecisionSummaryApi(params?: any) {
  return request.get('/stats/decision/summary', { params })
}
