import request from '@/utils/request'
import type { DefectRecordQuery } from '@/types'

export const getDefectRecordList = (params: DefectRecordQuery) =>
  request.get('/api/v1/defect/records', { params })

export const getDefectRecordDetail = (id: number) =>
  request.get(`/api/v1/defect/records/${id}`)

export const disposeDefect = (id: number, data: { disposeRemark: string }) =>
  request.patch(`/api/v1/defect/records/${id}/dispose`, data)

export const getDefectImages = (id: number) =>
  request.get(`/api/v1/defect/records/${id}/images`)
