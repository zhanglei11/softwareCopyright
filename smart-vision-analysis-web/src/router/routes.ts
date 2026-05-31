import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  { path: '/login', name: 'Login', component: () => import('@/views/login/index.vue'), meta: { title: '登录', requiresAuth: false } },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '首页', icon: 'DashboardOutlined' } },
      // 影像管理
      { path: 'image', redirect: '/image/list', meta: { title: '影像管理', icon: 'PictureOutlined' }, children: [
        { path: 'list', name: 'ImageList', component: () => import('@/views/image/list/index.vue'), meta: { title: '影像列表', icon: 'FileImageOutlined' } },
        { path: 'category', name: 'ImageCategory', component: () => import('@/views/image/category/index.vue'), meta: { title: '影像分类', icon: 'FolderOutlined' } },
      ]},
      // 识别任务
      { path: 'task', redirect: '/task/list', meta: { title: '识别任务', icon: 'PlayCircleOutlined' }, children: [
        { path: 'list', name: 'TaskList', component: () => import('@/views/task/list/index.vue'), meta: { title: '任务列表', icon: 'UnorderedListOutlined' } },
        { path: ':id', name: 'TaskDetail', component: () => import('@/views/task/detail/index.vue'), meta: { title: '任务详情', hidden: true } },
      ]},
      // 识别结果
      { path: 'result', redirect: '/result/list', meta: { title: '识别结果', icon: 'EyeOutlined' }, children: [
        { path: 'list', name: 'ResultList', component: () => import('@/views/result/list/index.vue'), meta: { title: '结果列表', icon: 'TableOutlined' } },
        { path: ':id/annotation', name: 'Annotation', component: () => import('@/views/result/annotation/index.vue'), meta: { title: '标注工具', hidden: true } },
      ]},
      // 分析报告
      { path: 'report', redirect: '/report/task', meta: { title: '分析报告', icon: 'BarChartOutlined' }, children: [
        { path: 'task', name: 'ReportTask', component: () => import('@/views/report/task/index.vue'), meta: { title: '任务报告', icon: 'FileTextOutlined' } },
        { path: 'summary', name: 'ReportSummary', component: () => import('@/views/report/summary/index.vue'), meta: { title: '汇总报告', icon: 'LineChartOutlined' } },
      ]},
      // 模型管理
      { path: 'model', name: 'ModelList', component: () => import('@/views/model/index.vue'), meta: { title: '模型版本', icon: 'RobotOutlined' } },
      // 系统管理
      { path: 'system', redirect: '/system/user', meta: { title: '系统管理', icon: 'SettingOutlined' }, children: [
        { path: 'user', name: 'SystemUser', component: () => import('@/views/system/user/index.vue'), meta: { title: '用户管理', icon: 'UserOutlined' } },
        { path: 'role', name: 'SystemRole', component: () => import('@/views/system/role/index.vue'), meta: { title: '角色管理', icon: 'TeamOutlined' } },
        { path: 'menu', name: 'SystemMenu', component: () => import('@/views/system/menu/index.vue'), meta: { title: '菜单管理', icon: 'MenuOutlined' } },
        { path: 'log', name: 'SystemLog', component: () => import('@/views/system/log/index.vue'), meta: { title: '操作日志', icon: 'AuditOutlined' } },
      ]},
      { path: 'profile', name: 'Profile', component: () => import('@/views/profile/index.vue'), meta: { title: '个人中心', hidden: true } },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/error/404.vue'), meta: { hidden: true } },
]
