import axios from 'axios'
import { message } from 'ant-design-vue'
import { clearAuth, getToken } from './common'

const request = axios.create({
  baseURL: import.meta.env.DEV ? '' : (import.meta.env.VITE_API_URL || 'http://127.0.0.1:19915'),
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
})

let redirecting = false

function goLogin(msg?: string) {
  if (redirecting) return
  redirecting = true
  clearAuth()
  if (msg) message.error(msg, 2)
  const redirect = window.location.hash.replace('#', '') || '/'
  window.location.hash = `/login?redirect=${encodeURIComponent(redirect)}`
  window.setTimeout(() => {
    redirecting = false
  }, 400)
}

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers = config.headers ?? {}
    ;(config.headers as any).Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (payload?.code === 200) return payload
    if (payload?.code === 401) {
      goLogin(payload?.message || '登录已过期，请重新登录')
      return Promise.reject(new Error(payload?.message || '未登录'))
    }
    if (payload?.message) message.error(payload.message)
    return Promise.reject(new Error(payload?.message || '请求失败'))
  },
  (error) => {
    const status = error.response?.status
    if (status === 401) goLogin('登录已过期，请重新登录')
    else if (status === 403) message.error('无权限执行此操作')
    else if (status === 500) message.error('系统异常，请联系管理员')
    else if (!error.response) message.error('网络连接失败')
    return Promise.reject(error)
  },
)

export default request