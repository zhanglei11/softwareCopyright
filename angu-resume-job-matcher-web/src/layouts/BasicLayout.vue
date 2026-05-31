<script setup lang="ts">
import { computed, h, ref, watch } from 'vue'
import type { MenuProps } from 'ant-design-vue'
import { useRoute, useRouter } from 'vue-router'
import {
  AppstoreOutlined,
  ApartmentOutlined,
  BarChartOutlined,
  DashboardOutlined,
  FileTextOutlined,
  OrderedListOutlined,
  PieChartOutlined,
  SearchOutlined,
  SettingOutlined,
  SolutionOutlined,
  TeamOutlined,
  UserOutlined,
  CalendarOutlined,
} from '@ant-design/icons-vue'
import { useAppStore } from '@/store'
import { collectOpenKeys } from '@/utils/permission'

const store = useAppStore()
const route = useRoute()
const router = useRouter()
const openKeys = ref<string[]>([])

const iconMap: Record<string, any> = {
  setting: SettingOutlined,
  user: UserOutlined,
  team: TeamOutlined,
  menu: AppstoreOutlined,
  briefcase: SolutionOutlined,
  file: FileTextOutlined,
  search: SearchOutlined,
  flow: ApartmentOutlined,
  'ordered-list': OrderedListOutlined,
  calendar: CalendarOutlined,
  'bar-chart': BarChartOutlined,
  dashboard: DashboardOutlined,
  'pie-chart': PieChartOutlined,
}

function renderIcon(icon?: string | null) {
  const IconComponent = icon ? (iconMap[icon] || AppstoreOutlined) : AppstoreOutlined
  return () => h(IconComponent)
}

function toMenuItems(nodes: any[]): MenuProps['items'] {
  return nodes.filter((node) => node.menuType !== 2).map((node) => {
    const item: any = {
      key: node.path || `dir-${node.id}`,
      icon: renderIcon(node.icon),
      label: node.menuName,
    }
    const children = toMenuItems(node.children || []) || []
    if (children.length) item.children = children
    return item
  })
}

const menuItems = computed(() => toMenuItems(store.menuTree))
const selectedKeys = computed(() => [route.path])
const currentTitle = computed(() => String(route.meta.title || '招聘工作台'))

watch(
  () => [route.path, store.menuTree],
  () => {
    openKeys.value = collectOpenKeys(store.menuTree, route.path)
  },
  { immediate: true, deep: true },
)

function onMenuClick({ key }: { key: string }) {
  if (key.startsWith('/')) router.push(key)
}

function logout() {
  store.logout()
  router.replace('/login')
}
</script>

<template>
  <a-layout class="shell-layout">
    <a-layout-sider v-model:collapsed="store.collapsed" collapsible width="268" class="shell-sider">
      <div class="brand-block">
        <div class="brand-mark">AG</div>
        <div v-if="!store.collapsed" class="brand-copy">
          <strong>招聘匹配系统</strong>
        </div>
      </div>
      <a-menu v-model:openKeys="openKeys" mode="inline" :selected-keys="selectedKeys" :items="menuItems" class="brand-menu" @click="onMenuClick" />
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="shell-header">
        <div>
          <div class="header-title">{{ currentTitle }}</div>
        </div>
        <div class="header-actions">
          <a-tag color="processing">{{ store.userInfo?.realName || store.userInfo?.username }}</a-tag>
          <a href="http://127.0.0.1:19915/swagger-ui/index.html" target="_blank"><a-button>Swagger</a-button></a>
          <a-button type="primary" @click="logout">退出</a-button>
        </div>
      </a-layout-header>
      <a-layout-content class="shell-content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<style scoped>
.shell-layout { min-height: 100vh; background: transparent; }
.shell-sider {
  overflow: hidden;
  border-right: 1px solid rgba(15, 118, 110, 0.08);
  background: linear-gradient(180deg, rgba(17, 24, 39, 0.96), rgba(15, 118, 110, 0.92));
}
.brand-block { display: flex; align-items: center; gap: 14px; padding: 22px 18px 18px; color: #f7f8f4; }
.brand-mark {
  display: grid;
  place-items: center;
  width: 46px;
  height: 46px;
  border-radius: 16px;
  background: linear-gradient(135deg, #f59e0b, #fb7185);
  color: #111827;
  font-weight: 800;
}
.brand-copy { display: flex; flex-direction: column; }
.brand-copy span { color: rgba(255, 255, 255, 0.72); font-size: 12px; }
:deep(.brand-menu) { background: transparent; color: rgba(255, 255, 255, 0.86); border-inline-end: none; }
:deep(.brand-menu .ant-menu-item), :deep(.brand-menu .ant-menu-submenu-title) { margin-inline: 10px; border-radius: 12px; }
:deep(.brand-menu .ant-menu-item-selected) { background: rgba(255, 255, 255, 0.12); }
.shell-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 82px;
  padding: 18px 24px;
  background: rgba(255, 255, 255, 0.55);
  border-bottom: 1px solid rgba(15, 118, 110, 0.08);
  backdrop-filter: blur(16px);
}
.header-title { font-size: 24px; font-weight: 700; }
.header-actions { display: flex; align-items: center; gap: 12px; }
.shell-content { padding: 24px; }
@media (max-width: 960px) {
  .shell-header { height: auto; align-items: flex-start; flex-direction: column; gap: 12px; }
  .shell-content { padding: 16px; }
}
</style>