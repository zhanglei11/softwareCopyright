<template>
  <div class="page-container">
    <!-- 概览 -->
    <a-card title="存储概览" :bordered="false" style="margin-bottom:16px;border-radius:8px">
      <a-row :gutter="16" v-if="overview">
        <a-col :span="6"><a-statistic title="总文件数" :value="overview.totalFiles" /></a-col>
        <a-col :span="6"><a-statistic title="已使用(GB)" :value="((overview.usedSize || 0) / 1073741824).toFixed(2)" /></a-col>
        <a-col :span="6"><a-statistic title="总容量(GB)" :value="((overview.totalSize || 0) / 1073741824).toFixed(2)" /></a-col>
        <a-col :span="6"><a-statistic title="使用率" :value="overview.usagePercent" suffix="%" /></a-col>
      </a-row>
    </a-card>

    <a-tabs v-model:activeKey="activeTab">
      <!-- 清理规则 -->
      <a-tab-pane key="rules" tab="清理规则">
        <div class="table-toolbar">
          <span>清理规则</span>
          <a-button type="primary" @click="showAddRule"><PlusOutlined /> 新增</a-button>
        </div>
        <a-table :dataSource="rules" :columns="ruleColumns" :loading="ruleLoading" rowKey="id" size="middle" style="background:#fff;border-radius:8px">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'enabled'">
              <a-switch :checked="record.enabled" size="small" disabled />
            </template>
            <template v-if="column.key === 'action'">
              <a-space>
                <a-button type="link" size="small" @click="executeCleanNow(record.id!)">立即清理</a-button>
                <a-button type="link" size="small" @click="showEditRule(record)">编辑</a-button>
                <a-popconfirm title="确认删除？" @confirm="deleteRule(record.id!)">
                  <a-button type="link" size="small" danger>删除</a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </a-table>
      </a-tab-pane>

      <!-- 清理日志 -->
      <a-tab-pane key="logs" tab="清理日志">
        <a-table :dataSource="cleanLogs" :columns="logColumns" :loading="logLoading" rowKey="id" :pagination="logPagination" @change="handleLogPageChange" size="middle" style="background:#fff;border-radius:8px">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'status'">
              <a-tag :color="record.status === 'SUCCESS' ? 'success' : 'error'">{{ record.status }}</a-tag>
            </template>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <!-- 规则弹窗 -->
    <a-modal v-model:open="ruleModalVisible" :title="ruleForm.id ? '编辑规则' : '新增规则'" @ok="saveRule" :confirm-loading="saving" width="560px">
      <a-form :model="ruleForm" :rules="ruleFormRules" ref="ruleFormRef" :label-col="{span:7}">
        <a-form-item label="规则名称" name="name"><a-input v-model:value="ruleForm.name" /></a-form-item>
        <a-form-item label="存储路径" name="storagePath"><a-input v-model:value="ruleForm.storagePath" /></a-form-item>
        <a-form-item label="文件保留天数" name="retentionDays"><a-input-number v-model:value="ruleForm.retentionDays" style="width:100%" /></a-form-item>
        <a-form-item label="最大存储(GB)"><a-input-number v-model:value="ruleForm.maxStorageGb" style="width:100%" /></a-form-item>
        <a-form-item label="Cron表达式"><a-input v-model:value="ruleForm.cronExpression" /></a-form-item>
        <a-form-item label="启用"><a-switch v-model:checked="ruleForm.enabled" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="ruleForm.description" :rows="2" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getStorageOverview, getCleanRuleList as getCleanRules, addCleanRule, updateCleanRule, deleteCleanRule, executeClean, getCleanLogs } from '@/api/storage'
import type { StorageCleanRule, StorageOverview } from '@/types'

const activeTab = ref('rules')
const overview = ref<StorageOverview | null>(null)

// Rules
const ruleLoading = ref(false)
const saving = ref(false)
const rules = ref<StorageCleanRule[]>([])
const ruleModalVisible = ref(false)
const ruleFormRef = ref()
const ruleForm = reactive<Partial<StorageCleanRule>>({})
const ruleFormRules = {
  name: [{ required: true, message: '请输入规则名称' }],
  storagePath: [{ required: true, message: '请输入存储路径' }],
  retentionDays: [{ required: true, message: '请输入保留天数' }],
}
const ruleColumns = [
  { title: '规则名称', dataIndex: 'name' },
  { title: '存储路径', dataIndex: 'storagePath', ellipsis: true },
  { title: '保留天数', dataIndex: 'retentionDays' },
  { title: '最大(GB)', dataIndex: 'maxStorageGb' },
  { title: '启用', key: 'enabled' },
  { title: '操作', key: 'action', width: 200 },
]
const loadRules = async () => {
  ruleLoading.value = true
  try { const res = await getCleanRules(); rules.value = res.data.rows ?? res.data }
  finally { ruleLoading.value = false }
}
const showAddRule = () => { Object.assign(ruleForm, { id: undefined, name: '', storagePath: '', retentionDays: 30, maxStorageGb: undefined, cronExpression: '', enabled: true, description: '' }); ruleModalVisible.value = true }
const showEditRule = (r: StorageCleanRule) => { Object.assign(ruleForm, { ...r }); ruleModalVisible.value = true }
const saveRule = async () => {
  await ruleFormRef.value?.validate(); saving.value = true
  try {
    if (ruleForm.id) await updateCleanRule(ruleForm.id, ruleForm as StorageCleanRule)
    else await addCleanRule(ruleForm as StorageCleanRule)
    message.success('保存成功'); ruleModalVisible.value = false; loadRules()
  } finally { saving.value = false }
}
const deleteRule = async (id: number) => { await deleteCleanRule(id); message.success('删除成功'); loadRules() }
const executeCleanNow = async (id: number) => { await executeClean(id); message.success('清理任务已触发') }

// Logs
const logLoading = ref(false)
const cleanLogs = ref<Record<string, unknown>[]>([])
const logPagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const logColumns = [
  { title: '规则ID', dataIndex: 'ruleId' },
  { title: '清理文件数', dataIndex: 'cleanedFiles' },
  { title: '释放空间(MB)', dataIndex: 'freedSizeMb' },
  { title: '状态', key: 'status', dataIndex: 'status' },
  { title: '执行时间', dataIndex: 'executeTime' },
]
const loadLogs = async () => {
  logLoading.value = true
  try {
    const res = await getCleanLogs({ pageNum: logPagination.current, pageSize: logPagination.pageSize })
    cleanLogs.value = res.data.rows; logPagination.total = res.data.total
  } finally { logLoading.value = false }
}
const handleLogPageChange = (p: typeof logPagination) => { logPagination.current = p.current; logPagination.pageSize = p.pageSize; loadLogs() }

onMounted(async () => {
  const res = await getStorageOverview()
  overview.value = res.data
  loadRules(); loadLogs()
})
</script>
