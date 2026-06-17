import request from '@/utils/request'
import type { ApiResponse, DashboardVO } from '@/types'

export const getDashboard = (date?: string) =>
  request.get<ApiResponse<DashboardVO>>('/api/v1/stats/dashboard', { params: { date } })

export const getTrend = (params: { startDate: string; endDate: string; lineId?: number }) =>
  request.get('/api/v1/stats/trend', { params })
