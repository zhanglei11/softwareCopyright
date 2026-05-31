<template>
  <div class="login-wrap">
    <div class="login-box">
      <div class="login-title">
        <h2>多场景影像传感设备资源协同调度系统平台</h2>
        <p>Imaging Device Scheduler</p>
      </div>
      <a-form :model="form" @finish="handleLogin" layout="vertical">
        <a-form-item name="username" :rules="[{ required: true, message: '请输入用户名' }]">
          <a-input v-model:value="form.username" size="large" placeholder="用户名">
            <template #prefix><UserOutlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item name="password" :rules="[{ required: true, message: '请输入密码' }]">
          <a-input-password v-model:value="form.password" size="large" placeholder="密码">
            <template #prefix><LockOutlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" size="large" block>
            登 录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { loginApi } from '@/api/auth'
import { useAppStore } from '@/store'

const router = useRouter()
const route = useRoute()
const store = useAppStore()
const loading = ref(false)

const form = reactive({ username: 'admin', password: 'admin123' })

async function handleLogin() {
  loading.value = true
  try {
    const res: any = await loginApi(form)
    const data = res.data
    store.setTokens(data.accessToken, data.refreshToken)
    store.setUser(data.userInfo)
    localStorage.setItem('ids_user', JSON.stringify(data.userInfo))
    message.success('登录成功')
    const redirect = route.query.redirect as string || '/'
    router.push(redirect)
  } catch {
    // error handled in request interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-wrap {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #001529 0%, #003a70 100%);
}
.login-box {
  width: 400px;
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-title {
  text-align: center;
  margin-bottom: 32px;
  h2 { font-size: 22px; color: #001529; margin-bottom: 4px; }
  p { color: #999; font-size: 13px; }
}
</style>
