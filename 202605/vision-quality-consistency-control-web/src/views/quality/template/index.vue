<template>
  <div>
    <div style="margin-bottom:12px"><a-button type="primary" @click="openModal()">新增模板</a-button></div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status===1?'green':'default'">{{ record.status===1?'启用':'停用' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" type="link" @click="viewDetail(record)">查看</a-button>
            <a-button size="small" type="link" @click="openModal(record)">编辑</a-button>
            <a-popconfirm :title="`确认${record.status===1?'停用':'启用'}?`" @confirm="toggleStatus(record)">
              <a-button size="small" type="link">{{ record.status===1?'停用':'启用' }}</a-button>
            </a-popconfirm>
            <a-popconfirm title="确认删除?" @confirm="onDelete(record.id)">
              <a-button size="small" type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalOpen" :title="editId ? '编辑模板' : '新增模板'" @ok="onSave" :confirm-loading="saving" width="600px">
      <a-form :model="form">
        <a-form-item label="模板名称"><a-input v-model:value="form.templateName" /></a-form-item>
        <a-form-item label="适用场景"><a-input v-model:value="form.applicableScene" /></a-form-item>
        <a-form-item label="包含指标">
          <a-select v-model:value="form.metricIds" mode="multiple" :options="metricOptions" placeholder="选择质量指标" />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-drawer v-model:open="detailOpen" title="模板详情" width="500">
      <a-descriptions :column="1" bordered>
        <a-descriptions-item label="模板名称">{{ detailData?.templateName }}</a-descriptions-item>
        <a-descriptions-item label="适用场景">{{ detailData?.applicableScene }}</a-descriptions-item>
        <a-descriptions-item label="创建人">{{ detailData?.createBy }}</a-descriptions-item>
        <a-descriptions-item label="状态"><a-tag :color="detailData?.status===1?'green':'default'">{{ detailData?.status===1?'启用':'停用' }}</a-tag></a-descriptions-item>
      </a-descriptions>
      <a-divider>包含指标</a-divider>
      <a-table :columns="metricCols" :data-source="detailData?.metrics||[]" :pagination="false" size="small" row-key="id" />
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { templateApi } from '@/api/quality/template'
import { metricApi } from '@/api/quality/metric'

const columns = [
  { title: '模板名称', dataIndex: 'templateName', key: 'templateName' },
  { title: '适用场景', dataIndex: 'applicableScene', key: 'applicableScene' },
  { title: '指标数', dataIndex: 'metricCount', key: 'metricCount' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', key: 'createdAt', customRender: ({ record }: any) => record.createdAt ? record.createdAt.replace('T', ' ').slice(0, 19) : '-' },
  { title: '操作', key: 'action' },
]
const metricCols = [
  { title: '指标名称', dataIndex: 'metricName' },
  { title: '类型', key: 'metricType', customRender: ({ record }: any) => record.metricType===0?'数值型':'等级型' },
  { title: '范围', customRender: ({ record }: any) => record.metricType===0 ? `${record.minValue??'-'} ~ ${record.maxValue??'-'}` : '-' },
]
const list = ref<any[]>([])
const loading = ref(false)
const modalOpen = ref(false)
const detailOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive({ templateName: '', applicableScene: '', metricIds: [] as number[] })
const metricOptions = ref<any[]>([])
const detailData = ref<any>(null)

async function loadData() {
  loading.value = true
  try {
    const res: any = await templateApi.list()
    list.value = res.data?.rows || res.data?.list || res.data || []
  } finally { loading.value = false }
}

function openModal(row?: any) {
  editId.value = row?.id || null
  Object.assign(form, row ? { templateName: row.templateName, applicableScene: row.applicableScene, metricIds: (row.metrics||[]).map((m: any) => m.id) } : { templateName: '', applicableScene: '', metricIds: [] })
  modalOpen.value = true
}

async function viewDetail(row: any) {
  const res: any = await templateApi.get(row.id)
  detailData.value = res.data
  detailOpen.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (editId.value) await templateApi.update(editId.value, form)
    else await templateApi.create(form)
    message.success('保存成功'); modalOpen.value = false; loadData()
  } finally { saving.value = false }
}

async function toggleStatus(row: any) {
  await templateApi.setStatus(row.id, row.status === 1 ? 0 : 1)
  message.success('操作成功'); loadData()
}

async function onDelete(id: number) {
  await templateApi.remove(id); message.success('删除成功'); loadData()
}

onMounted(async () => {
  loadData()
  const res: any = await metricApi.list({ status: 1 })
  metricOptions.value = (res.data?.rows || res.data?.list || res.data?.records || []).map((m: any) => ({ label: m.metricName, value: m.id }))
})
</script>
