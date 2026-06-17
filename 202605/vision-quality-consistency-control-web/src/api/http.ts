import axios from 'axios'
import { message } from 'ant-design-vue'
import router from '@/router'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

http.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

http.interceptors.response.use(
  res => {
    const data = res.data
    if (data.code !== undefined && data.code !== 200) {
      message.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message))
    }
    return data
  },
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    } else {
      message.error(err.response?.data?.message || '网络异常')
    }
    return Promise.reject(err)
  }
)

export default http
