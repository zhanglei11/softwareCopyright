import type { VNode } from 'vue'

export interface PaginationParams {
  pageNum?: number
  pageSize?: number
}

export interface RouteMeta {
  title?: string
  icon?: string
  requiresAuth?: boolean
  permission?: string
  permissions?: string[]
  hidden?: boolean
}

export interface MenuItemData {
  key: string
  label: string
  icon?: () => VNode
  children?: MenuItemData[]
}

export interface UserInfo {
  id?: string | number
  username?: string
  realName?: string
  avatar?: string
  phone?: string
  email?: string
  roles?: string[]
  permissions?: string[]
}

export interface LoginParams {
  username: string
  password: string
}

export interface PageResult<T> {
  rows: T[]
  total: number
}
