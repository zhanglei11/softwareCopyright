import type { MenuNode, PageData, UserProfile } from '@/types'

const TOKEN_KEY = 'angu_resume_job_matcher_token'
const USER_KEY = 'angu_resume_job_matcher_user'
const MENU_KEY = 'angu_resume_job_matcher_menu_tree'

function parseJson<T>(key: string, fallback: T): T {
  const raw = localStorage.getItem(key)
  if (!raw) return fallback
  try {
    return JSON.parse(raw) as T
  } catch {
    return fallback
  }
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function getUserInfo() {
  return parseJson<UserProfile | null>(USER_KEY, null)
}

export function setUserInfo(user: UserProfile | null) {
  if (!user) {
    localStorage.removeItem(USER_KEY)
    return
  }
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function getMenus() {
  return parseJson<MenuNode[]>(MENU_KEY, [])
}

export function setMenus(menus: MenuNode[]) {
  localStorage.setItem(MENU_KEY, JSON.stringify(menus))
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  localStorage.removeItem(MENU_KEY)
}

export function pickPage<T = any>(payload: any): PageData<T> {
  const data = payload?.data ?? payload ?? {}
  if (Array.isArray(data)) {
    return { total: data.length, pages: 1, list: data }
  }
  return {
    total: Number(data.total ?? 0),
    pages: Number(data.pages ?? 1),
    list: Array.isArray(data.list) ? data.list : [],
  }
}

export function pickList<T = any>(payload: any): T[] {
  const data = payload?.data ?? payload
  if (Array.isArray(data)) return data
  if (Array.isArray(data?.list)) return data.list
  return []
}

export function labelizeSource(source?: string) {
  const mapping: Record<string, string> = {
    MANUAL: '手动录入',
    FILE: '文件上传',
    THIRD_PARTY: '第三方',
  }
  return mapping[source || ''] || source || '-'
}