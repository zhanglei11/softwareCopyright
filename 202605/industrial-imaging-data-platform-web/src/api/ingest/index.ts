import request from '@/utils/request'
import type { IngestTask, PageQuery } from '@/types'

export const getIngestTaskList = (params?: Partial<IngestTask> & PageQuery) =>
  request({ url: '/api/ingest/tasks', method: 'get', params })

export const getIngestTaskDetail = (id: number) =>
  request({ url: `/api/ingest/tasks/${id}`, method: 'get' })

export const addIngestTask = (data: IngestTask) =>
  request({ url: '/api/ingest/tasks', method: 'post', data })

export const updateIngestTask = (id: number, data: IngestTask) =>
  request({ url: `/api/ingest/tasks/${id}`, method: 'put', data })

export const updateIngestTaskStatus = (id: number, status: number) =>
  request({ url: `/api/ingest/tasks/${id}/status`, method: 'patch', data: { status } })

export const triggerIngestTask = (id: number) =>
  request({ url: `/api/ingest/tasks/${id}/trigger`, method: 'post' })

export const deleteIngestTask = (id: number) =>
  request({ url: `/api/ingest/tasks/${id}`, method: 'delete' })

export const getIngestRecords = (params?: { taskId?: number } & PageQuery) =>
  request({ url: '/api/ingest/records', method: 'get', params })
