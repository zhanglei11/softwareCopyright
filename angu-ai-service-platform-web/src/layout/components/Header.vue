<template>
  <a-layout-header style="background:#fff;padding:0 24px;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,0,0,0.1)">
    <div style="font-size:18px;font-weight:bold;color:#1a1a1a">{{ $t('login.title') }}</div>
    <a-dropdown>
      <a-space style="cursor:pointer">
        <a-avatar style="background:#1890ff"><UserOutlined /></a-avatar>
        <span>{{ store.userInfo?.realName || store.userInfo?.username }}</span>
        <DownOutlined />
      </a-space>
      <template #overlay>
        <a-menu>
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
import { useAppStore } from '@/store'
import { UserOutlined, DownOutlined, LogoutOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

const store = useAppStore()
const router = useRouter()

const handleLogout = () => {
  store.logout()
  message.success('已退出登录')
  router.replace('/login')
}
</script>
