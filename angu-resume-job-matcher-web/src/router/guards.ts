import NProgress from 'nprogress'
import type { Router } from 'vue-router'
import { pinia, useAppStore } from '@/store'

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to) => {
    const store = useAppStore(pinia)
    NProgress.start()

    if (to.meta.public) {
      if (to.path === '/login' && store.token) {
        if (!store.initialized) {
          try {
            await store.bootstrap()
          } catch {
            store.logout()
            return true
          }
        }
        return store.homePath
      }
      return true
    }

    if (!store.token) {
      return { path: '/login', query: { redirect: to.fullPath } }
    }

    if (!store.initialized) {
      try {
        await store.bootstrap()
      } catch {
        store.logout()
        return { path: '/login', query: { redirect: to.fullPath } }
      }
    }

    if (!store.canAccess(to.path)) {
      return store.homePath || '/404'
    }

    return true
  })

  router.afterEach(() => {
    NProgress.done()
  })
}