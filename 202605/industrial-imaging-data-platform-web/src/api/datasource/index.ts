import request from '@/utils/request'
import type { DatasourceConfig, PageQuery } from '@/types'

export const getDatasourceList = (params?: Partial<DatasourceConfig> & PageQuery) =>
  request({ url: '/api/datasource', method: 'get', params })

export const getDatasourceDetail = (id: number) =>
  request({ url: `/api/datasource/${id}`, method: 'get' })

export const addDatasource = (data: DatasourceConfig) =>
  request({ url: '/api/datasource', method: 'post', data })

export const updateDatasource = (id: number, data: DatasourceConfig) =>
  request({ url: `/api/datasource/${id}`, method: 'put', data })

export const updateDatasourceStatus = (id: number, status: number) =>
  request({ url: `/api/datasource/${id}/status`, method: 'patch', data: { status } })

export const deleteDatasource = (id: number) =>
  request({ url: `/api/datasource/${id}`, method: 'delete' })

export const testDatasourceConn = (id: number) =>
  request({ url: `/api/datasource/${id}/test`, method: 'post' })
