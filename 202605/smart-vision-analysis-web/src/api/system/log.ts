import request from '@/utils/request'
export const getOperationLogsApi = (p?: object) => request.get('/api/system/logs', { params: p })
