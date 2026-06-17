<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { applicationApi, interviewApi } from '@/api/flow'
import { pickPage } from '@/utils/common'

const interviewableStatuses = ['INTERVIEW_WAITING', 'INTERVIEWING']

const loading = ref(false)
const modalOpen = ref(false)
const resultOpen = ref(false)
const tableData = ref<any[]>([])
const total = ref(0)
const applications = ref<any[]>([])
const editingId = ref<number | null>(null)
const currentRecord = ref<any>(null)
const query = reactive({ interviewer: '', page: 1, size: 10 })
const form = reactive({ applicationId: undefined as number | undefined, interviewTime: '', interviewer: '', location: '' })
const resultForm = reactive({ score: 4, comment: '', result: 'PASS' })
const selectableApplications = computed(() => applications.value.filter((item: any) => interviewableStatuses.includes(item.status) || item.id === form.applicationId))

function resetForm() {
  Object.assign(form, { applicationId: undefined, interviewTime: '', interviewer: '', location: '' })
}

async function loadApplications() {
  const response: any = await applicationApi.list({ page: 1, size: 999 })
  applications.value = pickPage(response).list
}

async function loadData() {
  loading.value = true
  try {
    const response: any = await interviewApi.list(query)
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
  Object.assign(form, { applicationId: record.applicationId, interviewTime: record.interviewTime, interviewer: record.interviewer, location: record.location })
  modalOpen.value = true
}

async function saveInterview() {
  if (editingId.value) {
    await interviewApi.update(editingId.value, { ...form })
  } else {
    await interviewApi.create({ ...form })
  }
  message.success('面试记录已保存')
  modalOpen.value = false
  await Promise.all([loadApplications(), loadData()])
}

function openResult(record: any) {
  currentRecord.value = record
  Object.assign(resultForm, { score: record.score || 4, comment: record.comment || '', result: record.result || 'PASS' })
  resultOpen.value = true
}

async function saveResult() {
  if (!currentRecord.value) return
  await interviewApi.updateResult(currentRecord.value.id, { ...resultForm })
  message.success('面试结果已提交')
  resultOpen.value = false
  await loadData()
}

onMounted(async () => {
  await Promise.all([loadApplications(), loadData()])
})
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>面试管理</h2><p>支持面试安排、编辑以及评价结果回填。</p></div>
    <a-card class="glass-card" :bordered="false"><div class="toolbar-row"><div class="toolbar-filters"><a-input v-model:value="query.interviewer" placeholder="面试官" style="width: 180px" /></div><div class="toolbar-actions"><a-button type="primary" @click="loadData">查询</a-button><a-button v-permission="'interview:add'" @click="openCreate">新增面试</a-button></div></div></a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="投递ID" data-index="applicationId" />
        <a-table-column title="面试时间" data-index="interviewTime" />
        <a-table-column title="面试官" data-index="interviewer" />
        <a-table-column title="地点/链接" data-index="location" />
        <a-table-column title="结果" data-index="result" />
        <a-table-column title="操作" width="180"><template #default="{ record }"><a-space><a-button type="link" v-permission="'interview:edit'" @click="openEdit(record)">编辑</a-button><a-button type="link" v-permission="'interview:edit'" @click="openResult(record)">填写结果</a-button></a-space></template></a-table-column>
      </a-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 16px;"><a-pagination v-model:current="query.page" v-model:page-size="query.size" :total="total" :show-size-changer="true" @change="loadData" /></div>
    </a-card>
    <a-modal v-model:open="modalOpen" :title="editingId ? '编辑面试' : '新增面试'" @ok="saveInterview">
      <a-form layout="vertical">
        <a-form-item label="投递记录"><a-select v-model:value="form.applicationId"><a-select-option v-for="item in selectableApplications" :key="item.id" :value="item.id">{{ item.positionTitle }} / {{ item.resumeName }}</a-select-option></a-select></a-form-item>
        <a-form-item label="面试时间"><a-input v-model:value="form.interviewTime" placeholder="2026-05-27 19:30:00" /></a-form-item>
        <a-form-item label="面试官"><a-input v-model:value="form.interviewer" /></a-form-item>
        <a-form-item label="地点或会议链接"><a-input v-model:value="form.location" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:open="resultOpen" title="填写面试结果" @ok="saveResult">
      <a-form layout="vertical">
        <a-form-item label="评分"><a-input-number v-model:value="resultForm.score" :min="1" :max="5" style="width: 100%" /></a-form-item>
        <a-form-item label="结果"><a-select v-model:value="resultForm.result"><a-select-option value="PASS">通过</a-select-option><a-select-option value="REJECT">淘汰</a-select-option></a-select></a-form-item>
        <a-form-item label="评价"><a-textarea v-model:value="resultForm.comment" :auto-size="{ minRows: 4, maxRows: 7 }" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>