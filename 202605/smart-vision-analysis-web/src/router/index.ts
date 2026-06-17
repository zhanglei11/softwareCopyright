import { createRouter, createWebHashHistory } from 'vue-router'
import { routes } from './routes'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/common'

NProgress.configure({ showSpinner: false })

const router = createRouter({ history: createWebHashHistory(), routes, scrollBehavior: () => ({ top: 0 }) })

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  const token = getToken()
  const requiresAuth = to.meta.requiresAuth !== false
  if (requiresAuth && !token) { next({ path: '/login', query: { redirect: to.fullPath } }); return }
  if (to.path === '/login' && token) { next('/'); return }
  next()
})
router.afterEach(() => NProgress.done())

export default router
