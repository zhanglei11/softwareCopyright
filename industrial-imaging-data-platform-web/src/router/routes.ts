import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true },
  },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '数据概览', icon: 'DashboardOutlined' },
      },
      {
        path: 'datasource',
        name: 'Datasource',
        component: () => import('@/views/datasource/index.vue'),
        meta: { title: '数据源管理', icon: 'DatabaseOutlined', permission: 'datasource:config:list' },
      },
      {
        path: 'ingest',
        name: 'Ingest',
        component: () => import('@/views/ingest/index.vue'),
        meta: { title: '数据接入', icon: 'CloudDownloadOutlined', permission: 'ingest:task:list' },
      },
      {
        path: 'process',
        name: 'Process',
        component: () => import('@/views/process/index.vue'),
        meta: { title: '数据处理', icon: 'SettingOutlined', permission: 'process:task:list' },
      },
      {
        path: 'storage',
        name: 'Storage',
        component: () => import('@/views/storage/index.vue'),
        meta: { title: '数据存储', icon: 'HddOutlined', permission: 'storage:clean:list' },
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        meta: { title: '系统管理', icon: 'ToolOutlined' },
        children: [
          {
            path: 'user',
            name: 'SysUser',
            component: () => import('@/views/system/user/index.vue'),
            meta: { title: '用户管理', permission: 'system:user:list' },
          },
          {
            path: 'role',
            name: 'SysRole',
            component: () => import('@/views/system/role/index.vue'),
            meta: { title: '角色管理', permission: 'system:role:list' },
          },
          {
            path: 'menu',
            name: 'SysMenu',
            component: () => import('@/views/system/menu/index.vue'),
            meta: { title: '菜单管理', permission: 'system:menu:list' },
          },
        ],
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/logs/index.vue'),
        meta: { title: '操作日志', icon: 'FileTextOutlined', permission: 'monitor:operlog:list' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { hidden: true },
  },
]
