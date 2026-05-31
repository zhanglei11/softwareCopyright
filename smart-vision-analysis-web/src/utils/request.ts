import axios from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { message } from 'ant-design-vue'
import { getToken } from './common'
import router from '@/router'

const service: AxiosInstance = axios.create({ baseURL: import.meta.env.VITE_API_URL || '', timeout: 30000 })

service.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = getToken()
  if (token && config.headers) config.headers['Authorization'] = `Bearer ${token}`
  return config
}, (e) => Promise.reject(e))

service.interceptors.response.use((res: AxiosResponse) => {
  const d = res.data
  if (d.code === 200 || d.code === 0) {
    // Normalize TableDataInfo paginated responses: {records, total, ...} → {data: {rows, list, total}}
    if (d.records !== undefined) {
      return { data: { rows: d.records, list: d.records, total: d.total ?? 0 }, code: 200 }
    }
    return d
  }
  if (d.code === 401) { message.error('登录已过期'); router.push('/login'); return Promise.reject(new Error(d.message)) }
  message.error(d.message || '请求失败')
  return Promise.reject(new Error(d.message))
}, (e) => {
  const s = e.response?.status
  if (s === 401) { message.error('登录已过期'); router.push('/login') }
  else if (s === 403) message.error('无权限访问')
  else message.error(e.message || '网络异常')
  return Promise.reject(e)
})

export default service
