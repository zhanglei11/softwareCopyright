<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { jobApi } from '@/api/jobs'
import { pickPage } from '@/utils/common'

const loading = ref(false)
const saving = ref(false)
const modalOpen = ref(false)
const editingId = ref<number | null>(null)
const tableData = ref<any[]>([])
const total = ref(0)
const query = reactive({ title: '', status: undefined as string | undefined, page: 1, size: 10 })
const form = reactive({ title: '', department: '', jobType: 'FULL_TIME', location: '', salaryMin: 10000, salaryMax: 20000, eduRequire: 'BACHELOR', expRequire: 1, description: '', skillsText: '' })

function resetForm() {
  Object.assign(form, { title: '', department: '', jobType: 'FULL_TIME', location: '', salaryMin: 10000, salaryMax: 20000, eduRequire: 'BACHELOR', expRequire: 1, description: '', skillsText: '' })
}

async function loadData() {
  loading.value = true
  try {
    const response: any = await jobApi.list(query)
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
  Object.assign(form, { title: record.title, department: record.department, jobType: record.jobType, location: record.location, salaryMin: record.salaryMin, salaryMax: record.salaryMax, eduRequire: record.eduRequire, expRequire: record.expRequire, description: record.description, skillsText: '' })
  modalOpen.value = true
}

function buildPayload() {
  return { ...form, skillTags: form.skillsText.split(',').map((item) => item.trim()).filter(Boolean) }
}

async function saveJob() {
  saving.value = true
  try {
    if (editingId.value) {
      await jobApi.update(editingId.value, buildPayload())
    } else {
      await jobApi.create(buildPayload())
    }
    message.success('职位已保存')
    modalOpen.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function publishJob(id: number) {
  await jobApi.publish(id)
  message.success('职位已发布')
  await loadData()
}

async function closeJob(id: number) {
  await jobApi.close(id)
  message.success('职位已关闭')
  await loadData()
}

async function removeJob(id: number) {
  await jobApi.remove(id)
  message.success('职位已删除')
  await loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page-shell">
    <div class="page-hero"><h2>职位管理</h2><p>对接职位列表、创建、编辑、发布、关闭和删除接口。</p></div>
    <a-card class="glass-card" :bordered="false"><div class="toolbar-row"><div class="toolbar-filters"><a-input v-model:value="query.title" placeholder="职位名称" style="width: 220px" /><a-select v-model:value="query.status" allow-clear placeholder="职位状态" style="width: 160px"><a-select-option value="DRAFT">草稿</a-select-option><a-select-option value="OPEN">发布中</a-select-option><a-select-option value="CLOSED">已关闭</a-select-option></a-select></div><div class="toolbar-actions"><a-button type="primary" @click="loadData">查询</a-button><a-button v-permission="'job:job:add'" @click="openCreate">新增职位</a-button></div></div></a-card>
    <a-card class="glass-card" :bordered="false">
      <a-table :data-source="tableData" :loading="loading" row-key="id" :pagination="false">
        <a-table-column title="职位名称" data-index="title" />
        <a-table-column title="部门" data-index="department" />
        <a-table-column title="类型" data-index="jobType" />
        <a-table-column title="地点" data-index="location" />
        <a-table-column title="薪资"><template #default="{ record }">{{ record.salaryMin }} - {{ record.salaryMax }}</template></a-table-column>
        <a-table-column title="状态"><template #default="{ record }"><a-tag :color="record.status === 'OPEN' ? 'green' : record.status === 'CLOSED' ? 'default' : 'gold'">{{ record.status }}</a-tag></template></a-table-column>
        <a-table-column title="操作" width="260"><template #default="{ record }"><a-space>
          <a-button type="link" v-permission="'job:job:edit'" @click="openEdit(record)">编辑</a-button>
          <a-button type="link" v-if="record.status !== 'OPEN'" v-permission="'job:job:publish'" @click="publishJob(record.id)">发布</a-button>
          <a-button type="link" v-if="record.status === 'OPEN'" v-permission="'job:job:close'" @click="closeJob(record.id)">关闭</a-button>
          <a-popconfirm title="确认删除该职位吗？" @confirm="removeJob(record.id)"><a-button type="link" danger v-permission="'job:job:delete'">删除</a-button></a-popconfirm>
        </a-space></template></a-table-column>
      </a-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 16px;"><a-pagination v-model:current="query.page" v-model:page-size="query.size" :total="total" :show-size-changer="true" @change="loadData" /></div>
    </a-card>
    <a-modal v-model:open="modalOpen" :title="editingId ? '编辑职位' : '新增职位'" width="760" :confirm-loading="saving" @ok="saveJob">
      <a-form layout="vertical">
        <div class="split-grid">
          <a-form-item label="职位名称"><a-input v-model:value="form.title" /></a-form-item>
          <a-form-item label="所属部门"><a-input v-model:value="form.department" /></a-form-item>
          <a-form-item label="岗位类型"><a-select v-model:value="form.jobType"><a-select-option value="FULL_TIME">全职</a-select-option><a-select-option value="PART_TIME">兼职</a-select-option><a-select-option value="INTERN">实习</a-select-option></a-select></a-form-item>
          <a-form-item label="工作地点"><a-input v-model:value="form.location" /></a-form-item>
          <a-form-item label="薪资下限"><a-input-number v-model:value="form.salaryMin" style="width: 100%" /></a-form-item>
          <a-form-item label="薪资上限"><a-input-number v-model:value="form.salaryMax" style="width: 100%" /></a-form-item>
          <a-form-item label="学历要求"><a-select v-model:value="form.eduRequire"><a-select-option value="HIGH_SCHOOL">高中</a-select-option><a-select-option value="ASSOCIATE">大专</a-select-option><a-select-option value="BACHELOR">本科</a-select-option><a-select-option value="MASTER">硕士</a-select-option><a-select-option value="DOCTOR">博士</a-select-option></a-select></a-form-item>
          <a-form-item label="经验年限"><a-input-number v-model:value="form.expRequire" style="width: 100%" /></a-form-item>
        </div>
        <a-form-item label="技能标签（英文逗号分隔）"><a-input v-model:value="form.skillsText" /></a-form-item>
        <a-form-item label="职位描述"><a-textarea v-model:value="form.description" :auto-size="{ minRows: 4, maxRows: 7 }" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>