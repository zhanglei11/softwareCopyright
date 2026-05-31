import request from '@/utils/request'
import type { StorageCleanRule, PageQuery } from '@/types'

export const getStorageOverview = () =>
  request({ url: '/api/storage/overview', method: 'get' })

export const getCleanRuleList = (params?: Partial<StorageCleanRule> & PageQuery) =>
  request({ url: '/api/storage/clean-rules', method: 'get', params })

export const addCleanRule = (data: StorageCleanRule) =>
  request({ url: '/api/storage/clean-rules', method: 'post', data })

export const updateCleanRule = (id: number, data: StorageCleanRule) =>
  request({ url: `/api/storage/clean-rules/${id}`, method: 'put', data })

export const updateCleanRuleStatus = (id: number, status: number) =>
  request({ url: `/api/storage/clean-rules/${id}/status`, method: 'patch', data: { status } })

export const deleteCleanRule = (id: number) =>
  request({ url: `/api/storage/clean-rules/${id}`, method: 'delete' })

export const executeClean = (id: number) =>
  request({ url: `/api/storage/clean-rules/${id}/execute`, method: 'post' })

export const getCleanLogs = (params?: { ruleId?: number } & PageQuery) =>
  request({ url: '/api/storage/clean-logs', method: 'get', params })
