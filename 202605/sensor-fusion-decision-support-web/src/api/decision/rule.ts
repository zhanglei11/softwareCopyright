import request from '@/utils/request'

export function getDecisionRuleListApi(params?: any) {
  return request.get('/decision/rules', { params })
}

export function getDecisionRuleDetailApi(id: number) {
  return request.get(`/decision/rules/${id}`)
}

export function addDecisionRuleApi(data: any) {
  return request.post('/decision/rules', data)
}

export function editDecisionRuleApi(id: number, data: any) {
  return request.put(`/decision/rules/${id}`, data)
}

export function deleteDecisionRuleApi(id: number) {
  return request.delete(`/decision/rules/${id}`)
}

export function updateDecisionRuleStatusApi(id: number, status: number) {
  return request.put(`/decision/rules/${id}/status`, null, { params: { status } })
}

export function getDecisionConditionListApi(params?: any) {
  return request.get('/decision/conditions', { params })
}

export function addDecisionConditionApi(data: any) {
  return request.post('/decision/conditions', data)
}

export function editDecisionConditionApi(id: number, data: any) {
  return request.put(`/decision/conditions/${id}`, data)
}

export function deleteDecisionConditionApi(id: number) {
  return request.delete(`/decision/conditions/${id}`)
}
