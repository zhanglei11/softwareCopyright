<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider
      v-model:collapsed="store.collapsed"
      :trigger="null"
      collapsible
      :width="220"
      style="background: #001529"
    >
      <div class="logo">
        <span v-if="!store.collapsed" style="color:#fff;font-size:13px;font-weight:600">工业级影像传感大数据处理软件</span>
        <span v-else style="color:#fff;font-size:13px">IIDP</span>
      </div>
      <Sider />
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background:#fff;padding:0 16px;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,21,41,.08)">
        <MenuFoldOutlined v-if="!store.collapsed" @click="store.toggleCollapsed" style="font-size:18px;cursor:pointer" />
        <MenuUnfoldOutlined v-else @click="store.toggleCollapsed" style="font-size:18px;cursor:pointer" />
        <div style="display:flex;align-items:center;gap:16px">
          <span style="color:#333">{{ store.username }}</span>
          <a-button type="link" @click="handleLogout" style="color:#666">退出</a-button>
        </div>
      </a-layout-header>
      <a-layout-content style="margin:16px;overflow:auto">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue'
import { useAppStore } from '@/store'
import { useRouter } from 'vue-router'
import { logout } from '@/api/auth'
import { message } from 'ant-design-vue'
import Sider from './Sidebar/Sider.vue'

const store = useAppStore()
const router = useRouter()

const handleLogout = async () => {
  try { await logout() } catch { /* ignore */ }
  store.logout()
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
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
</style>
