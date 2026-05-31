import type { Router } from 'vue-router'
import NProgress from 'nprogress'
import { useAppStore } from '@/store'
import { getMenus } from '@/api/auth'

const WHITE_LIST = ['/login']

export const setupGuards = (router: Router) => {
  router.beforeEach(async (to, _from, next) => {
    NProgress.start()
    const store = useAppStore()

    if (WHITE_LIST.includes(to.path)) {
      if (store.isLoggedIn && to.path === '/login') return next('/')
      return next()
    }

    if (!store.isLoggedIn) {
      return next({ path: '/login', query: { redirect: to.fullPath } })
    }

    // 首次加载菜单
    if (store.menus.length === 0) {
      try {
        const res = await getMenus()
        store.setMenus(res.data ?? [])
      } catch {
        store.logout()
        return next('/login')
      }
    }

    next()
  })

  router.afterEach(() => {
    NProgress.done()
  })
}
