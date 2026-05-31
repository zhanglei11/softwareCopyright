import { defineStore } from 'pinia'
import { removeToken, setToken, setRefreshToken } from '@/utils/common'
import router from '@/router'

interface UserInfo {
  id: number
  username: string
  realName: string
  roles: string[]
}

export const useAppStore = defineStore('app', {
  state: () => ({
    userInfo: null as UserInfo | null,
    roles: [] as string[],
    collapsed: false,
  }),
  getters: {
    isAdmin: (state) => state.roles.includes('admin'),
  },
  actions: {
    setUser(info: UserInfo) {
      this.userInfo = info
      this.roles = info.roles || []
    },
    setTokens(accessToken: string, refreshToken: string) {
      setToken(accessToken)
      setRefreshToken(refreshToken)
    },
    logout() {
      this.userInfo = null
      this.roles = []
      removeToken()
      router.push('/login')
    },
    toggleCollapsed() {
      this.collapsed = !this.collapsed
    },
  },
})
