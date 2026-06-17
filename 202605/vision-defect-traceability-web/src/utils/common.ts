const TOKEN_KEY = 'vision_token'
const USER_KEY = 'vision_user'

export const getToken = (): string | null => localStorage.getItem(TOKEN_KEY)
export const setToken = (token: string) => localStorage.setItem(TOKEN_KEY, token)
export const removeToken = () => localStorage.removeItem(TOKEN_KEY)

export const getUserInfo = () => {
  const str = localStorage.getItem(USER_KEY)
  if (!str) return null
  try { return JSON.parse(str) } catch { return null }
}
export const setUserInfo = (info: unknown) => localStorage.setItem(USER_KEY, JSON.stringify(info))
export const removeUserInfo = () => localStorage.removeItem(USER_KEY)
