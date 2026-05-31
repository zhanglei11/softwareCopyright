<template>
  <div class="page-container">
    <a-card title="个人信息">
      <a-descriptions :column="2" bordered>
        <a-descriptions-item label="用户名">{{ userInfo?.username }}</a-descriptions-item>
        <a-descriptions-item label="真实姓名">{{ userInfo?.realName }}</a-descriptions-item>
        <a-descriptions-item label="手机号">{{ userInfo?.phone }}</a-descriptions-item>
        <a-descriptions-item label="邮箱">{{ userInfo?.email }}</a-descriptions-item>
        <a-descriptions-item label="角色">{{ userInfo?.roles?.join(', ') }}</a-descriptions-item>
      </a-descriptions>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useUserStore } from '@/store'
const userInfo = computed(() => useUserStore().userInfo)
</script>
