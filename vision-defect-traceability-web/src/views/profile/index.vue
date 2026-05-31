<template>
  <a-card title="个人中心">
    <a-descriptions :column="2" bordered>
      <a-descriptions-item label="用户名">{{ store.userInfo?.realName }}</a-descriptions-item>
      <a-descriptions-item label="角色">{{ store.userInfo?.roles?.join(', ') }}</a-descriptions-item>
    </a-descriptions>
  </a-card>
</template>
<script setup lang="ts">
import { useAppStore } from '@/store'
const store = useAppStore()
</script>
