import request from '@/utils/request'
import type { SysRole, PageQuery } from '@/types'

export const getRoleList = (params?: Partial<SysRole> & PageQuery) =>
  request({ url: '/api/system/roles', method: 'get', params })

export const getRoleDetail = (id: number) =>
  request({ url: `/api/system/roles/${id}`, method: 'get' })

export const addRole = (data: SysRole) =>
  request({ url: '/api/system/roles', method: 'post', data })

export const updateRole = (id: number, data: SysRole) =>
  request({ url: `/api/system/roles/${id}`, method: 'put', data })

export const deleteRole = (id: number) =>
  request({ url: `/api/system/roles/${id}`, method: 'delete' })
