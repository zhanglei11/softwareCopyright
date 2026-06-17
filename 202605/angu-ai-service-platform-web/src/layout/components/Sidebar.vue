<template>
  <a-layout-sider v-model:collapsed="collapsed" collapsible :width="220" style="background:#001529">
    <div class="logo">
      <span v-if="!collapsed" style="color:#fff;font-weight:bold;font-size:16px">安谷AI平台</span>
      <span v-else style="color:#fff;font-weight:bold;font-size:14px">AI</span>
    </div>
    <a-menu v-model:selectedKeys="selectedKeys" v-model:openKeys="openKeys" mode="inline" theme="dark" :inline-collapsed="collapsed">
      <a-menu-item key="dashboard">
        <template #icon><DashboardOutlined /></template>
        <router-link to="/dashboard">使用量看板</router-link>
      </a-menu-item>
      <a-sub-menu key="system" v-if="isSuperAdmin">
        <template #icon><SettingOutlined /></template>
        <template #title>系统管理</template>
        <a-menu-item key="system/user"><router-link to="/system/user">用户管理</router-link></a-menu-item>
        <a-menu-item key="system/role"><router-link to="/system/role">角色管理</router-link></a-menu-item>
        <a-menu-item key="system/menu"><router-link to="/system/menu">菜单管理</router-link></a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="ai" v-if="isAdmin">
        <template #icon><ApiOutlined /></template>
        <template #title>AI管理</template>
        <a-menu-item key="ai/category"><router-link to="/ai/category">场景分类</router-link></a-menu-item>
        <a-menu-item key="ai/scene"><router-link to="/ai/scene">场景配置</router-link></a-menu-item>
        <a-menu-item key="ai/model"><router-link to="/ai/model">AI模型配置</router-link></a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="app">
        <template #icon><AppstoreOutlined /></template>
        <template #title>应用功能</template>
        <a-menu-item key="app/square"><router-link to="/app/square">场景广场</router-link></a-menu-item>
        <a-menu-item key="app/chat"><router-link to="/app/chat">智能对话</router-link></a-menu-item>
        <a-menu-item key="app/doc"><router-link to="/app/doc">文档分析</router-link></a-menu-item>
        <a-menu-item key="app/generate"><router-link to="/app/generate">内容生成</router-link></a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="kb" v-if="isAdmin">
        <template #icon><DatabaseOutlined /></template>
        <template #title>知识库管理</template>
        <a-menu-item key="kb/base"><router-link to="/kb/base">知识库列表</router-link></a-menu-item>
      </a-sub-menu>
      <a-menu-item key="conversation">
        <template #icon><HistoryOutlined /></template>
        <router-link to="/conversation">历史会话</router-link>
      </a-menu-item>
      <a-menu-item key="stats" v-if="isAdmin">
        <template #icon><BarChartOutlined /></template>
        <router-link to="/stats">场景统计</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store'
import { DashboardOutlined, SettingOutlined, ApiOutlined, AppstoreOutlined, DatabaseOutlined, HistoryOutlined, BarChartOutlined } from '@ant-design/icons-vue'

const store = useAppStore()
const route = useRoute()
const collapsed = ref(false)
const isSuperAdmin = computed(() => store.roles.includes('SUPER_ADMIN'))
const isAdmin = computed(() => store.roles.includes('SUPER_ADMIN') || store.roles.includes('AI_ADMIN'))
const selectedKeys = ref<string[]>([])
const openKeys = ref<string[]>([])

watch(() => route.path, (path) => {
  const parts = path.replace(/^\//, '').split('/')
  if (parts.length >= 2) {
    selectedKeys.value = [parts.slice(0, 2).join('/')]
    openKeys.value = [parts[0]]
  } else {
    selectedKeys.value = [parts[0]]
  }
}, { immediate: true })
</script>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
</style>
