import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  { path: '/login', name: 'login', component: () => import('@/views/login/index.vue'), meta: { public: true, title: '登录' } },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/stats/dashboard',
    children: [
      { path: '/system/users', component: () => import('@/views/system/user/index.vue'), meta: { title: '用户管理' } },
      { path: '/system/roles', component: () => import('@/views/system/role/index.vue'), meta: { title: '角色管理' } },
      { path: '/system/menus', component: () => import('@/views/system/menu/index.vue'), meta: { title: '菜单管理' } },
      { path: '/jobs/list', component: () => import('@/views/jobs/index.vue'), meta: { title: '职位管理' } },
      { path: '/resumes/list', component: () => import('@/views/resumes/index.vue'), meta: { title: '简历管理' } },
      { path: '/match/list', component: () => import('@/views/match/index.vue'), meta: { title: '智能匹配' } },
      { path: '/applications', component: () => import('@/views/applications/index.vue'), meta: { title: '投递记录' } },
      { path: '/interviews', component: () => import('@/views/interviews/index.vue'), meta: { title: '面试管理' } },
      { path: '/stats/dashboard', component: () => import('@/views/stats/dashboard.vue'), meta: { title: '数据看板' } },
      { path: '/stats/source', component: () => import('@/views/stats/source.vue'), meta: { title: '来源统计' } },
    ],
  },
  { path: '/404', name: '404', component: () => import('@/views/error/404.vue'), meta: { public: true, title: '页面不存在' } },
  { path: '/:pathMatch(.*)*', redirect: '/404' },
]