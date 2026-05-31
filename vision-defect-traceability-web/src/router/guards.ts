import type { Router } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { useAppStore } from '@/store'

NProgress.configure({ showSpinner: false })

export const setupGuards = (router: Router) => {
  router.beforeEach((to, _from, next) => {
    NProgress.start()
    const store = useAppStore()
    if (to.meta?.requiresAuth && !store.isLoggedIn()) {
      next({ path: '/login', query: { redirect: to.fullPath } })
    } else if (to.path === '/login' && store.isLoggedIn()) {
      next('/')
    } else {
      next()
    }
  })
  router.afterEach(() => NProgress.done())
}
