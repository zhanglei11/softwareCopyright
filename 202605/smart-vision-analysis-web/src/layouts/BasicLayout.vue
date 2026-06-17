<template>
  <a-layout style="min-height:100vh">
    <a-layout-sider v-model:collapsed="collapsed" :trigger="null" collapsible width="220" theme="light" style="border-right:1px solid #f0f0f0">
      <div class="logo">
        <EyeOutlined style="font-size:24px;color:#1677ff" />
        <span v-if="!collapsed" class="logo-text">视觉影像分析</span>
      </div>
      <AppSider :collapsed="collapsed" />
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background:#fff;padding:0;border-bottom:1px solid #f0f0f0">
        <AppHeader v-model:collapsed="collapsed" />
      </a-layout-header>
      <a-layout-content style="margin:16px;overflow:auto">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { EyeOutlined } from '@ant-design/icons-vue'
import AppSider from './Sidebar/Sider.vue'
import AppHeader from './Header/Header.vue'
const collapsed = ref(false)
</script>
<style scoped>
.logo { height:64px;display:flex;align-items:center;justify-content:center;gap:8px;border-bottom:1px solid #f0f0f0;cursor:pointer; }
.logo-text { font-size:14px;font-weight:600;color:#1677ff;white-space:nowrap; }
</style>
