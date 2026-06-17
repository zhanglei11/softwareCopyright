import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: () => import('@/views/login/index.vue') },
    {
      path: '/',
      component: () => import('@/layout/BasicLayout.vue'),
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '质量看板' } },
        { path: 'system/user', component: () => import('@/views/system/user/index.vue'), meta: { title: '用户管理' } },
        { path: 'system/role', component: () => import('@/views/system/role/index.vue'), meta: { title: '角色管理' } },
        { path: 'system/menu', component: () => import('@/views/system/menu/index.vue'), meta: { title: '菜单管理' } },
        { path: 'quality/metric', component: () => import('@/views/quality/metric/index.vue'), meta: { title: '质量指标' } },
        { path: 'quality/template', component: () => import('@/views/quality/template/index.vue'), meta: { title: '标准模板' } },
        { path: 'quality/task', component: () => import('@/views/quality/task/index.vue'), meta: { title: '检测任务' } },
        { path: 'quality/analysis', component: () => import('@/views/quality/analysis/index.vue'), meta: { title: '一致性分析' } },
        { path: 'defect', component: () => import('@/views/defect/index.vue'), meta: { title: '不合格品管理' } },
        { path: 'stats', component: () => import('@/views/stats/index.vue'), meta: { title: '趋势报表' } },
        { path: 'agent', component: () => import('@/views/agent/index.vue'), meta: { title: '智能体管理' } },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) return '/login'
})

export default router
