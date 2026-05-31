<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { applicationApi } from '@/api/flow'
import { jobApi } from '@/api/jobs'
import { resumeApi } from '@/api/resumes'
import { pickList, pickPage } from '@/utils/common'

const statusOptionsMap: Record<string, Array<{ label: string; value: string }>> = {
  PENDING: [
    { label: '简历通过', value: 'RESUME_PASSED' },
    { label: '简历淘汰', value: 'RESUME_REJECTED' },
  ],
  RESUME_PASSED: [{ label: '待面试', value: 'INTERVIEW_WAITING' }],
  INTERVIEW_WAITING: [{ label: '面试中', value: 'INTERVIEWING' }],
  INTERVIEWING: [
    { label: '面试通过', value: 'INTERVIEW_PASSED' },
    { label: '面试淘汰', value: 'INTERVIEW_REJECTED' },
  ],
  INTERVIEW_PASSED: [{ label: '已录用', value: 'HIRED' }],
}

const loading = ref(false)
const modalOpen = ref(false)
const statusOpen = ref(false)
const logsOpen = ref(false)
const tableData = ref<any[]>([])
const total = ref(0)
const positions = ref<any[]>([])
const resumes = ref<any[]>([])
const logs = ref<any[]>([])
const currentRecord = ref<any>(null)
const query = reactive({ positionId: undefined as number | undefined, status: undefined as string | undefined, page: 1, size: 10 })
const form = reactive({ positionId: undefined as number | undefined, resumeId: undefined as number | undefined, remark: '' })
const statusForm = reactive({ status: 'RESUME_PASSED', remark: '' })

function getNextStatusOptions(status?: string) {
  return status ? statusOptionsMap[status] || [] : []
}

async function loadOptions() {
  const [jobsResponse, resumesResponse]: any = await Promise.all([jobApi.list({ page: 1, size: 999 }), resumeApi.list({ page: 1, size: 999 })])
  positions.value = pickPage(jobsResponse).list
  resumes.value = pickPage(resumesResponse).list
}

async function loadData() {
  loading.value = true
  try {
    const response: any = await applicationApi.list(query)
    const pageData = pickPage(response)
    tableData.value = pageData.list
    total.value = pageData.total
  } finally {
    loading.value = false
  }
}

async function saveApplication() {
  await applicationApi.create({ ...form })
  message.success('投递记录已创建')
  modalOpen.value = false
  await loadData()
}

function openStatus(record: any) {
  const options = getNextStatusOptions(record.status)
  if (!options.length) {
    message.warning('当前状态没有后续可流转操作')
    return
  }
  currentRecord.value = record
  statusForm.status = options[0].value
  statusForm.remark = ''
  statusOpen.value = true
}

async function saveStatus() {
  if (!currentRecord.value) return
  await applicationApi.updateStatus(currentRecord.value.id, { ...statusForm })
  message.success('状态已更新')
  statusOpen.value = false
  await loadData()
}

async function openLogs(record: any) {
  currentRecord.value = record
  const response: any = await applicationApi.logs(record.id)
  logs.value = pickList(response)
  logsOpen.value = true
}

onMounted(async () => {
  await Promise.all([loadOptions(), loadData()])
})
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>投递记录</h2><p>支持创建投递、状态流转和日志查看。</p></div>
    <a-card class="glass-card" :bordered="false"><div class="toolbar-row"><div class="toolbar-filters"><a-select v-model:value="query.positionId" allow-clear placeholder="按职位筛选" style="width: 220px"><a-select-option v-for="item in positions" :key="item.id" :value="item.id">{{ item.title }}</a-select-option></a-select><a-select v-model:value="query.status" allow-clear placeholder="按状态筛选" style="width: 180px"><a-select-option value="PENDING">待筛选</a-select-option><a-select-option value="RESUME_PASSED">简历通过</a-select-option><a-select-option value="INTERVIEW_WAITING">待面试</a-select-option><a-select-option value="INTERVIEWING">面试中</a-select-option><a-select-option value="INTERVIEW_PASSED">面试通过</a-select-option><a-select-option value="HIRED">已录用</a-select-option></a-select></div><div class="toolbar-actions"><a-button type="primary" @click="loadData">查询</a-button><a-button v-permission="'application:add'" @click="modalOpen = true">创建投递</a-button></div></div></a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="职位" data-index="positionTitle" />
        <a-table-column title="候选人" data-index="resumeName" />
        <a-table-column title="状态" data-index="status" />
        <a-table-column title="更新时间" data-index="operateTime" />
        <a-table-column title="备注" data-index="remark" />
        <a-table-column title="操作" width="180"><template #default="{ record }"><a-space><a-button v-if="getNextStatusOptions(record.status).length" type="link" v-permission="'application:edit'" @click="openStatus(record)">改状态</a-button><a-button type="link" @click="openLogs(record)">日志</a-button></a-space></template></a-table-column>
      </a-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 16px;"><a-pagination v-model:current="query.page" v-model:page-size="query.size" :total="total" :show-size-changer="true" @change="loadData" /></div>
    </a-card>
    <a-modal v-model:open="modalOpen" title="创建投递" @ok="saveApplication">
      <a-form layout="vertical">
        <a-form-item label="职位"><a-select v-model:value="form.positionId"><a-select-option v-for="item in positions" :key="item.id" :value="item.id">{{ item.title }}</a-select-option></a-select></a-form-item>
        <a-form-item label="简历"><a-select v-model:value="form.resumeId"><a-select-option v-for="item in resumes" :key="item.id" :value="item.id">{{ item.name || `简历-${item.id}` }}</a-select-option></a-select></a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="form.remark" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:open="statusOpen" title="变更投递状态" @ok="saveStatus">
      <a-form layout="vertical">
        <a-form-item label="目标状态"><a-select v-model:value="statusForm.status"><a-select-option v-for="item in getNextStatusOptions(currentRecord?.status)" :key="item.value" :value="item.value">{{ item.label }}</a-select-option></a-select></a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="statusForm.remark" /></a-form-item>
      </a-form>
    </a-modal>
    <a-drawer v-model:open="logsOpen" title="操作日志" width="520"><a-timeline><a-timeline-item v-for="item in logs" :key="item.id"><div><strong>{{ item.fromStatus || '初始' }} → {{ item.toStatus }}</strong></div><div class="muted-text">{{ item.operatorName || '-' }} · {{ item.createdTime }}</div><div class="muted-text">{{ item.remark || '-' }}</div></a-timeline-item></a-timeline></a-drawer>
  </div>
</template>