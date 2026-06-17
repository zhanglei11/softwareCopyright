import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/login/LoginView.vue'), meta: { public: true } },
    {
      path: '/',
      component: () => import('@/layouts/BasicLayout.vue'),
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: '首页', icon: 'HomeOutlined' } },
        { path: 'datasource', name: 'Datasource', component: () => import('@/views/datasource/DatasourceView.vue'), meta: { title: '数据源管理', icon: 'DatabaseOutlined' } },
        {
          path: 'fusion',
          meta: { title: '传感数据融合', icon: 'ApiOutlined' },
          children: [
            { path: 'scheme', name: 'FusionScheme', component: () => import('@/views/fusion/scheme/FusionSchemeView.vue'), meta: { title: '融合方案' } },
            { path: 'result', name: 'FusionResult', component: () => import('@/views/fusion/result/FusionResultView.vue'), meta: { title: '融合结果' } },
          ],
        },
        {
          path: 'decision',
          meta: { title: '决策支持', icon: 'ApartmentOutlined' },
          children: [
            { path: 'rule', name: 'DecisionRule', component: () => import('@/views/decision/rule/DecisionRuleView.vue'), meta: { title: '决策规则' } },
            { path: 'result', name: 'DecisionResult', component: () => import('@/views/decision/result/DecisionResultView.vue'), meta: { title: '决策结果' } },
          ],
        },
        { path: 'stats', name: 'Stats', component: () => import('@/views/stats/StatsView.vue'), meta: { title: '统计分析', icon: 'BarChartOutlined' } },
        {
          path: 'system',
          meta: { title: '系统管理', icon: 'SettingOutlined' },
          children: [
            { path: 'user', name: 'SysUser', component: () => import('@/views/system/user/SysUserView.vue'), meta: { title: '用户管理' } },
            { path: 'role', name: 'SysRole', component: () => import('@/views/system/role/SysRoleView.vue'), meta: { title: '角色管理' } },
            { path: 'menu', name: 'SysMenu', component: () => import('@/views/system/menu/SysMenuView.vue'), meta: { title: '菜单管理' } },
          ],
        },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

router.beforeEach(async (to) => {
  const token = localStorage.getItem('access_token')
  if (to.meta.public) return true
  if (!token) return '/login'
  return true
})

export default router
