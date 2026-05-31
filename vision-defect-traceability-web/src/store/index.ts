import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo } from '@/types'
import { getToken, setToken, removeToken, setUserInfo, getUserInfo, removeUserInfo } from '@/utils/common'

export const useAppStore = defineStore('app', () => {
  const token = ref<string | null>(getToken())
  const userInfo = ref<UserInfo | null>(getUserInfo())

  const setLoginInfo = (t: string, info: UserInfo) => {
    token.value = t
    userInfo.value = info
    setToken(t)
    setUserInfo(info)
  }

  const logout = () => {
    token.value = null
    userInfo.value = null
    removeToken()
    removeUserInfo()
  }

  const isLoggedIn = () => !!token.value

  return { token, userInfo, setLoginInfo, logout, isLoggedIn }
})
