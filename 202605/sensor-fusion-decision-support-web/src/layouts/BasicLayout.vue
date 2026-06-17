<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <div class="logo">
        <span v-if="!collapsed">传感融合决策系统</span>
        <span v-else>SFD</span>
      </div>
      <a-menu
        v-model:selected-keys="selectedKeys"
        v-model:open-keys="openKeys"
        theme="dark"
        mode="inline"
        @click="handleMenuClick"
      >
        <a-menu-item key="/dashboard"><home-outlined /><span>首页</span></a-menu-item>
        <a-menu-item key="/datasource"><database-outlined /><span>数据源管理</span></a-menu-item>
        <a-sub-menu key="fusion"><template #icon><api-outlined /></template><template #title>传感数据融合</template>
          <a-menu-item key="/fusion/scheme">融合方案</a-menu-item>
          <a-menu-item key="/fusion/result">融合结果</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="decision"><template #icon><apartment-outlined /></template><template #title>决策支持</template>
          <a-menu-item key="/decision/rule">决策规则</a-menu-item>
          <a-menu-item key="/decision/result">决策结果</a-menu-item>
        </a-sub-menu>
        <a-menu-item key="/stats"><bar-chart-outlined /><span>统计分析</span></a-menu-item>
        <a-sub-menu key="system"><template #icon><setting-outlined /></template><template #title>系统管理</template>
          <a-menu-item key="/system/user">用户管理</a-menu-item>
          <a-menu-item key="/system/role">角色管理</a-menu-item>
          <a-menu-item key="/system/menu">菜单管理</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background:#fff;padding:0 16px;display:flex;align-items:center;justify-content:space-between">
        <menu-fold-outlined v-if="!collapsed" @click="collapsed=true" style="font-size:18px;cursor:pointer" />
        <menu-unfold-outlined v-else @click="collapsed=false" style="font-size:18px;cursor:pointer" />
        <a-dropdown>
          <a-space style="cursor:pointer">
            <user-outlined />
            <span>{{ authStore.userInfo?.realName || authStore.userInfo?.username || '管理员' }}</span>
          </a-space>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="handleLogout"><logout-outlined /> 退出登录</a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-layout-header>
      <a-layout-content style="margin:16px;padding:16px;background:#fff;min-height:280px">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { message } from 'ant-design-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const collapsed = ref(false)
const selectedKeys = ref<string[]>([route.path])
const openKeys = ref<string[]>(['fusion', 'decision', 'system'])

watch(() => route.path, (path) => { selectedKeys.value = [path] })

function handleMenuClick({ key }: { key: string }) {
  router.push(key)
}

async function handleLogout() {
  authStore.logout()
  message.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  padding: 0 8px;
  white-space: nowrap;
  overflow: hidden;
}
</style>
