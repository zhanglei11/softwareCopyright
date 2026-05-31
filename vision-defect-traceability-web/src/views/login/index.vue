<template>
  <div class="login-wrap">
    <div class="login-box">
      <div class="login-title">
        <BugOutlined style="color:#1890ff;font-size:32px" />
        <h2>产线智能视觉缺陷分类与追溯管理系统</h2>
      </div>
      <a-form :model="form" :rules="rules" ref="formRef" @finish="handleLogin">
        <a-form-item name="username">
          <a-input v-model:value="form.username" size="large" placeholder="请输入用户名" allow-clear>
            <template #prefix><UserOutlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item name="password">
          <a-input-password v-model:value="form.password" size="large" placeholder="请输入密码">
            <template #prefix><LockOutlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" size="large" block :loading="loading">
            登录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined, BugOutlined } from '@ant-design/icons-vue'
import { login } from '@/api/login'
import { useAppStore } from '@/store'

const router = useRouter()
const route = useRoute()
const store = useAppStore()
const loading = ref(false)
const formRef = ref()
const form = reactive({ username: 'admin', password: 'Admin@123' })
const rules = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码' }],
}

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(form)
    const data = (res as any).data ?? res
    store.setLoginInfo(data.accessToken, data.userInfo)
    message.success('登录成功')
    const redirect = route.query.redirect as string
    router.replace(redirect && redirect !== '/login' ? redirect : '/')
  } catch {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrap { min-height: 100vh; background: linear-gradient(135deg,#1890ff 0%,#096dd9 100%); display: flex; align-items: center; justify-content: center; }
.login-box { width: 420px; background: #fff; border-radius: 8px; padding: 48px 40px; box-shadow: 0 8px 32px rgba(0,0,0,.15); }
.login-title { text-align: center; margin-bottom: 32px; }
.login-title h2 { margin-top: 12px; font-size: 18px; color: #262626; }
</style>
