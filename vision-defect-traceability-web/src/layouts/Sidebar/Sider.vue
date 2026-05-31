<template>
  <a-layout-sider v-model:collapsed="collapsed" collapsible :width="220" style="background:#001529">
    <div class="logo">
      <BugOutlined style="color:#1890ff;font-size:20px" />
      <span v-if="!collapsed" style="color:#fff;margin-left:8px;font-size:14px;font-weight:600;">缺陷追溯系统</span>
    </div>
    <a-menu
      v-model:selectedKeys="selectedKeys"
      v-model:openKeys="openKeys"
      theme="dark"
      mode="inline"
      @click="handleMenuClick"
    >
      <template v-for="item in menuItems" :key="item.name">
        <a-sub-menu v-if="item.children?.length" :key="item.name">
          <template #icon><component :is="item.icon" /></template>
          <template #title>{{ t(item.title) }}</template>
          <a-menu-item v-for="child in item.children" :key="child.name">
            {{ t(child.title) }}
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item v-else :key="item.name">
          <template #icon><component :is="item.icon" /></template>
          {{ t(item.title) }}
        </a-menu-item>
      </template>
    </a-menu>
  </a-layout-sider>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import {
  DashboardOutlined, BugOutlined, ApartmentOutlined,
  BellOutlined, BarChartOutlined, SearchOutlined, SettingOutlined
} from '@ant-design/icons-vue'

const { t } = useI18n()
const router = useRouter()
const route = useRoute()
const collapsed = ref(false)

const iconMap: Record<string, unknown> = {
  DashboardOutlined, BugOutlined, ApartmentOutlined,
  BellOutlined, BarChartOutlined, SearchOutlined, SettingOutlined
}

const menuItems = [
  { name: 'Dashboard', title: 'menu_dashboard', icon: DashboardOutlined },
  { name: 'Defect', title: 'menu_defect', icon: BugOutlined, children: [
    { name: 'DefectRecord', title: 'menu_defectRecord' },
    { name: 'DefectCategory', title: 'menu_defectCategory' },
  ]},
  { name: 'Line', title: 'menu_line', icon: ApartmentOutlined, children: [
    { name: 'LineManage', title: 'menu_lineManage' },
    { name: 'LineProduct', title: 'menu_product' },
  ]},
  { name: 'Alert', title: 'menu_alert', icon: BellOutlined, children: [
    { name: 'AlertRule', title: 'menu_alertRule' },
    { name: 'AlertRecord', title: 'menu_alertRecord' },
  ]},
  { name: 'Stats', title: 'menu_stats', icon: BarChartOutlined },
  { name: 'Trace', title: 'menu_trace', icon: SearchOutlined },
  { name: 'System', title: 'menu_system', icon: SettingOutlined, children: [
    { name: 'SystemUser', title: 'menu_systemUser' },
    { name: 'SystemRole', title: 'menu_systemRole' },
  ]},
]

const selectedKeys = ref<string[]>([])
const openKeys = ref<string[]>([])

watch(() => route.name, (name) => {
  if (name) selectedKeys.value = [name as string]
}, { immediate: true })

const handleMenuClick = ({ key }: { key: string }) => {
  router.push({ name: key })
}
</script>

<style scoped>
.logo { height: 64px; display: flex; align-items: center; justify-content: center; padding: 0 16px; }
</style>
