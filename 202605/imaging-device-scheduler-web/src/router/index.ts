import { createRouter, createWebHashHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/common'
import { useAppStore } from '@/store'

NProgress.configure({ showSpinner: false })

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index.vue'),
      meta: { title: '登录', public: true },
    },
    {
      path: '/',
      component: () => import('@/layout/BasicLayout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          meta: { title: '调度总览' },
        },
        {
          path: 'device',
          name: 'Device',
          component: () => import('@/views/device/index.vue'),
          meta: { title: '设备管理' },
        },
        {
          path: 'scene/group',
          name: 'SceneGroup',
          component: () => import('@/views/scene/group.vue'),
          meta: { title: '场景分组' },
        },
        {
          path: 'scene/list',
          name: 'Scene',
          component: () => import('@/views/scene/index.vue'),
          meta: { title: '场景管理' },
        },
        {
          path: 'task',
          name: 'Task',
          component: () => import('@/views/task/index.vue'),
          meta: { title: '任务管理' },
        },
        {
          path: 'dispatch',
          name: 'Dispatch',
          component: () => import('@/views/dispatch/index.vue'),
          meta: { title: '调度管理' },
        },
        {
          path: 'stats',
          name: 'Stats',
          component: () => import('@/views/stats/index.vue'),
          meta: { title: '统计分析' },
        },
        {
          path: 'system/user',
          name: 'SysUser',
          component: () => import('@/views/system/user/index.vue'),
          meta: { title: '用户管理' },
        },
        {
          path: 'system/role',
          name: 'SysRole',
          component: () => import('@/views/system/role/index.vue'),
          meta: { title: '角色管理' },
        },
        {
          path: 'system/menu',
          name: 'SysMenu',
          component: () => import('@/views/system/menu/index.vue'),
          meta: { title: '菜单管理' },
        },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

const WHITE_LIST = ['/login']

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  const token = getToken()
  if (WHITE_LIST.includes(to.path)) {
    token ? next('/') : next()
    return
  }
  if (!token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  const store = useAppStore()
  if (!store.userInfo) {
    const saved = localStorage.getItem('ids_user')
    if (saved) {
      try {
        store.setUser(JSON.parse(saved))
      } catch {
        store.logout()
        return
      }
    }
  }
  next()
})

router.afterEach((to) => {
  NProgress.done()
  document.title = `${to.meta.title || ''} - 多场景影像传感设备资源协同调度系统平台`
})

export default router
