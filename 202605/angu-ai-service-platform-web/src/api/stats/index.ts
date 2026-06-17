import request from '@/utils/request'
export const getDashboardApi = () => request.get('/api/stats/dashboard')
export const getSceneStatsApi = (params?: any) => request.get('/api/stats/scenes', { params })
export const getUserRankApi = (params?: any) => request.get('/api/stats/user-rank', { params })
