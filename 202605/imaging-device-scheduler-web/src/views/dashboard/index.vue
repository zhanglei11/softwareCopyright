<template>
  <div>
    <a-row :gutter="16" style="margin-bottom:16px">
      <a-col :span="6" v-for="item in overviewCards" :key="item.label">
        <a-card :bordered="false" style="border-radius:8px">
          <a-statistic :title="item.label" :value="item.value" :value-style="{ color: item.color }">
            <template #prefix><component :is="item.icon" /></template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="超时预警任务" :bordered="false" style="border-radius:8px">
          <a-table
            :columns="alertColumns"
            :data-source="alertList"
            :pagination="false"
            size="small"
            row-key="id"
          />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="最近调度日志" :bordered="false" style="border-radius:8px">
          <a-table
            :columns="logColumns"
            :data-source="recentLogs"
            :pagination="false"
            size="small"
            row-key="id"
          />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { DesktopOutlined, OrderedListOutlined, CheckCircleOutlined, WarningOutlined } from '@ant-design/icons-vue'
import { getDispatchOverviewApi, getTimeoutAlertsApi, getDispatchLogsApi } from '@/api/dispatch'

const overview = ref<any>({})
const alertList = ref<any[]>([])
const recentLogs = ref<any[]>([])

const overviewCards = ref([
  { label: '总设备数', value: 0, color: '#1677ff', icon: DesktopOutlined },
  { label: '在线设备', value: 0, color: '#52c41a', icon: CheckCircleOutlined },
  { label: '今日任务', value: 0, color: '#fa8c16', icon: OrderedListOutlined },
  { label: '超时预警', value: 0, color: '#ff4d4f', icon: WarningOutlined },
])

const alertColumns = [
  { title: '任务名称', dataIndex: 'taskName', ellipsis: true },
  { title: '计划结束', dataIndex: 'planEndTime', width: 150 },
  { title: '超时(分)', dataIndex: 'overMinutes', width: 80 },
]

const logColumns = [
  { title: '操作', dataIndex: 'actionDesc', ellipsis: true },
  { title: '任务', dataIndex: 'taskName', width: 120, ellipsis: true },
  { title: '操作人', dataIndex: 'operatorName', width: 80 },
]

async function loadData() {
  try {
    const [ovRes, alertRes, logRes] = await Promise.all([
      getDispatchOverviewApi(),
      getTimeoutAlertsApi(),
      getDispatchLogsApi({ size: 5 }),
    ])
    const d = ovRes.data || {}
    overviewCards.value[0].value = d.totalDevices ?? 0
    overviewCards.value[1].value = d.onlineDevices ?? 0
    overviewCards.value[2].value = d.todayTasks ?? 0
    overviewCards.value[3].value = d.timeoutAlerts ?? 0
    alertList.value = alertRes.data || []
    recentLogs.value = (logRes.data?.records || logRes.data || []).slice(0, 5)
  } catch {}
}

onMounted(loadData)
</script>
