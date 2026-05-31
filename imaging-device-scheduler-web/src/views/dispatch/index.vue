<template>
  <div>
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="logs" tab="调度日志">
        <a-card :bordered="false">
          <a-space style="margin-bottom:16px">
            <a-input v-model:value="logQuery.keyword" placeholder="任务名称" style="width:180px" />
            <a-button type="primary" @click="loadLogs">查询</a-button>
          </a-space>
          <a-table :columns="logColumns" :data-source="logList" :loading="logLoading" row-key="id" :pagination="logPagination" @change="handleLogTableChange" />
        </a-card>
      </a-tab-pane>

      <a-tab-pane key="gantt" tab="甘特图">
        <a-card :bordered="false">
          <a-button @click="loadGantt" style="margin-bottom:16px">刷新</a-button>
          <div style="overflow-x:auto">
            <div v-for="item in ganttData" :key="item.deviceId" style="display:flex;align-items:center;margin-bottom:8px">
              <div style="width:120px;flex-shrink:0;font-size:13px">{{ item.deviceName }}</div>
              <div style="flex:1;position:relative;height:28px;background:#f0f0f0;border-radius:4px">
                <div
                  v-for="task in item.tasks" :key="task.taskId"
                  :style="getGanttBarStyle(task)"
                  class="gantt-bar"
                  :title="task.taskName"
                >{{ task.taskName }}</div>
              </div>
            </div>
          </div>
        </a-card>
      </a-tab-pane>

      <a-tab-pane key="config" tab="调度配置">
        <a-card :bordered="false" style="max-width:600px">
          <a-form :model="config" :label-col="{ span: 8 }" :wrapper-col="{ span: 14 }">
            <a-form-item label="最大设备数/任务">
              <a-input-number v-model:value="config.maxDevicesPerTask" :min="1" style="width:100%" />
            </a-form-item>
            <a-form-item label="任务超时时间(分钟)">
              <a-input-number v-model:value="config.taskTimeoutMinutes" :min="1" style="width:100%" />
            </a-form-item>
            <a-form-item label="自动调度">
              <a-switch v-model:checked="autoDispatch" />
            </a-form-item>
            <a-form-item label="调度策略">
              <a-select v-model:value="config.dispatchStrategy">
                <a-select-option value="FIFO">先进先出</a-select-option>
                <a-select-option value="PRIORITY">优先级优先</a-select-option>
                <a-select-option value="LOAD_BALANCE">负载均衡</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="预警阈值(分钟)">
              <a-input-number v-model:value="config.alertThresholdMinutes" :min="1" style="width:100%" />
            </a-form-item>
            <a-form-item :wrapper-col="{ offset: 8 }">
              <a-button type="primary" :loading="savingConfig" @click="handleSaveConfig">保存配置</a-button>
            </a-form-item>
          </a-form>
        </a-card>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getDispatchLogsApi, getGanttDataApi, getDispatchConfigApi, updateDispatchConfigApi } from '@/api/dispatch'

const activeTab = ref('logs')

// Logs
const logList = ref<any[]>([])
const logLoading = ref(false)
const logPagination = reactive({ current: 1, pageSize: 10, total: 0 })
const logQuery = reactive({ keyword: '' })

const logColumns = [
  { title: '任务名称', dataIndex: 'taskName' },
  { title: '操作', dataIndex: 'actionDesc' },
  { title: '涉及设备', dataIndex: 'deviceIds', ellipsis: true },
  { title: '操作人', dataIndex: 'operatorName', width: 100 },
  { title: '时间', dataIndex: 'createdAt', width: 165 },
]

async function loadLogs() {
  logLoading.value = true
  try {
    const res: any = await getDispatchLogsApi({ ...logQuery, page: logPagination.current, size: logPagination.pageSize })
    const d = res.data
    logList.value = d.records || d || []
    logPagination.total = d.total || logList.value.length
  } finally { logLoading.value = false }
}

function handleLogTableChange(p: any) { logPagination.current = p.current; logPagination.pageSize = p.pageSize; loadLogs() }

// Gantt
const ganttData = ref<any[]>([])
async function loadGantt() {
  const res: any = await getGanttDataApi()
  ganttData.value = res.data || []
}

function getGanttBarStyle(task: any) {
  const colors = ['#1677ff', '#52c41a', '#fa8c16', '#722ed1', '#13c2c2']
  return {
    position: 'absolute',
    left: `${task.leftPercent || 10}%`,
    width: `${task.widthPercent || 30}%`,
    height: '100%',
    background: colors[task.taskId % colors.length],
    borderRadius: '3px',
    color: '#fff',
    fontSize: '11px',
    padding: '0 4px',
    display: 'flex',
    alignItems: 'center',
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    cursor: 'pointer',
  }
}

// Config
const config = reactive<any>({
  maxDevicesPerTask: 5,
  taskTimeoutMinutes: 60,
  autoDispatchEnabled: 1,
  dispatchStrategy: 'PRIORITY',
  alertThresholdMinutes: 10,
})
const autoDispatch = computed({
  get: () => config.autoDispatchEnabled === 1,
  set: (v: boolean) => { config.autoDispatchEnabled = v ? 1 : 0 },
})
const savingConfig = ref(false)

async function loadConfig() {
  const res: any = await getDispatchConfigApi()
  Object.assign(config, res.data || {})
}

async function handleSaveConfig() {
  savingConfig.value = true
  try {
    await updateDispatchConfigApi(config)
    message.success('配置保存成功')
  } finally { savingConfig.value = false }
}

onMounted(() => { loadLogs(); loadGantt(); loadConfig() })
</script>

<style scoped>
.gantt-bar:hover { opacity: 0.85; }
</style>
