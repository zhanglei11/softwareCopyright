<template>
  <div class="login-container">
    <a-card class="login-card" title="工业级影像传感大数据处理软件" :bordered="false">
      <a-form :model="form" :rules="rules" ref="formRef" @finish="handleLogin">
        <a-form-item name="username">
          <a-input v-model:value="form.username" placeholder="用户名" size="large" prefix-icon="UserOutlined">
            <template #prefix><UserOutlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item name="password">
          <a-input-password v-model:value="form.password" placeholder="密码" size="large">
            <template #prefix><LockOutlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" block size="large">登录</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { login } from '@/api/auth'
import { useAppStore } from '@/store'

const router = useRouter()
const route = useRoute()
const store = useAppStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({ username: 'admin', password: 'Admin@123' })
const rules = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码' }],
}

/** 解析 JWT payload（不校验签名，仅读取展示字段） */
const parseJwt = (token: string) => {
  try {
    const payload = token.split('.')[1]
    return JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')))
  } catch { return {} }
}

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(form)
    const { accessToken, refreshToken } = res.data
    const claims = parseJwt(accessToken)
    store.setAuth(accessToken, refreshToken, {
      id: claims.userId ?? 0,
      username: claims.sub ?? form.username,
      realName: claims.sub ?? form.username,
      roles: [],
    })
    message.success('登录成功')
    const redirect = route.query.redirect as string
    router.push(redirect || '/')
  } catch {
    message.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1677ff 0%, #0050b3 100%);
}
.login-card {
  width: 400px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.2);
}
:deep(.ant-card-head-title) {
  text-align: center;
  font-size: 20px;
  font-weight: 600;
}
</style>
