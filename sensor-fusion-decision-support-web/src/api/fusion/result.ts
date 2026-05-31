import request from '@/utils/request'

export function getFusionResultListApi(params?: any) {
  return request.get('/fusion/results', { params })
}

export function getFusionResultDetailApi(id: number) {
  return request.get(`/fusion/results/${id}`)
}
