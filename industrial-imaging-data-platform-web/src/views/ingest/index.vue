<template>
  <div class="page-container">
    <a-tabs v-model:activeKey="activeTab">
      <!-- 采集任务 -->
      <a-tab-pane key="tasks" tab="采集任务">
        <div class="search-form">
          <a-form layout="inline" :model="taskQuery">
            <a-form-item label="任务名"><a-input v-model:value="taskQuery.name" allowClear placeholder="任务名称" /></a-form-item>
            <a-form-item label="状态">
              <a-select v-model:value="taskQuery.status" style="width:120px" allowClear placeholder="全部">
                <a-select-option value="ENABLED">启用</a-select-option>
                <a-select-option value="DISABLED">停用</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="loadTasks">查询</a-button>
              <a-button style="margin-left:8px" @click="() => { taskQuery.name=''; taskQuery.status=undefined; loadTasks() }">重置</a-button>
            </a-form-item>
          </a-form>
        </div>
        <div class="table-toolbar">
          <span>采集任务列表</span>
          <a-button type="primary" @click="showAddTask"><PlusOutlined /> 新增</a-button>
        </div>
        <a-table :dataSource="tasks" :columns="taskColumns" :loading="taskLoading" rowKey="id" :pagination="taskPagination" @change="handleTaskPageChange" size="middle" style="background:#fff;border-radius:8px">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'enabled'">
              <a-switch :checked="record.enabled" size="small" disabled />
            </template>
            <template v-if="column.key === 'action'">
              <a-space>
                <a-button type="link" size="small" @click="triggerTask(record.id!)">立即触发</a-button>
                <a-button type="link" size="small" @click="showEditTask(record)">编辑</a-button>
                <a-popconfirm title="确认删除？" @confirm="deleteTask(record.id!)">
                  <a-button type="link" size="small" danger>删除</a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </a-table>
      </a-tab-pane>

      <!-- 采集记录 -->
      <a-tab-pane key="records" tab="采集记录">
        <div class="search-form">
          <a-form layout="inline" :model="recQuery">
            <a-form-item label="任务ID"><a-input-number v-model:value="recQuery.taskId" style="width:120px" /></a-form-item>
            <a-form-item label="状态">
              <a-select v-model:value="recQuery.status" style="width:120px" allowClear placeholder="全部">
                <a-select-option value="SUCCESS">成功</a-select-option>
                <a-select-option value="FAILED">失败</a-select-option>
                <a-select-option value="RUNNING">进行中</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item><a-button type="primary" @click="loadRecords">查询</a-button></a-form-item>
          </a-form>
        </div>
        <a-table :dataSource="records" :columns="recColumns" :loading="recLoading" rowKey="id" :pagination="recPagination" @change="handleRecPageChange" size="middle" style="background:#fff;border-radius:8px">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'status'">
              <a-tag :color="statusColor(record.status)">{{ record.status }}</a-tag>
            </template>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <!-- 任务弹窗 -->
    <a-modal v-model:open="taskModalVisible" :title="taskForm.id ? '编辑任务' : '新增任务'" @ok="saveTask" :confirm-loading="taskSaving" width="600px">
      <a-form :model="taskForm" :rules="taskRules" ref="taskFormRef" :label-col="{span:5}">
        <a-form-item label="任务名称" name="name"><a-input v-model:value="taskForm.name" /></a-form-item>
        <a-form-item label="数据源ID" name="datasourceId"><a-input-number v-model:value="taskForm.datasourceId" style="width:100%" /></a-form-item>
        <a-form-item label="Cron表达式" name="cronExpression"><a-input v-model:value="taskForm.cronExpression" placeholder="0 0/30 * * * ?" /></a-form-item>
        <a-form-item label="文件类型过滤"><a-input v-model:value="taskForm.fileTypeFilter" placeholder="jpg,png,tiff" /></a-form-item>
        <a-form-item label="文件大小上限(MB)"><a-input-number v-model:value="taskForm.maxFileSizeMb" style="width:100%" /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="taskForm.description" :rows="2" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getIngestTaskList, addIngestTask, updateIngestTask, deleteIngestTask, triggerIngestTask, getIngestRecords } from '@/api/ingest'
