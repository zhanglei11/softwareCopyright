import request from '@/utils/request'
import type { SysUser, PageQuery } from '@/types'

export const getUserList = (params?: Partial<SysUser> & PageQuery) =>
  request({ url: '/api/system/users', method: 'get', params })

export const getUserDetail = (id: number) =>
  request({ url: `/api/system/users/${id}`, method: 'get' })

export const addUser = (data: SysUser) =>
  request({ url: '/api/system/users', method: 'post', data })

export const updateUser = (id: number, data: SysUser) =>
  request({ url: `/api/system/users/${id}`, method: 'put', data })

export const updateUserStatus = (id: number, status: number) =>
  request({ url: `/api/system/users/${id}/status`, method: 'patch', data: { status } })

export const resetPassword = (id: number, data: { newPassword: string }) =>
  request({ url: `/api/system/users/${id}/reset-password`, method: 'put', data })

export const deleteUser = (id: number) =>
  request({ url: `/api/system/users/${id}`, method: 'delete' })
