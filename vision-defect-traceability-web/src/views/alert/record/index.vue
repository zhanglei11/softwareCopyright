<template>
  <a-card title="告警记录">
    <a-table :columns="columns" :data-source="tableData" :loading="loading"
      :pagination="pagination" row-key="id" @change="handleTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'alertTime'">
          {{ record.alertTime }}
        </template>
        <template v-if="column.key === 'alertLevel'">
          <a-tag :color="['','blue','orange','red'][record.alertLevel] || 'default'">{{ record.alertLevelLabel }}</a-tag>
        </template>
        <template v-if="column.key === 'handleStatus'">
          <a-tag :color="record.handleStatus === 1 ? 'green' : 'orange'">{{ record.handleStatus === 1 ? '已处理' : '待处理' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-button v-if="record.handleStatus === 0" type="link" size="small" @click="handleProcess(record)">处理</a-button>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalVisible" title="处理告警" @ok="submitProcess" :confirm-loading="submitLoading">
      <a-form :model="processForm" layout="vertical">
        <a-form-item label="处理说明" required>
          <a-textarea v-model:value="processForm.handleRemark" :rows="4" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getAlertRecordList, handleAlert } from '@/api/alert/record'
import { getAlertRuleList } from '@/api/alert/rule'
import { getLineList } from '@/api/line'
import type { AlertRecord } from '@/types'
const loading = ref(false)
const tableData = ref<AlertRecord[]>([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const rulesMap = ref<Record<number, any>>({})
const linesMap = ref<Record<number, any>>({})
const ALERT_LEVEL_MAP: Record<number, string> = { 1: '提示', 2: '重要', 3: '紧急' }
const columns = [
  { title: '告警规则', dataIndex: 'ruleName' },
  { title: '告警内容', dataIndex: 'alertContent', ellipsis: true },
  { title: '告警等级', key: 'alertLevel' },
  { title: '处理状态', key: 'handleStatus' },
  { title: '产线', dataIndex: 'lineName' },
  { title: '告警时间', key: 'alertTime' },
  { title: '操作', key: 'action', width: 80 },
]
const loadData = async () => {
  loading.value = true
  try {
    const res = await getAlertRecordList({ page: pagination.current, pageSize: pagination.pageSize })
    const d = (res as any).data
    const rows = d.rows ?? []
    tableData.value = rows.map((row: any) => {
      const rule = rulesMap.value[row.ruleId] || {}
      const line = linesMap.value[rule.lineId] || {}
      return {
        ...row,
        ruleName: rule.ruleName || '-',
        alertLevel: rule.alertLevel,
        alertLevelLabel: ALERT_LEVEL_MAP[rule.alertLevel] || '-',
        lineName: line.lineName || '-',
        alertTime: row.alertTime?.replace('T', ' ').slice(0, 19) || '-',
      }
    })
    pagination.total = d.total ?? 0
  } catch {} finally { loading.value = false }
}
const handleTableChange = (p: any) => { pagination.current = p.current; pagination.pageSize = p.pageSize; loadData() }
const modalVisible = ref(false)
const submitLoading = ref(false)
const currentId = ref<number>(0)
const processForm = reactive({ handleRemark: '' })
const handleProcess = (record: AlertRecord) => { currentId.value = record.id; processForm.handleRemark = ''; modalVisible.value = true }
const submitProcess = async () => {
  if (!processForm.handleRemark.trim()) { message.warning('请输入处理说明'); return }
  submitLoading.value = true
  try { await handleAlert(currentId.value, processForm); message.success('处理成功'); modalVisible.value = false; loadData() } catch {} finally { submitLoading.value = false }
}
onMounted(async () => {
  try {
    const [rulesRes, linesRes] = await Promise.all([getAlertRuleList({}), getLineList({})])
    const rules = (rulesRes as any).data?.rows ?? (rulesRes as any).rows ?? []
    const lines = (linesRes as any).data?.rows ?? (linesRes as any).rows ?? []
    rulesMap.value = Object.fromEntries(rules.map((r: any) => [r.id, r]))
    linesMap.value = Object.fromEntries(lines.map((l: any) => [l.id, l]))
  } catch {}
  loadData()
})
</script>
