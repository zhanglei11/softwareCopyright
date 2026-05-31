import request from '@/utils/request'

export const traceBatch = (params: { batchNo: string; startDate?: string; endDate?: string }) =>
  request.get('/api/v1/trace/batch', { params })

export const traceProduct = (serialNo: string) =>
  request.get('/api/v1/trace/product', { params: { serialNo } })
