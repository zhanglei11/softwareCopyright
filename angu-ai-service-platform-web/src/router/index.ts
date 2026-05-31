import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/common'
import { getMeApi } from '@/api/auth'
import { useAppStore } from '@/store'

NProgress.configure({ showSpinner: false })

const router = createRouter({
  history: createWebHistory(),
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
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '使用量看板' } },
        { path: 'system/user', name: 'SysUser', component: () => import('@/views/system/user/index.vue'), meta: { title: '用户管理' } },
        { path: 'system/role', name: 'SysRole', component: () => import('@/views/system/role/index.vue'), meta: { title: '角色管理' } },
        { path: 'system/menu', name: 'SysMenu', component: () => import('@/views/system/menu/index.vue'), meta: { title: '菜单管理' } },
        { path: 'ai/category', name: 'AiCategory', component: () => import('@/views/ai/category/index.vue'), meta: { title: '场景分类' } },
        { path: 'ai/scene', name: 'AiScene', component: () => import('@/views/ai/scene/index.vue'), meta: { title: '场景配置' } },
        { path: 'ai/scene/:id', name: 'AiSceneDetail', component: () => import('@/views/ai/scene/detail.vue'), meta: { title: '场景详情' } },
        { path: 'ai/model', name: 'AiModel', component: () => import('@/views/ai/model/index.vue'), meta: { title: 'AI模型配置' } },
        { path: 'app/square', name: 'AppSquare', component: () => import('@/views/app/square/index.vue'), meta: { title: '场景广场' } },
        { path: 'app/chat', name: 'AppChat', component: () => import('@/views/app/chat/index.vue'), meta: { title: '智能对话' } },
        { path: 'app/doc', name: 'AppDoc', component: () => import('@/views/app/doc/index.vue'), meta: { title: '文档分析' } },
        { path: 'app/generate', name: 'AppGenerate', component: () => import('@/views/app/generate/index.vue'), meta: { title: '内容生成' } },
        { path: 'kb/base', name: 'KbBase', component: () => import('@/views/kb/base/index.vue'), meta: { title: '知识库列表' } },
        { path: 'kb/docs/:id', name: 'KbDocs', component: () => import('@/views/kb/docs/index.vue'), meta: { title: '知识库文档' } },
        { path: 'conversation', name: 'Conversation', component: () => import('@/views/conversation/index.vue'), meta: { title: '历史会话' } },
        { path: 'stats', name: 'Stats', component: () => import('@/views/stats/index.vue'), meta: { title: '场景统计' } },
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
  } else {
    if (!token) return next({ path: '/login', query: { redirect: to.fullPath } })
    // 刷新后 Pinia store 中 roles/permissions 为空时，重新从接口加载
    const store = useAppStore()
    if (!store.userInfo) {
      try {
        const meRes = await getMeApi()
        const me = meRes.data || {}
        store.userInfo = me.userInfo || me
        store.roles = me.roles || []
        store.permissions = me.permissions || []
      } catch {
        // token 失效，跳转登录
        store.logout()
        return next({ path: '/login', query: { redirect: to.fullPath } })
      }
    }
    next()
  }
})

router.afterEach((to) => {
  NProgress.done()
  document.title = `${to.meta.title || '安谷AI'} - 安谷AI多场景服务平台`
})

export default router
