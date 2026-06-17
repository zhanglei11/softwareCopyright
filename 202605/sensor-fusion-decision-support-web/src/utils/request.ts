import axios, { type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { message } from 'ant-design-vue'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('access_token')
  if (token) config.headers['Authorization'] = `Bearer ${token}`
  return config
})

request.interceptors.response.use(
  (res: AxiosResponse) => {
    const data = res.data
    if (data.code === 200) return data
    if (data.code === 401) {
      localStorage.removeItem('access_token')
      router.push('/login')
      return Promise.reject(new Error(data.message))
    }
    message.error(data.message || '请求失败')
    return Promise.reject(new Error(data.message))
  },
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('access_token')
      router.push('/login')
    } else {
      message.error(err.response?.data?.message || '网络异常')
    }
    return Promise.reject(err)
  }
)

export default request
