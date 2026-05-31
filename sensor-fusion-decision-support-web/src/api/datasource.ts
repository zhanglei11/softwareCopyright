import request from '@/utils/request'

export function getDatasourceListApi(params?: any) {
  return request.get('/datasource', { params })
}

export function getDatasourceDetailApi(id: number) {
  return request.get(`/datasource/${id}`)
}

export function addDatasourceApi(data: any) {
  return request.post('/datasource', data)
}

export function editDatasourceApi(id: number, data: any) {
  return request.put(`/datasource/${id}`, data)
}

export function updateDatasourceStatusApi(id: number, status: number) {
  return request.put(`/datasource/${id}/status`, null, { params: { status } })
}

export function testConnApi(id: number) {
  return request.post(`/datasource/${id}/test-conn`)
}

export function getDatasourceStatusOverviewApi() {
  return request.get('/datasource/status-overview')
}
