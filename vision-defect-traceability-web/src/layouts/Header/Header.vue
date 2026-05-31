<template>
  <a-layout-header style="background:#fff;padding:0 24px;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,21,41,.08)">
    <span style="font-size:16px;font-weight:600;color:#1890ff">产线智能视觉缺陷分类与追溯管理系统</span>
    <a-dropdown>
      <a-space style="cursor:pointer">
        <UserOutlined />
        <span>{{ store.userInfo?.realName || '管理员' }}</span>
      </a-space>
      <template #overlay>
        <a-menu>
          <a-menu-item key="profile" @click="router.push('/profile')">
            <UserOutlined /> 个人中心
          </a-menu-item>
          <a-menu-divider />
          <a-menu-item key="logout" @click="handleLogout">
            <LogoutOutlined /> 退出登录
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </a-layout-header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons-vue'
import { useAppStore } from '@/store'
import { logout } from '@/api/login'

const router = useRouter()
const store = useAppStore()

const handleLogout = () => {
  Modal.confirm({
    title: '确认退出',
    content: '确认退出登录？',
    onOk: async () => {
      try { await logout() } catch {}
      store.logout()
      router.replace('/login')
      message.success('已退出登录')
    }
  })
}
</script>
