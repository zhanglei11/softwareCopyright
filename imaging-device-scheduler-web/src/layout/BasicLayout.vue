<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider
      v-model:collapsed="store.collapsed"
      :trigger="null"
      collapsible
      width="220"
      style="background: #001529"
    >
      <div class="logo">
        <span v-if="!store.collapsed">多场景影像传感设备资源协同调度系统</span>
        <span v-else>IDS</span>
      </div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        theme="dark"
        mode="inline"
        @click="handleMenuClick"
      >
        <a-menu-item key="/dashboard">
          <template #icon><DashboardOutlined /></template>
          调度总览
        </a-menu-item>
        <a-menu-item key="/device">
          <template #icon><DesktopOutlined /></template>
          设备管理
        </a-menu-item>
        <a-sub-menu key="scene">
          <template #icon><AppstoreOutlined /></template>
          <template #title>场景管理</template>
          <a-menu-item key="/scene/group">场景分组</a-menu-item>
          <a-menu-item key="/scene/list">场景列表</a-menu-item>
        </a-sub-menu>
        <a-menu-item key="/task">
          <template #icon><OrderedListOutlined /></template>
          任务管理
        </a-menu-item>
        <a-menu-item key="/dispatch">
          <template #icon><ScheduleOutlined /></template>
          调度管理
        </a-menu-item>
        <a-menu-item key="/stats">
          <template #icon><BarChartOutlined /></template>
          统计分析
        </a-menu-item>
        <a-sub-menu key="system">
          <template #icon><SettingOutlined /></template>
          <template #title>系统管理</template>
          <a-menu-item key="/system/user">用户管理</a-menu-item>
          <a-menu-item key="/system/role">角色管理</a-menu-item>
          <a-menu-item key="/system/menu">菜单管理</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0 16px; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 1px 4px rgba(0,21,41,.08)">
        <div style="display:flex;align-items:center;gap:12px">
          <menu-unfold-outlined v-if="store.collapsed" @click="store.toggleCollapsed" style="font-size:18px;cursor:pointer" />
          <menu-fold-outlined v-else @click="store.toggleCollapsed" style="font-size:18px;cursor:pointer" />
          <a-breadcrumb>
            <a-breadcrumb-item>多场景影像传感设备资源协同调度系统平台</a-breadcrumb-item>
            <a-breadcrumb-item>{{ currentTitle }}</a-breadcrumb-item>
          </a-breadcrumb>
        </div>
        <a-dropdown>
          <a style="color:#333">
            <UserOutlined /> {{ store.userInfo?.realName || store.userInfo?.username }}
            <DownOutlined />
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="handleLogout">退出登录</a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-layout-header>
      <a-layout-content style="margin: 16px; overflow: auto">
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  DashboardOutlined, DesktopOutlined, AppstoreOutlined,
  OrderedListOutlined, ScheduleOutlined, BarChartOutlined,
  SettingOutlined, UserOutlined, DownOutlined,
  MenuFoldOutlined, MenuUnfoldOutlined,
} from '@ant-design/icons-vue'
import { useAppStore } from '@/store'
import { logoutApi } from '@/api/auth'

const store = useAppStore()
const route = useRoute()
const router = useRouter()

const selectedKeys = ref<string[]>([route.path])
const openKeys = ref<string[]>([])
const currentTitle = computed(() => route.meta.title as string || '')

watch(() => route.path, (path) => {
  selectedKeys.value = [path]
  if (path.startsWith('/scene')) openKeys.value = ['scene']
  else if (path.startsWith('/system')) openKeys.value = ['system']
})

function handleMenuClick({ key }: { key: string }) {
  router.push(key)
}

async function handleLogout() {
  try { await logoutApi() } catch {}
  store.logout()
}
</script>

<style scoped lang="scss">
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
