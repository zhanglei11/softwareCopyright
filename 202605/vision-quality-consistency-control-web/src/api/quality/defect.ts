import http from '../http'

export const defectApi = {
  list: (params?: any) => http.get('/v1/quality/defects', { params }),
  get: (id: number) => http.get(`/v1/quality/defects/${id}`),
  history: (id: number) => http.get(`/v1/quality/defects/${id}/history`),
  dispose: (data: any) => http.post('/v1/quality/defects/dispose', data),
  ignore: (data: any) => http.post('/v1/quality/defects/ignore', data),
  verify: (data: any) => http.post('/v1/quality/defects/verify', data),
}
