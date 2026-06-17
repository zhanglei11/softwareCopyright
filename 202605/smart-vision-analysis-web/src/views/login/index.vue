<template>
  <div class="login-page">
    <div class="login-box">
      <div class="login-header">
        <EyeOutlined style="font-size:40px;color:#1677ff;display:block;margin-bottom:12px" />
        <h1>智能视觉影像识别辅助分析系统</h1>
      </div>
      <a-form :model="form" :rules="rules" ref="formRef" @finish="handleLogin">
        <a-form-item name="username">
          <a-input v-model:value="form.username" size="large" placeholder="用户名" :prefix="h(UserOutlined)" allow-clear />
        </a-form-item>
        <a-form-item name="password">
          <a-input-password v-model:value="form.password" size="large" placeholder="密码" :prefix="h(LockOutlined)" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">登 录</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>
<script setup lang="ts">
import { h, reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { EyeOutlined, UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/store'
const router = useRouter(); const route = useRoute(); const userStore = useUserStore()
const loading = ref(false); const formRef = ref()
const form = reactive({ username: 'admin', password: 'Admin@123' })
const rules = { username: [{ required: true, message: '请输入用户名' }], password: [{ required: true, message: '请输入密码' }] }
async function handleLogin() {
  loading.value = true
  try { await userStore.login(form.username, form.password); message.success('登录成功'); router.push((route.query.redirect as string) || '/') }
  catch (e: any) { message.error(e?.message || '登录失败') }
  finally { loading.value = false }
}
</script>
<style scoped>
.login-page { min-height:100vh;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);display:flex;align-items:center;justify-content:center; }
.login-box { background:#fff;border-radius:12px;padding:40px;width:420px;box-shadow:0 20px 60px rgba(0,0,0,0.15); }
.login-header { text-align:center;margin-bottom:32px; }
.login-header h1 { font-size:18px;font-weight:600;color:#262626;margin:0; }
</style>
