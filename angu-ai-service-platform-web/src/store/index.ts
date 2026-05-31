import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken } from '@/utils/common'
import { loginApi, getMeApi } from '@/api/auth'
import type { UserInfo } from '@/types'

export const useAppStore = defineStore('app', () => {
  const token = ref<string>(getToken())
  const userInfo = ref<UserInfo | null>(null)
  const permissions = ref<string[]>([])
  const roles = ref<string[]>([])
  const menuRoutes = ref<any[]>([])

  const setLoginInfo = (data: { accessToken: string; userInfo: UserInfo; permissions: string[]; roles: string[] }) => {
    token.value = data.accessToken
    userInfo.value = data.userInfo
    permissions.value = data.permissions || []
    roles.value = data.roles || []
    setToken(data.accessToken)
  }

  const login = async (username: string, password: string) => {
    const loginRes = await loginApi({ username, password })
    const accessToken = loginRes.data?.accessToken || loginRes.data?.access_token
    if (!accessToken) throw new Error('登录失败，未获取到 token')
    setToken(accessToken)
    token.value = accessToken
    const meRes = await getMeApi()
    const me = meRes.data || {}
    userInfo.value = me.userInfo || me
    roles.value = me.roles || []
    permissions.value = me.permissions || []
  }

  const hasPermission = (perm: string) => {
    if (roles.value.includes('SUPER_ADMIN')) return true
    return permissions.value.includes(perm)
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    permissions.value = []
    roles.value = []
    removeToken()
  }

  return { token, userInfo, permissions, roles, menuRoutes, setLoginInfo, hasPermission, logout, login }
})
