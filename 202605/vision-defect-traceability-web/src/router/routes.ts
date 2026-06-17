import type { RouteRecordRaw } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'

export const routes: RouteRecordRaw[] = [
  { path: '/login', name: 'Login', component: () => import('@/views/login/index.vue'), meta: { title: 'menu_login' } },
  { path: '/404', name: '404', component: () => import('@/views/error/404.vue') },
  { path: '/:pathMatch(.*)*', redirect: '/404' },
  {
    path: '/',
    component: BasicLayout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: 'menu_dashboard', icon: 'DashboardOutlined', requiresAuth: true } },
      { path: 'profile', name: 'Profile', component: () => import('@/views/profile/index.vue'), meta: { title: 'menu_profile', requiresAuth: true, hidden: true } },
      { path: 'defect/record', name: 'DefectRecord', component: () => import('@/views/defect/record/index.vue'), meta: { title: 'menu_defectRecord', requiresAuth: true } },
      { path: 'defect/category', name: 'DefectCategory', component: () => import('@/views/defect/category/index.vue'), meta: { title: 'menu_defectCategory', requiresAuth: true } },
      { path: 'line/manage', name: 'LineManage', component: () => import('@/views/line/manage/index.vue'), meta: { title: 'menu_lineManage', requiresAuth: true } },
      { path: 'line/product', name: 'LineProduct', component: () => import('@/views/line/product/index.vue'), meta: { title: 'menu_product', requiresAuth: true } },
      { path: 'alert/rule', name: 'AlertRule', component: () => import('@/views/alert/rule/index.vue'), meta: { title: 'menu_alertRule', requiresAuth: true } },
      { path: 'alert/record', name: 'AlertRecord', component: () => import('@/views/alert/record/index.vue'), meta: { title: 'menu_alertRecord', requiresAuth: true } },
      { path: 'stats', name: 'Stats', component: () => import('@/views/stats/index.vue'), meta: { title: 'menu_stats', icon: 'BarChartOutlined', requiresAuth: true } },
      { path: 'trace', name: 'Trace', component: () => import('@/views/trace/index.vue'), meta: { title: 'menu_trace', icon: 'SearchOutlined', requiresAuth: true } },
      { path: 'system/user', name: 'SystemUser', component: () => import('@/views/system/user/index.vue'), meta: { title: 'menu_systemUser', requiresAuth: true } },
      { path: 'system/role', name: 'SystemRole', component: () => import('@/views/system/role/index.vue'), meta: { title: 'menu_systemRole', requiresAuth: true } },
    ]
  }
]
