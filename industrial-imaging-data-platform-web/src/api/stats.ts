import request from '@/utils/request'

export const getOverview = () =>
  request({ url: '/api/stats/overview', method: 'get' })

export const getIngestTrend = (params?: { days?: number }) =>
  request({ url: '/api/stats/ingest/trend', method: 'get', params })

export const getProcessSummary = () =>
  request({ url: '/api/stats/process/summary', method: 'get' })

export const getIngestAnalysis = () =>
  request({ url: '/api/stats/ingest/analysis', method: 'get' })

export const getDatasourceContribution = () =>
  request({ url: '/api/stats/datasource/contribution', method: 'get' })

export const getFileTypeDistribution = () =>
  request({ url: '/api/stats/filetype/distribution', method: 'get' })
