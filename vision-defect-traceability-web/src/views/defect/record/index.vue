<template>
  <a-card>
    <div class="search-bar">
      <a-form layout="inline" :model="query">
        <a-form-item label="批次号">
          <a-input v-model:value="query.batchNo" placeholder="请输入批次号" allow-clear style="width:160px" @pressEnter="loadData" />
        </a-form-item>
        <a-form-item label="序列号">
          <a-input v-model:value="query.serialNo" placeholder="请输入序列号" allow-clear style="width:160px" @pressEnter="loadData" />
        </a-form-item>
        <a-form-item label="检测结果">
          <a-select v-model:value="query.result" allow-clear style="width:120px" placeholder="全部">
            <a-select-option :value="1">合格</a-select-option>
            <a-select-option :value="0">不合格</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="处置状态">
          <a-select v-model:value="query.disposeStatus" allow-clear style="width:120px" placeholder="全部">
            <a-select-option :value="0">待处置</a-select-option>
            <a-select-option :value="1">已处置</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="loadData"><SearchOutlined />搜索</a-button>
            <a-button @click="resetQuery"><ReloadOutlined />重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
    <a-table :columns="columns" :data-source="tableData" :loading="loading"
      :pagination="pagination" row-key="id" @change="handleTableChange"
      :scroll="{ x: 'max-content' }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'result'">
          <a-tag :color="record.result === 1 ? 'green' : 'red'">{{ record.result === 1 ? '合格' : '不合格' }}</a-tag>
        </template>
        <template v-if="column.key === 'level'">
          <a-tag v-if="record.level != null" :color="['','blue','orange','red'][record.level] || 'default'">{{ {1:'轻微',2:'一般',3:'严重'}[record.level] || record.level }}</a-tag>
          <span v-else>-</span>
        </template>
        <template v-if="column.key === 'disposeStatus'">
          <a-tag :color="record.disposeStatus === 1 ? 'green' : 'orange'">{{ record.disposeStatus === 1 ? '已处置' : '待处置' }}</a-tag>
        </template>
        <template v-if="column.key === 'detectTime'">
          {{ record.detectTime ? record.detectTime.replace('T', ' ').slice(0, 19) : '-' }}
        </template>
        <template v-if="column.key === 'action'">
          <a-button type="link" size="small" @click="handleDetail(record)">详情</a-button>
          <a-button v-if="record.disposeStatus === 0" type="link" size="small" @click="handleDispose(record)">处置</a-button>
        </template>
      </template>
    </a-table>

    <!-- 处置弹窗 -->
    <a-modal v-model:open="disposeVisible" title="缺陷处置" @ok="submitDispose" :confirm-loading="submitLoading">
      <a-form :model="disposeForm" layout="vertical">
        <a-form-item label="处置说明" required>
          <a-textarea v-model:value="disposeForm.disposeRemark" :rows="4" placeholder="请输入处置说明" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import { getDefectRecordList, disposeDefect } from '@/api/defect/record'
import type { DefectRecord } from '@/types'

const loading = ref(false)
const tableData = ref<DefectRecord[]>([])
const query = reactive({ pageNum: 1, pageSize: 10, batchNo: '', serialNo: '', result: undefined as number | undefined, disposeStatus: undefined as number | undefined })
const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showQuickJumper: true })

const columns = [
  { title: '序列号', dataIndex: 'serialNo', key: 'serialNo' },
  { title: '批次号', dataIndex: 'batchNo', key: 'batchNo' },
  { title: '产线', dataIndex: 'lineName', key: 'lineName' },
  { title: '产品型号', dataIndex: 'productTypeName', key: 'productTypeName' },
  { title: '缺陷分类', dataIndex: 'categoryName', key: 'categoryName' },
  { title: '缺陷等级', dataIndex: 'level', key: 'level' },
  { title: '检测结果', dataIndex: 'result', key: 'result' },
  { title: '班次', dataIndex: 'shift', key: 'shift' },
  { title: '检测时间', dataIndex: 'detectTime', key: 'detectTime' },
  { title: '处置状态', dataIndex: 'disposeStatus', key: 'disposeStatus' },
  { title: '操作', key: 'action', fixed: 'right', width: 120 },
]

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDefectRecordList({ ...query, pageNum: pagination.current, pageSize: pagination.pageSize })
    const d = (res as any).data
    tableData.value = d.rows ?? []
    pagination.total = d.total ?? 0
  } catch {} finally { loading.value = false }
}

const resetQuery = () => {
  query.batchNo = ''; query.serialNo = ''; query.result = undefined; query.disposeStatus = undefined
  pagination.current = 1; loadData()
}

const handleTableChange = (p: any) => { pagination.current = p.current; pagination.pageSize = p.pageSize; loadData() }

// 处置
const disposeVisible = ref(false)
const submitLoading = ref(false)
const currentRecord = ref<DefectRecord | null>(null)
const disposeForm = reactive({ disposeRemark: '' })

const handleDetail = (record: DefectRecord) => { /* 简单弹出详情 */ }
const handleDispose = (record: DefectRecord) => { currentRecord.value = record; disposeForm.disposeRemark = ''; disposeVisible.value = true }
const submitDispose = async () => {
  if (!disposeForm.disposeRemark.trim()) { message.warning('请输入处置说明'); return }
  submitLoading.value = true
  try {
    await disposeDefect(currentRecord.value!.id, { disposeRemark: disposeForm.disposeRemark })
    message.success('处置成功'); disposeVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}

onMounted(loadData)
</script>
