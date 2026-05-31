<template>
  <div class="login-bg">
    <div class="login-box">
      <div class="login-title">视觉检测影像质量一致性管控系统</div>
      <a-form :model="form" @finish="onLogin" layout="vertical" style="margin-top:32px">
        <a-form-item name="username" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input v-model:value="form.username" placeholder="登录账号" size="large" allow-clear>
            <template #prefix><user-outlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item name="password" :rules="[{ required: true, message: '请输入密码' }]">
          <a-input-password v-model:value="form.password" placeholder="登录密码" size="large">
            <template #prefix><lock-outlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" size="large" block :loading="loading">
            登 录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'Admin@123' })

async function onLogin() {
  try {
    loading.value = true
    await authStore.login(form.username, form.password)
    message.success('登录成功')
    router.push('/')
  } catch {
    // error already shown by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #1d3a6e 0%, #0d6efd 60%, #198754 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-box {
  background: #fff;
  border-radius: 12px;
  padding: 48px 40px;
  width: 420px;
  box-shadow: 0 8px 32px rgba(0,0,0,.25);
}
.login-title {
  font-size: 20px;
  font-weight: 700;
  color: #1d3a6e;
  text-align: center;
}
</style>
