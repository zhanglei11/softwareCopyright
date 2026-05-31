import { computed, ref } from 'vue'
import { createPinia, defineStore } from 'pinia'
import { authApi } from '@/api/auth'
import { clearAuth, getMenus, getToken, getUserInfo, setMenus, setToken, setUserInfo } from '@/utils/common'
import { extractMenuPaths, extractPermissions, hasPermission } from '@/utils/permission'
import type { LoginRequest, MenuNode, UserProfile } from '@/types'

export const pinia = createPinia()

export const useAppStore = defineStore('app', () => {
  const token = ref(getToken())
  const userInfo = ref<UserProfile | null>(getUserInfo())
  const menuTree = ref<MenuNode[]>(getMenus())
  const initialized = ref(Boolean(token.value && userInfo.value && menuTree.value.length))
  const collapsed = ref(false)
  const permissions = computed(() => extractPermissions(menuTree.value))
  const menuPaths = computed(() => extractMenuPaths(menuTree.value))
  const homePath = computed(() => (menuPaths.value.includes('/stats/dashboard') ? '/stats/dashboard' : (menuPaths.value[0] || '/stats/dashboard')))

  function persist() {
    setToken(token.value)
    setUserInfo(userInfo.value)
    setMenus(menuTree.value)
  }

  async function login(payload: LoginRequest) {
    const response: any = await authApi.login(payload)
    token.value = response.data.accessToken
    persist()
    await bootstrap()
  }

  async function bootstrap() {
    const [meResponse, menuResponse]: any = await Promise.all([authApi.me(), authApi.myMenus()])
    userInfo.value = meResponse.data
    menuTree.value = menuResponse.data || []
    initialized.value = true
    persist()
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    menuTree.value = []
    initialized.value = false
    collapsed.value = false
    clearAuth()
  }

  function canAccess(path: string) {
    return menuPaths.value.includes(path)
  }

  function can(permission: string | string[]) {
    return hasPermission(permission, permissions.value)
  }

  return { token, userInfo, menuTree, initialized, collapsed, permissions, menuPaths, homePath, login, bootstrap, logout, canAccess, can }
})