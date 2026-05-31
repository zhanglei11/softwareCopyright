<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { resumeApi } from '@/api/resumes'
import { labelizeSource, pickPage } from '@/utils/common'

const loading = ref(false)
const saving = ref(false)
const modalOpen = ref(false)
const detailOpen = ref(false)
const editingId = ref<number | null>(null)
const tableData = ref<any[]>([])
const total = ref(0)
const detail = ref<any>(null)
const query = reactive({ name: '', phone: '', source: undefined as string | undefined, page: 1, size: 10 })
const form = reactive({ name: '', phone: '', email: '', city: '', desiredPosition: '', desiredCity: '', jobStatus: '', selfIntro: '', source: 'MANUAL', skillsText: '' })

function resetForm() {
  Object.assign(form, { name: '', phone: '', email: '', city: '', desiredPosition: '', desiredCity: '', jobStatus: '', selfIntro: '', source: 'MANUAL', skillsText: '' })
}

async function loadData() {
  loading.value = true
  try {
    const response: any = await resumeApi.list(query)
    const pageData = pickPage(response)
    tableData.value = pageData.list
    total.value = pageData.total
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingId.value = null
  resetForm()
  modalOpen.value = true
}

function openEdit(record: any) {
  editingId.value = record.id
  Object.assign(form, { name: record.name, phone: record.phone, email: record.email, city: record.city, desiredPosition: record.desiredPosition, desiredCity: record.desiredCity, jobStatus: record.jobStatus, selfIntro: record.selfIntro, source: record.source || 'MANUAL', skillsText: '' })
  modalOpen.value = true
}

async function saveResume() {
  saving.value = true
  try {
    const payload = { ...form, skills: form.skillsText.split(',').map((item) => item.trim()).filter(Boolean), educations: [], workExps: [] }
    if (editingId.value) {
      await resumeApi.update(editingId.value, payload)
    } else {
      await resumeApi.create(payload)
    }
    message.success('简历已保存')
    modalOpen.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function openDetail(id: number) {
  const response: any = await resumeApi.get(id)
  detail.value = response.data
  detailOpen.value = true
}

async function removeResume(id: number) {
  await resumeApi.remove(id)
  message.success('简历已删除')
  await loadData()
}

async function uploadResume(options: any) {
  try {
    await resumeApi.upload(options.file as File)
    message.success('简历文件已上传')
    options.onSuccess({}, options.file)
    await loadData()
  } catch (error) {
    options.onError(error)
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>简历管理</h2><p>支持结构化录入、文件上传、编辑和查看详情。</p></div>
    <a-card class="glass-card" :bordered="false"><div class="toolbar-row"><div class="toolbar-filters"><a-input v-model:value="query.name" placeholder="姓名" style="width: 180px" /><a-input v-model:value="query.phone" placeholder="手机号" style="width: 180px" /><a-select v-model:value="query.source" allow-clear placeholder="来源" style="width: 160px"><a-select-option value="MANUAL">手动录入</a-select-option><a-select-option value="FILE">文件上传</a-select-option><a-select-option value="THIRD_PARTY">第三方</a-select-option></a-select></div><div class="toolbar-actions"><a-button type="primary" @click="loadData">查询</a-button><a-upload :show-upload-list="false" :custom-request="uploadResume"><a-button v-permission="'resume:resume:add'">上传简历文件</a-button></a-upload><a-button v-permission="'resume:resume:add'" @click="openCreate">新增简历</a-button></div></div></a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="姓名" data-index="name" />
        <a-table-column title="手机号" data-index="phone" />
        <a-table-column title="邮箱" data-index="email" />
        <a-table-column title="期望职位" data-index="desiredPosition" />
        <a-table-column title="来源"><template #default="{ record }">{{ labelizeSource(record.source) }}</template></a-table-column>
        <a-table-column title="解析结果"><template #default="{ record }"><a-tag :color="record.parseSuccess ? 'green' : 'default'">{{ record.parseSuccess ? '成功' : '待处理/失败' }}</a-tag></template></a-table-column>
        <a-table-column title="操作" width="220"><template #default="{ record }"><a-space>
          <a-button type="link" @click="openDetail(record.id)">详情</a-button>
          <a-button type="link" v-permission="'resume:resume:edit'" @click="openEdit(record)">编辑</a-button>
          <a-popconfirm title="确认删除该简历吗？" @confirm="removeResume(record.id)"><a-button type="link" danger v-permission="'resume:resume:delete'">删除</a-button></a-popconfirm>
        </a-space></template></a-table-column>
      </a-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 16px;"><a-pagination v-model:current="query.page" v-model:page-size="query.size" :total="total" :show-size-changer="true" @change="loadData" /></div>
    </a-card>
    <a-modal v-model:open="modalOpen" :title="editingId ? '编辑简历' : '新增简历'" width="760" :confirm-loading="saving" @ok="saveResume">
      <a-form layout="vertical">
        <div class="split-grid">
          <a-form-item label="姓名"><a-input v-model:value="form.name" /></a-form-item>
          <a-form-item label="手机号"><a-input v-model:value="form.phone" /></a-form-item>
          <a-form-item label="邮箱"><a-input v-model:value="form.email" /></a-form-item>
          <a-form-item label="居住城市"><a-input v-model:value="form.city" /></a-form-item>
          <a-form-item label="期望职位"><a-input v-model:value="form.desiredPosition" /></a-form-item>
          <a-form-item label="期望城市"><a-input v-model:value="form.desiredCity" /></a-form-item>
        </div>
        <a-form-item label="来源"><a-select v-model:value="form.source"><a-select-option value="MANUAL">手动录入</a-select-option><a-select-option value="FILE">文件上传</a-select-option><a-select-option value="THIRD_PARTY">第三方</a-select-option></a-select></a-form-item>
        <a-form-item label="技能标签（英文逗号分隔）"><a-input v-model:value="form.skillsText" /></a-form-item>
        <a-form-item label="自我介绍"><a-textarea v-model:value="form.selfIntro" :auto-size="{ minRows: 4, maxRows: 7 }" /></a-form-item>
      </a-form>
    </a-modal>
    <a-drawer v-model:open="detailOpen" title="简历详情" width="520">
      <a-descriptions :column="1" bordered>
        <a-descriptions-item label="姓名">{{ detail?.name }}</a-descriptions-item>
        <a-descriptions-item label="手机号">{{ detail?.phone }}</a-descriptions-item>
        <a-descriptions-item label="邮箱">{{ detail?.email }}</a-descriptions-item>
        <a-descriptions-item label="期望职位">{{ detail?.desiredPosition }}</a-descriptions-item>
        <a-descriptions-item label="来源">{{ labelizeSource(detail?.source) }}</a-descriptions-item>
        <a-descriptions-item label="自我介绍">{{ detail?.selfIntro || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-drawer>
  </div>
</template>