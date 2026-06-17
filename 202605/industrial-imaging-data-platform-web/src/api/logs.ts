import request from '@/utils/request'
import type { PageQuery } from '@/types'

export const getOperLogList = (params?: PageQuery) =>
  request({ url: '/api/logs/operation', method: 'get', params })
