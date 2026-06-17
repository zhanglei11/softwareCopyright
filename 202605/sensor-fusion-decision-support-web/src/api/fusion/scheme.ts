import request from '@/utils/request'

export function getFusionSchemeListApi(params?: any) {
  return request.get('/fusion/schemes', { params })
}

export function getFusionSchemeDetailApi(id: number) {
  return request.get(`/fusion/schemes/${id}`)
}

export function addFusionSchemeApi(data: any) {
  return request.post('/fusion/schemes', data)
}

export function editFusionSchemeApi(id: number, data: any) {
  return request.put(`/fusion/schemes/${id}`, data)
}

export function deleteFusionSchemeApi(id: number) {
  return request.delete(`/fusion/schemes/${id}`)
}

export function updateFusionSchemeStatusApi(id: number, status: number) {
  return request.put(`/fusion/schemes/${id}/status`, null, { params: { status } })
}

export function getSchemeRulesApi(schemeId: number) {
  return request.get(`/fusion/schemes/${schemeId}/rules`)
}

export function addSchemeRuleApi(schemeId: number, data: any) {
  return request.post(`/fusion/schemes/${schemeId}/rules`, data)
}

export function editSchemeRuleApi(schemeId: number, ruleId: number, data: any) {
  return request.put(`/fusion/schemes/${schemeId}/rules/${ruleId}`, data)
}

export function deleteSchemeRuleApi(schemeId: number, ruleId: number) {
  return request.delete(`/fusion/schemes/${schemeId}/rules/${ruleId}`)
}

export function getSchemeWeightsApi(schemeId: number) {
  return request.get(`/fusion/schemes/${schemeId}/weights`)
}

export function saveSchemeWeightsApi(schemeId: number, data: any) {
  return request.put(`/fusion/schemes/${schemeId}/weights`, data)
}
