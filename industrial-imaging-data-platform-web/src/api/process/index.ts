import request from '@/utils/request'
import type { ProcessTask, PageQuery } from '@/types'

export const getProcessTaskList = (params?: Partial<ProcessTask> & PageQuery) =>
  request({ url: '/api/process/tasks', method: 'get', params })

export const getProcessTaskDetail = (id: number) =>
  request({ url: `/api/process/tasks/${id}`, method: 'get' })

export const addProcessTask = (data: ProcessTask) =>
  request({ url: '/api/process/tasks', method: 'post', data })

export const updateProcessTask = (id: number, data: ProcessTask) =>
  request({ url: `/api/process/tasks/${id}`, method: 'put', data })

export const updateProcessTaskStatus = (id: number, status: number) =>
  request({ url: `/api/process/tasks/${id}/status`, method: 'patch', data: { status } })

export const triggerProcessTask = (id: number) =>
  request({ url: `/api/process/tasks/${id}/trigger`, method: 'post' })

export const terminateProcessTask = (id: number) =>
  request({ url: `/api/process/tasks/${id}/terminate`, method: 'post' })

export const getRunningExecution = (id: number) =>
  request({ url: `/api/process/tasks/${id}/running`, method: 'get' })

export const deleteProcessTask = (id: number) =>
  request({ url: `/api/process/tasks/${id}`, method: 'delete' })

export const getProcessExecutions = (params?: PageQuery) =>
  request({ url: '/api/process/executions', method: 'get', params })
