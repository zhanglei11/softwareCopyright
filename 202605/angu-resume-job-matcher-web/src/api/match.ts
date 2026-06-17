import request from '@/utils/request'

export const matchApi = {
  run: (positionId: number) => request.post('/api/match/run', { positionId }),
  getConfig: () => request.get('/api/match/config'),
  updateConfig: (data: any) => request.put('/api/match/config', data),
  results: (positionId: number) => request.get(`/api/match/results/${positionId}`),
}