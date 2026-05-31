<template>
  <div class="page-container">
    <a-tabs v-model:activeKey="activeTab">
      <!-- 处理任务 -->
      <a-tab-pane key="tasks" tab="处理任务">
        <div class="table-toolbar">
          <span>处理任务列表</span>
          <a-button type="primary" @click="showAddTask"><PlusOutlined /> 新增</a-button>
        </div>
        <a-table :dataSource="tasks" :columns="taskColumns" :loading="taskLoading" rowKey="id" :pagination="taskPagination" @change="handleTaskPageChange" size="middle" style="background:#fff;border-radius:8px">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'action'">
              <a-space>
                <a-button type="link" size="small" @click="triggerTask(record.id!)">触发</a-button>
                <a-button type="link" size="small" @click="showEdit(record)">编辑</a-button>
                <a-popconfirm title="确认删除？" @confirm="deleteTask(record.id!)">
                  <a-button type="link" size="small" danger>删除</a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </a-table>
      </a-tab-pane>

      <!-- 执行记录 -->
      <a-tab-pane key="executions" tab="执行记录">
        <a-table :dataSource="executions" :columns="execColumns" :loading="execLoading" rowKey="id" :pagination="execPagination" @change="handleExecPageChange" size="middle" style="background:#fff;border-radius:8px">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'status'">
              <a-tag :color="statusColor(record.status)">{{ record.status }}</a-tag>
            </template>
            <template v-if="column.key === 'progress'">
              <a-progress :percent="record.progress || 0" size="small" />
            </template>
            <template v-if="column.key === 'action'">
              <a-button v-if="record.status === 'RUNNING'" type="link" size="small" danger @click="terminateExec(record.id!)">终止</a-button>
            </template>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <!-- 任务弹窗 -->
    <a-modal v-model:open="taskModalVisible" :title="taskForm.id ? '编辑任务' : '新增任务'" @ok="saveTask" :confirm-loading="saving" width="600px">
      <a-form :model="taskForm" :rules="taskRules" ref="taskFormRef" :label-col="{span:6}">
        <a-form-item label="任务名称" name="name"><a-input v-model:value="taskForm.name" /></a-form-item>
        <a-form-item label="处理类型" name="processType">
          <a-select v-model:value="taskForm.processType">
            <a-select-option value="IMAGE_COMPRESS">图像压缩</a-select-option>
            <a-select-option value="FORMAT_CONVERT">格式转换</a-select-option>
            <a-select-option value="QUALITY_CHECK">质检</a-select-option>
            <a-select-option value="METADATA_EXTRACT">元数据提取</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Cron表达式" name="cronExpression"><a-input v-model:value="taskForm.cronExpression" /></a-form-item>
        <a-form-item label="参数JSON"><a-textarea v-model:value="taskForm.processParams" :rows="3" placeholder='{"quality":80}' /></a-form-item>
        <a-form-item label="描述"><a-textarea v-model:value="taskForm.description" :rows="2" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getProcessTaskList, addProcessTask, updateProcessTask, deleteProcessTask, triggerProcessTask, getProcessExecutions, terminateProcessTask as terminateExecution } from '@/api/process'
import type { ProcessTask, ProcessExecution } from '@/types'

const activeTab = ref('tasks')

// Tasks
const taskLoading = ref(false)
const tasks = ref<ProcessTask[]>([])
const taskModalVisible = ref(false)
const saving = ref(false)
const taskFormRef = ref()
const taskPagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const taskForm = reactive<Partial<ProcessTask>>({})
const taskRules = {
  name: [{ required: true, message: '请输入任务名称' }],
  processType: [{ required: true, message: '请选择处理类型' }],
}
const taskColumns = [
  { title: '任务名称', dataIndex: 'name' },
  { title: '处理类型', dataIndex: 'processType' },
  { title: 'Cron', dataIndex: 'cronExpression' },
  { title: '操作', key: 'action', width: 180 },
]
const loadTasks = async () => {
  taskLoading.value = true
  try {
    const res = await getProcessTaskList({ pageNum: taskPagination.current, pageSize: taskPagination.pageSize })
    tasks.value = res.data.rows; taskPagination.total = res.data.total
  } finally { taskLoading.value = false }
}
const handleTaskPageChange = (p: typeof taskPagination) => { taskPagination.current = p.current; taskPagination.pageSize = p.pageSize; loadTasks() }
const showAddTask = () => { Object.assign(taskForm, { id: undefined, name: '', processType: '', cronExpression: '', processParams: '', description: '' }); taskModalVisible.value = true }
const showEdit = (r: ProcessTask) => { Object.assign(taskForm, { ...r }); taskModalVisible.value = true }
const saveTask = async () => {
  await taskFormRef.value?.validate(); saving.value = true
  try {
    if (taskForm.id) await updateProcessTask(taskForm.id, taskForm as ProcessTask)
    else await addProcessTask(taskForm as ProcessTask)
    message.success('保存成功'); taskModalVisible.value = false; loadTasks()
  } finally { saving.value = false }
}
const deleteTask = async (id: number) => { await deleteProcessTask(id); message.success('删除成功'); loadTasks() }
const triggerTask = async (id: number) => { await triggerProcessTask(id); message.success('触发成功') }

// Executions
const execLoading = ref(false)
const executions = ref<ProcessExecution[]>([])
const execPagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })
const execColumns = [
  { title: '任务ID', dataIndex: 'taskId' },
  { title: '状态', key: 'status', dataIndex: 'status' },
  { title: '进度', key: 'progress', dataIndex: 'progress', width: 160 },
  { title: '开始时间', dataIndex: 'startTime' },
  { title: '结束时间', dataIndex: 'endTime' },
  { title: '操作', key: 'action', width: 80 },
]
const statusColor = (s: string) => ({ SUCCESS: 'success', FAILED: 'error', RUNNING: 'processing', PENDING: 'default' }[s] || 'default')
const loadExecutions = async () => {
  execLoading.value = true
  try {
    const res = await getProcessExecutions({ pageNum: execPagination.current, pageSize: execPagination.pageSize })
    executions.value = res.data.rows; execPagination.total = res.data.total
  } finally { execLoading.value = false }
}
const handleExecPageChange = (p: typeof execPagination) => { execPagination.current = p.current; execPagination.pageSize = p.pageSize; loadExecutions() }
const terminateExec = async (id: number) => { await terminateExecution(id); message.success('终止成功'); loadExecutions() }

onMounted(() => { loadTasks(); loadExecutions() })
</script>
