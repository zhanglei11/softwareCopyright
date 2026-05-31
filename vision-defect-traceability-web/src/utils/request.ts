import axios from 'axios'
import router from '@/router'
import { message } from 'ant-design-vue'
import { getToken } from './common'
import { useAppStore } from '@/store'

const apiUrl = import.meta.env.VITE_API_URL || ''

const instance = axios.create({
  timeout: 30000,
  baseURL: apiUrl,
  headers: { 'Content-Type': 'application/json;charset=UTF-8' },
})

let isHandlingUnauthorized = false

const redirectToLogin = (msg?: string) => {
  if (isHandlingUnauthorized) return
  isHandlingUnauthorized = true
  const store = useAppStore()
  const redirect = router.currentRoute.value.fullPath
  store.logout()
  message.error(msg || '认证过期，请重新登录', 2)
  router.replace({ path: '/login', query: { redirect } }).finally(() => {
    setTimeout(() => { isHandlingUnauthorized = false }, 300)
  })
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
  (response) => {
    const { data, status } = response
    if (response.config?.responseType === 'blob') return data
    const { code, msg: resMsg } = data
    if (status === 200) {
      if (code === 200) {
        if ('rows' in data && !('data' in data)) {
          data.data = { rows: data.rows, total: data.total }
        }
        return data
      }
      if (code === 401) { redirectToLogin(resMsg); return Promise.reject(new Error(resMsg)) }
      message.error(resMsg || '请求失败', 2)
      return Promise.reject(new Error(resMsg))
    }
    return Promise.reject(new Error(resMsg || '请求失败'))
  },
  (error) => {
    const status = error?.response?.status
    if (status === 401) { redirectToLogin(); return Promise.reject(error) }
    if (status === 403) { message.error('无权限访问', 2); return Promise.reject(error) }
    message.error(error?.message || '网络请求失败', 2)
    return Promise.reject(error)
  }
)

export default instance
