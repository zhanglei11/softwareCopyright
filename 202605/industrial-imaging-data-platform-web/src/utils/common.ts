const TOKEN_KEY = 'iidp_access_token'
const REFRESH_KEY = 'iidp_refresh_token'
const USER_INFO_KEY = 'iidp_user_info'

export const getToken = (): string | null => localStorage.getItem(TOKEN_KEY)
export const setToken = (token: string) => localStorage.setItem(TOKEN_KEY, token)
export const removeToken = () => localStorage.removeItem(TOKEN_KEY)

export const getRefreshToken = (): string | null => localStorage.getItem(REFRESH_KEY)
export const setRefreshToken = (token: string) => localStorage.setItem(REFRESH_KEY, token)
export const removeRefreshToken = () => localStorage.removeItem(REFRESH_KEY)

export const getUserInfo = () => {
  const raw = localStorage.getItem(USER_INFO_KEY)
  return raw ? JSON.parse(raw) : null
}
export const setUserInfo = (info: unknown) => localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
export const removeUserInfo = () => localStorage.removeItem(USER_INFO_KEY)

export const clearAuth = () => {
  removeToken(); removeRefreshToken(); removeUserInfo()
}

export const downloadBlob = (blob: Blob, filename: string) => {
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url; a.download = filename
  document.body.appendChild(a); a.click()
  document.body.removeChild(a); URL.revokeObjectURL(url)
}

export const formatFileSize = (bytes?: number): string => {
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}
