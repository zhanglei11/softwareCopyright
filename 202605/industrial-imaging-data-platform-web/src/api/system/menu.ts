import request from '@/utils/request'
import type { SysMenu } from '@/types'

export const getMenuTree = () =>
  request({ url: '/api/system/menus', method: 'get' })

export const addMenu = (data: SysMenu) =>
  request({ url: '/api/system/menus', method: 'post', data })

export const updateMenu = (id: number, data: SysMenu) =>
  request({ url: `/api/system/menus/${id}`, method: 'put', data })

export const deleteMenu = (id: number) =>
  request({ url: `/api/system/menus/${id}`, method: 'delete' })
