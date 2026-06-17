<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible theme="dark" width="220">
      <div class="logo">
        <span v-if="!collapsed">质量管控系统</span>
        <span v-else>QC</span>
      </div>
      <a-menu
        theme="dark"
        mode="inline"
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        @click="onMenuClick"
      >
        <a-menu-item key="/dashboard">
          <template #icon><dashboard-outlined /></template>
          质量看板
        </a-menu-item>
        <a-sub-menu key="quality">
          <template #icon><experiment-outlined /></template>
          <template #title>质量管理</template>
          <a-menu-item key="/quality/metric">质量指标</a-menu-item>
          <a-menu-item key="/quality/template">标准模板</a-menu-item>
          <a-menu-item key="/quality/task">检测任务</a-menu-item>
          <a-menu-item key="/quality/analysis">一致性分析</a-menu-item>
        </a-sub-menu>
        <a-menu-item key="/defect">
          <template #icon><warning-outlined /></template>
          不合格品管理
        </a-menu-item>
        <a-menu-item key="/stats">
          <template #icon><bar-chart-outlined /></template>
          趋势报表
        </a-menu-item>
        <a-menu-item key="/agent">
          <template #icon><robot-outlined /></template>
          智能体管理
        </a-menu-item>
        <a-sub-menu key="system">
          <template #icon><setting-outlined /></template>
          <template #title>系统管理</template>
          <a-menu-item key="/system/user">用户管理</a-menu-item>
          <a-menu-item key="/system/role">角色管理</a-menu-item>
          <a-menu-item key="/system/menu">菜单管理</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background:#fff; padding: 0 24px; display:flex; align-items:center; justify-content:space-between; box-shadow:0 1px 4px rgba(0,0,0,.1)">
        <a-breadcrumb>
          <a-breadcrumb-item>{{ currentTitle }}</a-breadcrumb-item>
        </a-breadcrumb>
        <a-dropdown>
          <a-space>
            <a-avatar style="background:#1677ff">{{ userInitial }}</a-avatar>
            <span>{{ authStore.userInfo?.username || '管理员' }}</span>
          </a-space>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="handleLogout">退出登录</a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-layout-header>
      <a-layout-content style="margin: 16px; background:#f0f2f5">
        <div style="background:#fff; min-height:calc(100vh - 120px); padding:24px; border-radius:8px">
          <router-view />
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import {
  DashboardOutlined, ExperimentOutlined, WarningOutlined,
  BarChartOutlined, SettingOutlined, RobotOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const collapsed = ref(false)
const selectedKeys = ref([route.path])
const openKeys = ref<string[]>([])

watch(() => route.path, (p) => {
  selectedKeys.value = [p]
  if (p.startsWith('/quality')) openKeys.value = ['quality']
  else if (p.startsWith('/system')) openKeys.value = ['system']
  else if (p.startsWith('/agent')) openKeys.value = []
})

const currentTitle = computed(() => (route.meta?.title as string) || '')
const userInitial = computed(() => {
  const u = authStore.userInfo?.username || 'A'
  return u[0].toUpperCase()
})

function onMenuClick({ key }: { key: string }) {
  router.push(key)
}

function handleLogout() {
  authStore.logout()
  router.push('/login')
}

authStore.fetchUserInfo().catch(() => {})
</script>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  overflow: hidden;
  white-space: nowrap;
}
</style>
