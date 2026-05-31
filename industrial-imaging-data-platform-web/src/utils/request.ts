import axios from 'axios'
import { message } from 'ant-design-vue'
import { getToken } from './common'

const instance = axios.create({
  timeout: 30000,
  baseURL: '',
  headers: { 'Content-Type': 'application/json;charset=UTF-8' },
})

let isHandlingUnauthorized = false

const redirectToLogin = (msg?: string) => {
  if (isHandlingUnauthorized) return
  isHandlingUnauthorized = true
  message.error(msg || '认证过期，请重新登录', 2)
  setTimeout(() => {
    window.location.href = '/#/login'
    isHandlingUnauthorized = false
  }, 300)
}

instance.interceptors.request.use((config) => {
  if (typeof config.url === 'string' && config.url && !/^https?:\/\//i.test(config.url) && !config.url.startsWith('/')) {
    config.url = `/${config.url}`
  }
  const token = getToken()
  if (token) config.headers.Authorization = 'Bearer ' + token
  return config
})

instance.interceptors.response.use(
  async (response) => {
    const { data, status } = response
    if (response.config?.responseType === 'blob') {
      const ct = String(response.headers?.['content-type'] || '')
      if (data instanceof Blob && ct.includes('application/json')) {
        try {
          const text = await data.text()
          const json = JSON.parse(text)
          const code = Number(json?.code)
          const msg = json?.msg || '导出失败'
          if (code === 401) { redirectToLogin(msg); return Promise.reject(new Error(msg)) }
          if (code !== 200) { message.error(msg, 2); return Promise.reject(new Error(msg)) }
        } catch {
          message.error('导出失败：响应解析异常', 2)
          return Promise.reject(new Error('导出失败'))
        }
      }
      return data
    }
    const { code, msg } = data
    if (status === 200) {
      if (code === 200) {
        if ('rows' in data && !('data' in data)) {
          data.data = { rows: data.rows, total: data.total }
        }
        return data
      } else if (Number(code) === 401) {
        redirectToLogin(msg)
        return Promise.reject(new Error(msg))
      } else {
        message.error(msg || '请求失败', 2)
        return Promise.reject(new Error(msg))
      }
    }
    return data
  },
  (error: unknown) => {
    if (axios.isAxiosError(error)) {
      const status = error.response?.status
      if (status === 401) { redirectToLogin(); return Promise.reject(error) }
      if (status === 403) { message.error('无操作权限', 2); return Promise.reject(error) }
      if (status === 500) { message.error('服务器内部错误', 2); return Promise.reject(error) }
      if (!error.response) { message.error('网络连接失败，请检查网络', 2) }
    }
    return Promise.reject(error)
  }
)

export default instance
