<template>
  <div>
    <a-form layout="inline" :model="query" style="margin-bottom:16px" @finish="loadData">
      <a-form-item>
        <a-select v-model:value="query.disposeStatus" placeholder="处置状态" allow-clear style="width:130px">
          <a-select-option :value="1">待处置</a-select-option>
          <a-select-option :value="2">处置中</a-select-option>
          <a-select-option :value="3">已处置</a-select-option>
          <a-select-option :value="4">已忽略</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" html-type="submit">查询</a-button></a-form-item>
    </a-form>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="pagination" @change="onTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'disposeStatus'">
          <a-tag :color="disposeColor(record.disposeStatus)">{{ disposeLabel(record.disposeStatus) }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="viewDetail(record)">详情</a-button>
            <a-button v-if="record.disposeStatus===1||record.disposeStatus===2" size="small" type="link" @click="openDispose(record)">处置</a-button>
            <a-popconfirm v-if="record.disposeStatus===1" title="确认忽略?" @confirm="onIgnore(record.id)">
              <a-button size="small" type="link">忽略</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-drawer v-model:open="detailOpen" title="不合格品详情" width="600">
      <a-descriptions :column="2" bordered v-if="detailData">
        <a-descriptions-item label="记录编号" :span="2">{{ detailData.defectCode }}</a-descriptions-item>
        <a-descriptions-item label="影像标识">{{ detailData.imageId }}</a-descriptions-item>
        <a-descriptions-item label="发现时间">{{ detailData.foundAt ? detailData.foundAt.replace('T', ' ').slice(0, 19) : '-' }}</a-descriptions-item>
        <a-descriptions-item label="超标指标" :span="2">{{ formatMetricIds(detailData.exceededMetrics) }}</a-descriptions-item>
        <a-descriptions-item label="处置状态" :span="2"><a-tag :color="disposeColor(detailData.disposeStatus)">{{ disposeLabel(detailData.disposeStatus) }}</a-tag></a-descriptions-item>
      </a-descriptions>
      <a-divider>处置历史</a-divider>
      <a-timeline>
        <a-timeline-item v-for="h in history" :key="h.id">
          <b>{{ h.disposeTime }}</b> - {{ h.disposeBy }} <br />
          {{ h.disposeResult }}
        </a-timeline-item>
      </a-timeline>
    </a-drawer>

    <a-modal v-model:open="disposeOpen" title="填写处置信息" @ok="onDisposeSave" :confirm-loading="saving">
      <a-form :model="disposeForm">
        <a-form-item label="处置方案">
          <a-select v-model:value="disposeForm.disposeAction">
            <a-select-option value="RECOLLECT">重新采集</a-select-option>
            <a-select-option value="ADJUST">参数调整</a-select-option>
            <a-select-option value="MAINTAIN">设备维护</a-select-option>
            <a-select-option value="ACCEPT">接受（忽略）</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="处置结果说明">
          <a-textarea v-model:value="disposeForm.disposeResult" :rows="4" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { defectApi } from '@/api/quality/defect'
import { metricApi } from '@/api/quality/metric'

const disposeColor = (s: number) => ({ 1: 'orange', 2: 'processing', 3: 'success', 4: 'default' } as any)[s] || 'default'
const disposeLabel = (s: number) => ({ 1: '待处置', 2: '处置中', 3: '已处置', 4: '已忽略' } as any)[s] || s
const formatMetricIds = (text: string) => { try { const ids: number[] = JSON.parse(text); return ids.map((id: number) => metricMap.value[id] || `指标${id}`).join('、') } catch { return text || '-' } }

// 指标ID->名称映射
const metricMap = ref<Record<number, string>>({})

const columns = [
  { title: '记录编号', dataIndex: 'defectCode', key: 'defectCode' },
  { title: '影像标识', dataIndex: 'imageId', key: 'imageId' },
  { title: '超标指标', dataIndex: 'exceededMetrics', key: 'exceededMetrics', customRender: ({ text }: any) => formatMetricIds(text) },
  { title: '处置状态', dataIndex: 'disposeStatus', key: 'disposeStatus' },
  { title: '发现时间', key: 'foundAt', customRender: ({ record }: any) => record.foundAt ? record.foundAt.replace('T', ' ').slice(0, 19) : '-' },
  { title: '操作', key: 'action' },
]
const query = reactive({ disposeStatus: undefined as number | undefined })
const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const detailOpen = ref(false)
const detailData = ref<any>(null)
const history = ref<any[]>([])
const disposeOpen = ref(false)
const disposeDefectId = ref<number | null>(null)
const saving = ref(false)
const disposeForm = reactive({ disposeAction: 'RECOLLECT', disposeResult: '' })

async function loadData() {
  loading.value = true
  try {
    const res: any = await defectApi.list({ ...query, page: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data?.rows || res.data?.list || res.data?.records || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

async function loadMetrics() {
  try {
    const res: any = await metricApi.list({ page: 1, pageSize: 200 })
    const rows = res.data?.rows || []
    const map: Record<number, string> = {}
    rows.forEach((m: any) => { map[m.id] = m.metricName })
    metricMap.value = map
  } catch { /* ignore */ }
}
function onTableChange(p: any) { pagination.current = p.current; loadData() }

async function viewDetail(row: any) {
  const [r1, r2]: any[] = await Promise.all([defectApi.get(row.id), defectApi.history(row.id)])
  detailData.value = r1.data; history.value = r2.data || []
  detailOpen.value = true
}

function openDispose(row: any) {
  disposeDefectId.value = row.id
  Object.assign(disposeForm, { disposeAction: 'RECOLLECT', disposeResult: '' })
  disposeOpen.value = true
}

async function onDisposeSave() {
  saving.value = true
  try {
    await defectApi.dispose({ defectId: disposeDefectId.value, ...disposeForm })
    message.success('处置成功'); disposeOpen.value = false; loadData()
  } finally { saving.value = false }
}

async function onIgnore(id: number) {
  await defectApi.ignore({ defectId: id }); message.success('已忽略'); loadData()
}

onMounted(() => { loadMetrics(); loadData() })
</script>
