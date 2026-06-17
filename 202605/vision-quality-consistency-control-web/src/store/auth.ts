import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null as any,
  }),
  getters: {
    isLoggedIn: (s) => !!s.token,
  },
  actions: {
    async login(username: string, password: string) {
      const res: any = await authApi.login({ username, password })
      this.token = res.data.accessToken
      localStorage.setItem('token', this.token)
    },
    async fetchUserInfo() {
      const res: any = await authApi.userinfo()
      this.userInfo = res.data
    },
    logout() {
      authApi.logout().catch(() => {})
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    },
  },
})
