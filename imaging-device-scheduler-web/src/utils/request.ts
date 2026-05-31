import axios from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { message } from 'ant-design-vue'
import { getToken } from './common'
import router from '@/router'

const service: AxiosInstance = axios.create({
  baseURL: '',
  timeout: 30000,
})

service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = getToken()
    if (token && config.headers) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    if (res.code === 200 || res.code === 0) {
      return res
    }
    if (res.code === 401) {
      message.error('登录已过期，请重新登录')
      router.push('/login')
      return Promise.reject(new Error(res.msg || '未授权'))
    }
    message.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  (error) => {
    const status = error.response?.status
    if (status === 401) {
      message.error('登录已过期，请重新登录')
      router.push('/login')
    } else if (status === 403) {
      message.error('无权限访问')
    } else if (status === 404) {
      message.error('接口不存在')
    } else {
      message.error(error.message || '网络异常')
    }
    return Promise.reject(error)
  },
)

export default service