import type { IngestTask, IngestRecord } from '@/types'

const activeTab = ref('tasks')

// --- tasks ---
const taskLoading = ref(false)
const taskSaving = ref(false)
const tasks = ref<IngestTask[]>([])
const taskModalVisible = ref(false)
const taskFormRef = ref()
const taskQuery = reactive({ name: '', status: undefined as string | undefined, pageNum: 1, pageSize: 10 })
const taskPagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const taskForm = reactive<Partial<IngestTask>>({})
const taskRules = {
  name: [{ required: true, message: '请输入任务名称' }],
  datasourceId: [{ required: true, message: '请输入数据源ID' }],
  cronExpression: [{ required: true, message: '请输入Cron表达式' }],
}
const taskColumns = [
  { title: '任务名称', dataIndex: 'name' },
  { title: '数据源ID', dataIndex: 'datasourceId' },
  { title: 'Cron', dataIndex: 'cronExpression' },
  { title: '状态', key: 'enabled', dataIndex: 'enabled' },
  { title: '操作', key: 'action', width: 200 },
]

const loadTasks = async () => {
  taskLoading.value = true
  try {
    const res = await getIngestTaskList({ ...taskQuery, pageNum: taskPagination.current, pageSize: taskPagination.pageSize })
    tasks.value = res.data.rows
    taskPagination.total = res.data.total
  } finally { taskLoading.value = false }
}
const handleTaskPageChange = (p: typeof taskPagination) => { taskPagination.current = p.current; taskPagination.pageSize = p.pageSize; loadTasks() }
const showAddTask = () => { Object.assign(taskForm, { id: undefined, name: '', datasourceId: undefined, cronExpression: '', fileTypeFilter: '', maxFileSizeMb: undefined, description: '' }); taskModalVisible.value = true }
const showEditTask = (r: IngestTask) => { Object.assign(taskForm, { ...r }); taskModalVisible.value = true }
const saveTask = async () => {
  await taskFormRef.value?.validate()
  taskSaving.value = true
  try {
    if (taskForm.id) await updateIngestTask(taskForm as IngestTask)
    else await addIngestTask(taskForm as IngestTask)
    message.success('保存成功'); taskModalVisible.value = false; loadTasks()
  } finally { taskSaving.value = false }
}
const deleteTask = async (id: number) => { await deleteIngestTask(id); message.success('删除成功'); loadTasks() }
const triggerTask = async (id: number) => { await triggerIngestTask(id); message.success('触发成功') }

// --- records ---
const recLoading = ref(false)
const records = ref<IngestRecord[]>([])
const recQuery = reactive({ taskId: undefined as number | undefined, status: undefined as string | undefined })
const recPagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const recColumns = [
  { title: '任务ID', dataIndex: 'taskId' },
  { title: '文件名', dataIndex: 'fileName' },
  { title: '文件大小', dataIndex: 'fileSize' },
  { title: '状态', key: 'status', dataIndex: 'status' },
  { title: '采集时间', dataIndex: 'ingestTime' },
  { title: '错误信息', dataIndex: 'errorMsg', ellipsis: true },
]
const statusColor = (s: string) => ({ SUCCESS: 'success', FAILED: 'error', RUNNING: 'processing' }[s] || 'default')
const loadRecords = async () => {
  recLoading.value = true
  try {
    const res = await getIngestRecords({ ...recQuery, pageNum: recPagination.current, pageSize: recPagination.pageSize })
    records.value = res.data.rows
    recPagination.total = res.data.total
  } finally { recLoading.value = false }
}
const handleRecPageChange = (p: typeof recPagination) => { recPagination.current = p.current; recPagination.pageSize = p.pageSize; loadRecords() }

onMounted(() => { loadTasks(); loadRecords() })
</script>
