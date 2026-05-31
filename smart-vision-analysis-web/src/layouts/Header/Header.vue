<template>
  <div class="app-header">
    <div class="left">
      <component :is="collapsed ? MenuUnfoldOutlined : MenuFoldOutlined" class="trigger" @click="$emit('update:collapsed', !collapsed)" />
    </div>
    <div class="right">
      <a-dropdown>
        <span class="user-info">
          <a-avatar size="small"><template #icon><UserOutlined /></template></a-avatar>
          <span style="margin-left:8px">{{ userInfo?.realName || userInfo?.username || '用户' }}</span>
        </span>
        <template #overlay>
          <a-menu>
            <a-menu-item key="profile" @click="$router.push('/profile')"><UserOutlined /> 个人中心</a-menu-item>
            <a-menu-divider />
            <a-menu-item key="logout" @click="handleLogout"><LogoutOutlined /> 退出登录</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { MenuFoldOutlined, MenuUnfoldOutlined, UserOutlined, LogoutOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/store'
import { message } from 'ant-design-vue'
defineProps<{ collapsed: boolean }>()
defineEmits(['update:collapsed'])
const router = useRouter()
const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)
function handleLogout() { userStore.logout(); message.success('已退出登录'); router.push('/login') }
</script>
<style scoped>
.app-header { height:64px;display:flex;align-items:center;justify-content:space-between;padding:0 16px; }
.left,.right { display:flex;align-items:center; }
.trigger { font-size:18px;cursor:pointer;color:rgba(0,0,0,0.65); }
.trigger:hover { color:#1677ff; }
.user-info { cursor:pointer;display:flex;align-items:center;padding:0 12px;height:64px; }
</style>
