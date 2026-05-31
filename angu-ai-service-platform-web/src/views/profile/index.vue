<template>
  <div class="page-container">
    <a-row :gutter="16">
      <a-col :span="8">
        <a-card>
          <div style="text-align: center; padding: 16px">
            <a-avatar :size="80" :src="userInfo?.avatar">
              <template #icon><UserOutlined /></template>
            </a-avatar>
            <div style="margin-top: 12px; font-size: 16px; font-weight: 600">
              {{ userInfo?.realName || userInfo?.username }}
            </div>
            <div style="color: #8c8c8c; margin-top: 4px">{{ userInfo?.roles?.join(', ') }}</div>
          </div>
        </a-card>
      </a-col>
      <a-col :span="16">
        <a-card title="基本信息">
          <a-descriptions :column="1" bordered>
            <a-descriptions-item label="用户名">{{ userInfo?.username }}</a-descriptions-item>
            <a-descriptions-item label="真实姓名">{{ userInfo?.realName }}</a-descriptions-item>
            <a-descriptions-item label="手机号">{{ userInfo?.phone }}</a-descriptions-item>
            <a-descriptions-item label="邮箱">{{ userInfo?.email }}</a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { UserOutlined } from '@ant-design/icons-vue'
import { useAppStore } from '@/store'

const userStore = useAppStore()
const userInfo = computed(() => userStore.userInfo)
</script>
