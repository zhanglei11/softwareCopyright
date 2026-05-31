import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getToken, setToken, setRefreshToken, getUserInfo, setUserInfo, clearAuth } from '@/utils/common'
import type { UserInfo, SysMenu } from '@/types'

export const useAppStore = defineStore('app', () => {
  const token = ref<string | null>(getToken())
  const userInfo = ref<UserInfo | null>(getUserInfo())
  const menus = ref<SysMenu[]>([])
  const collapsed = ref(false)

  const isLoggedIn = computed(() => !!token.value)
  const permissions = computed(() => userInfo.value?.roles ?? [])
  const username = computed(() => userInfo.value?.username ?? '')

  const setAuth = (accessToken: string, refreshToken: string, info: UserInfo) => {
    token.value = accessToken
    setToken(accessToken)
    setRefreshToken(refreshToken)
    userInfo.value = info
    setUserInfo(info)
  }

  const setMenus = (menuList: SysMenu[]) => {
    menus.value = menuList
  }

  const logout = () => {
    token.value = null
    userInfo.value = null
    menus.value = []
    clearAuth()
  }

  const toggleCollapsed = () => {
    collapsed.value = !collapsed.value
  }

  return {
    token, userInfo, menus, collapsed,
    isLoggedIn, permissions, username,
    setAuth, setMenus, logout, toggleCollapsed
  }
})
