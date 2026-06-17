import request from '@/utils/request'

export function getDecisionResultListApi(params?: any) {
  return request.get('/decision/results', { params })
}

export function getDecisionResultDetailApi(id: number) {
  return request.get(`/decision/results/${id}`)
}

export function getDecisionResultTraceApi(id: number) {
  return request.get(`/decision/results/${id}/trace`)
}

export function getDecisionFrequencyApi(params?: any) {
  return request.get('/decision/results/frequency', { params })
}
