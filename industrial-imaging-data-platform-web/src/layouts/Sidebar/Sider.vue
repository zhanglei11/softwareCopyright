<template>
  <a-menu
    v-model:selectedKeys="selectedKeys"
    v-model:openKeys="openKeys"
    mode="inline"
    theme="dark"
    :inline-collapsed="store.collapsed"
  >
    <template v-for="route in visibleRoutes" :key="route.name">
      <template v-if="route.children?.length">
        <a-sub-menu :key="route.name">
          <template #icon>
            <component :is="getIcon(route.meta?.icon as string)" />
          </template>
          <template #title>{{ route.meta?.title }}</template>
          <a-menu-item v-for="child in route.children?.filter(c => !c.meta?.hidden)" :key="child.name" @click="router.push(getPath(route, child))">
            {{ child.meta?.title }}
          </a-menu-item>
        </a-sub-menu>
      </template>
      <template v-else>
        <a-menu-item :key="route.name" @click="router.push('/' + route.path)">
          <template #icon>
            <component :is="getIcon(route.meta?.icon as string)" />
          </template>
          {{ route.meta?.title }}
        </a-menu-item>
      </template>
    </template>
  </a-menu>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAppStore } from '@/store'
import { routes } from '@/router/routes'
import * as Icons from '@ant-design/icons-vue'

const store = useAppStore()
const router = useRouter()
const route = useRoute()

const mainRoutes = routes.find(r => r.path === '/')?.children ?? []
const visibleRoutes = computed(() => mainRoutes.filter(r => !r.meta?.hidden))

const selectedKeys = ref<string[]>([])
const openKeys = ref<string[]>([])

watch(() => route.name, (name) => {
  if (name) selectedKeys.value = [String(name)]
}, { immediate: true })

const getIcon = (name?: string) => name ? (Icons as Record<string, unknown>)[name] : null

const getPath = (parent: typeof mainRoutes[0], child: typeof mainRoutes[0]) => {
  return `/${parent.path}/${child.path}`
}
</script>
