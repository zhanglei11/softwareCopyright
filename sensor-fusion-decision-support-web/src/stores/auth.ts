import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

export interface UserInfo {
  id: number
  username: string
  realName: string
  roles: string[]
  permissions: string[]
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('access_token') || '')
  const userInfo = ref<UserInfo | null>(null)

  async function login(username: string, password: string) {
    const res: any = await request.post('/auth/login', { username, password })
    token.value = res.data.accessToken
    localStorage.setItem('access_token', res.data.accessToken)
  }

  async function fetchUserInfo() {
    const res: any = await request.get('/auth/info')
    userInfo.value = res.data
  }

  function logout() {
    request.post('/auth/logout').catch(() => {})
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('access_token')
  }

  function hasPermission(perm: string) {
    return userInfo.value?.permissions?.includes(perm) ?? false
  }

  return { token, userInfo, login, fetchUserInfo, logout, hasPermission }
})
