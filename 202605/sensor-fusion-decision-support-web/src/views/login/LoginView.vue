<template>
  <div class="login-page">
    <a-card title="跨场景影像传感数据融合与决策支持系统" style="width:400px">
      <a-form :model="form" @finish="handleLogin" layout="vertical">
        <a-form-item name="username" :rules="[{required:true,message:'请输入用户名'}]">
          <a-input v-model:value="form.username" placeholder="用户名" size="large">
            <template #prefix><user-outlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item name="password" :rules="[{required:true,message:'请输入密码'}]">
          <a-input-password v-model:value="form.password" placeholder="密码" size="large">
            <template #prefix><lock-outlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" block size="large">
            登录
          </a-button>
        </a-form-item>
        <div style="text-align:center;color:#8c8c8c;font-size:12px;margin-top:-8px">
          默认账号：admin / Admin@123
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'Admin@123' })

async function handleLogin() {
  loading.value = true
  try {
    await authStore.login(form.username, form.password)
    message.success('登录成功')
    router.push('/')
  } catch (e: any) {
    message.error(e?.response?.data?.message || e?.message || '账号或密码错误')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
}
</style>
