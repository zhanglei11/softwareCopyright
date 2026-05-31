import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, clearAuth, getUserInfo, setUserInfo } from '@/utils/common'
import type { UserInfo } from '@/types'
import { loginApi, getUserInfoApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const userInfo = ref<UserInfo | null>(getUserInfo())
  const roles = ref<string[]>(getUserInfo()?.roles || [])
  const permissions = ref<string[]>(getUserInfo()?.permissions || [])

  async function login(username: string, password: string) {
    const res = await loginApi({ username, password })
    const tk = res.data?.accessToken || res.data?.token || res.data?.access_token || String(res.data)
    token.value = tk; setToken(tk)
    await fetchUserInfo()
  }

  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    const info: UserInfo = res.data || res
    userInfo.value = info; roles.value = info.roles || []; permissions.value = info.permissions || []
    setUserInfo(info); return info
  }

  function logout() { token.value = ''; userInfo.value = null; roles.value = []; permissions.value = []; clearAuth() }

  return { token, userInfo, roles, permissions, login, fetchUserInfo, logout }
})
