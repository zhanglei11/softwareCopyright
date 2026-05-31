export interface PageData<T = any> {
  total: number
  pages: number
  list: T[]
}

export interface LoginRequest {
  username: string
  password: string
}

export interface UserProfile {
  id: number
  username: string
  realName: string
  phone: string
  status: number
  createdTime?: string
  updatedTime?: string
}

export interface MenuNode {
  id: number
  parentId: number
  menuType: number
  menuName: string
  path?: string | null
  permCode?: string | null
  icon?: string | null
  sort?: number
  children?: MenuNode[]
}