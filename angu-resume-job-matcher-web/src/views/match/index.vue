<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { jobApi } from '@/api/jobs'
import { matchApi } from '@/api/match'
import { pickPage } from '@/utils/common'

const loading = ref(false)
const saving = ref(false)
const positions = ref<any[]>([])
const results = ref<any[]>([])
const form = reactive({ positionId: undefined as number | undefined, skillWeight: 50, eduWeight: 30, expWeight: 20 })

async function loadPositions() {
  const response: any = await jobApi.list({ status: 'OPEN', page: 1, size: 999 })
  positions.value = pickPage(response).list
}

async function loadConfig() {
  const response: any = await matchApi.getConfig()
  Object.assign(form, response.data || {})
}

async function saveConfig() {
  saving.value = true
  try {
    await matchApi.updateConfig({ skillWeight: form.skillWeight, eduWeight: form.eduWeight, expWeight: form.expWeight })
    message.success('权重配置已保存')
  } finally {
    saving.value = false
  }
}

async function runMatch() {
  if (!form.positionId) {
    message.warning('请先选择职位')
    return
  }
  loading.value = true
  try {
    const response: any = await matchApi.run(form.positionId)
    results.value = response.data || []
    message.success('匹配完成')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await Promise.all([loadPositions(), loadConfig()])
})
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>智能匹配</h2><p>选择开放职位并运行候选人排序。</p></div>
    <div class="split-grid">
      <a-card class="glass-card" :bordered="false">
        <a-form layout="vertical">
          <a-form-item label="目标职位"><a-select v-model:value="form.positionId" placeholder="请选择 OPEN 状态职位"><a-select-option v-for="item in positions" :key="item.id" :value="item.id">{{ item.title }}</a-select-option></a-select></a-form-item>
          <div class="split-grid">
            <a-form-item label="技能权重"><a-input-number v-model:value="form.skillWeight" style="width: 100%" /></a-form-item>
            <a-form-item label="学历权重"><a-input-number v-model:value="form.eduWeight" style="width: 100%" /></a-form-item>
          </div>
          <a-form-item label="经验权重"><a-input-number v-model:value="form.expWeight" style="width: 100%" /></a-form-item>
          <div class="toolbar-actions"><a-button @click="loadConfig">重载配置</a-button><a-button type="primary" :loading="saving" @click="saveConfig">保存配置</a-button></div>
        </a-form>
      </a-card>
      <a-card class="glass-card" :bordered="false"><h3 style="margin-top: 0;">匹配执行</h3><p class="muted-text">调用 `/api/match/run`，展示综合分、技能分、学历分和经验分。</p><a-button type="primary" size="large" :loading="loading" @click="runMatch">开始匹配</a-button></a-card>
    </div>
    <a-card class="glass-card" :bordered="false" :loading="loading">
      <a-table :data-source="results" row-key="resumeId" :pagination="false">
        <a-table-column title="简历ID" data-index="resumeId" />
        <a-table-column title="姓名" data-index="resumeName" />
        <a-table-column title="手机号" data-index="resumePhone" />
        <a-table-column title="综合分" data-index="totalScore" />
        <a-table-column title="技能分" data-index="skillScore" />
        <a-table-column title="学历分" data-index="eduScore" />
        <a-table-column title="经验分" data-index="expScore" />
      </a-table>
    </a-card>
  </div>
</template>