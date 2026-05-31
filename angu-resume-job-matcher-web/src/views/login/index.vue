<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useAppStore } from '@/store'

const route = useRoute()
const router = useRouter()
const store = useAppStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'Admin@123' })

async function submit() {
  loading.value = true
  try {
    await store.login(form)
    message.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : store.homePath
    router.replace(redirect || store.homePath)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-shell">
    <section class="login-hero">
      <span class="hero-tag">HR Intelligence Console</span>
      <h1>让职位、简历与招聘流程在一个界面里对齐。</h1>
      <p>聚合职位管理、简历沉淀、智能匹配与招聘流程协同，让团队在同一个工作台上完成闭环决策。</p>
    </section>
    <a-card class="login-card" :bordered="false">
      <div class="login-header">
        <h2>登录前端工作台</h2>
        <p>默认已预填管理员测试账号，可直接开始联调。</p>
      </div>
      <a-form layout="vertical" :model="form" @finish="submit">
        <a-form-item label="用户名" name="username"><a-input v-model:value="form.username" size="large" /></a-form-item>
        <a-form-item label="密码" name="password"><a-input-password v-model:value="form.password" size="large" /></a-form-item>
        <a-button html-type="submit" type="primary" size="large" block :loading="loading">登录并进入系统</a-button>
      </a-form>
      <div class="login-hint">默认账号：admin / Admin@123</div>
    </a-card>
  </div>
</template>

<style scoped>
.login-shell { display: grid; min-height: 100vh; grid-template-columns: 1.2fr 0.9fr; }
.login-hero {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 20px;
  padding: 56px 72px;
  color: #f8fafc;
  background:
    radial-gradient(circle at top left, rgba(251, 191, 36, 0.22), transparent 25%),
    radial-gradient(circle at bottom right, rgba(244, 114, 182, 0.22), transparent 20%),
    linear-gradient(135deg, #0f172a 0%, #0f766e 50%, #164e63 100%);
}
.hero-tag {
  width: fit-content;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.14);
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}
.login-hero h1 { max-width: 560px; margin: 0; font-size: 54px; line-height: 1.08; }
.login-hero p { max-width: 560px; margin: 0; color: rgba(248, 250, 252, 0.82); font-size: 18px; line-height: 1.75; }
.login-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 48px;
  margin: 42px;
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: 0 30px 80px rgba(15, 23, 42, 0.12);
}
.login-header { margin-bottom: 18px; }
.login-header h2 { margin: 0 0 6px; font-size: 28px; }
.login-header p, .login-hint { color: var(--ink-soft); }
.login-hint { margin-top: 16px; font-size: 13px; }
@media (max-width: 1080px) {
  .login-shell { grid-template-columns: 1fr; }
  .login-card, .login-hero { margin: 0; border-radius: 0; padding: 28px; }
  .login-hero h1 { font-size: 36px; }
}
</style>