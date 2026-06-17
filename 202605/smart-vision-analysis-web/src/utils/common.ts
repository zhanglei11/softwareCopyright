const TOKEN_KEY = 'SVA_TOKEN'
const USER_KEY = 'SVA_USER'
export const getToken = () => localStorage.getItem(TOKEN_KEY) || ''
export const setToken = (t: string) => localStorage.setItem(TOKEN_KEY, t)
export const removeToken = () => localStorage.removeItem(TOKEN_KEY)
export const getUserInfo = () => { try { const r = localStorage.getItem(USER_KEY); return r ? JSON.parse(r) : null } catch { return null } }
export const setUserInfo = (u: object) => localStorage.setItem(USER_KEY, JSON.stringify(u))
export const clearAuth = () => { removeToken(); localStorage.removeItem(USER_KEY) }
