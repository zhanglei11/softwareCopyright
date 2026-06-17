<template>
  <a-menu v-model:selectedKeys="selectedKeys" v-model:openKeys="openKeys" mode="inline" :inline-collapsed="collapsed" :items="menuItems" @click="handleClick" />
</template>
<script setup lang="ts">
import { ref, computed, watch, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as Icons from '@ant-design/icons-vue'
import { routes } from '@/router/routes'

const props = defineProps<{ collapsed: boolean }>()
const route = useRoute()
const router = useRouter()
const selectedKeys = ref<string[]>([route.name as string])
const openKeys = ref<string[]>([])

watch(() => route.name, (n) => { selectedKeys.value = [n as string] })

function renderIcon(name?: string) { if (!name) return undefined; const I = (Icons as any)[name]; return I ? () => h(I) : undefined }

function buildMenu(list: any[]): any[] {
  return list.filter(r => !r.meta?.hidden).map(r => {
    const cs = r.children?.filter((c: any) => !c.meta?.hidden)
    if (cs?.length) return { key: r.name || r.path, label: r.meta?.title, icon: renderIcon(r.meta?.icon), children: buildMenu(r.children) }
    return { key: r.name || r.path, label: r.meta?.title, icon: renderIcon(r.meta?.icon) }
  })
}

const menuItems = computed(() => {
  const layout = routes.find(r => r.path === '/')
  return layout?.children ? buildMenu(layout.children) : []
})

function handleClick({ key }: { key: string }) { router.push({ name: key }) }
</script>
