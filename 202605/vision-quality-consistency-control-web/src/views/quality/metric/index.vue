<template>
  <div>
    <a-form layout="inline" :model="query" style="margin-bottom:16px" @finish="loadData">
      <a-form-item><a-input v-model:value="query.metricName" placeholder="指标名称" allow-clear /></a-form-item>
      <a-form-item>
        <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:100px">
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="0">停用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" html-type="submit">查询</a-button></a-form-item>
    </a-form>
    <div style="margin-bottom:12px"><a-button type="primary" @click="openModal()">新增指标</a-button></div>
    <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id" :pagination="pagination" @change="onTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status===1?'green':'default'">{{ record.status===1?'启用':'停用' }}</a-tag>
        </template>
        <template v-if="column.key === 'importance'">
          <a-tag :color="record.importance===2?'red':record.importance===1?'orange':'blue'">{{ record.importance===2?'高':record.importance===1?'中':'低' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
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
    <a-modal v-model:open="modalOpen" :title="editId ? '编辑指标' : '新增指标'" @ok="onSave" :confirm-loading="saving" width="600px">
      <a-form :model="form" label-col="{ span: 7 }" wrapper-col="{ span: 15 }">
        <a-form-item label="指标名称"><a-input v-model:value="form.metricName" /></a-form-item>
        <a-form-item label="指标类型">
          <a-radio-group v-model:value="form.metricType">
            <a-radio :value="0">数值型</a-radio>
            <a-radio :value="1">等级型</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="计量单位"><a-input v-model:value="form.unit" /></a-form-item>
        <template v-if="form.metricType === 0">
          <a-form-item label="正常范围下限"><a-input-number v-model:value="form.minValue" style="width:100%" /></a-form-item>
          <a-form-item label="正常范围上限"><a-input-number v-model:value="form.maxValue" style="width:100%" /></a-form-item>
        </template>
        <a-form-item label="重要性">
          <a-select v-model:value="form.importance">
            <a-select-option :value="2">高</a-select-option>
            <a-select-option :value="1">中</a-select-option>
            <a-select-option :value="0">低</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="备注"><a-textarea v-model:value="form.remark" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { metricApi } from '@/api/quality/metric'

const columns = [
  { title: '指标名称', dataIndex: 'metricName', key: 'metricName' },
  { title: '类型', key: 'metricType', customRender: ({ record }: any) => record.metricType===0?'数值型':'等级型' },
  { title: '单位', dataIndex: 'unit', key: 'unit' },
  { title: '范围', key: 'range', customRender: ({ record }: any) => record.metricType===0 ? `${record.minValue??'-'} ~ ${record.maxValue??'-'}` : '-' },
  { title: '重要性', dataIndex: 'importance', key: 'importance' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '操作', key: 'action' },
]
const query = reactive({ metricName: '', status: undefined as number | undefined })
const list = ref<any[]>([])
const loading = ref(false)
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const modalOpen = ref(false)
const editId = ref<number | null>(null)
const saving = ref(false)
const form = reactive({ metricName: '', metricType: 0, unit: '', minValue: null as number | null, maxValue: null as number | null, importance: 1, remark: '' })

async function loadData() {
  loading.value = true
  try {
    const res: any = await metricApi.list({ ...query, page: pagination.current, pageSize: pagination.pageSize })
    list.value = res.data?.rows || res.data?.list || res.data?.records || []
    pagination.total = res.data?.total || 0
  } finally { loading.value = false }
}

function onTableChange(p: any) { pagination.current = p.current; loadData() }

function openModal(row?: any) {
  editId.value = row?.id || null
  Object.assign(form, row ? { ...row } : { metricName: '', metricType: 0, unit: '', minValue: null, maxValue: null, importance: 1, remark: '' })
  modalOpen.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (editId.value) await metricApi.update(editId.value, form)
    else await metricApi.create(form)
    message.success('保存成功'); modalOpen.value = false; loadData()
  } finally { saving.value = false }
}

async function toggleStatus(row: any) {
  await metricApi.setStatus(row.id, row.status === 1 ? 0 : 1)
  message.success('操作成功'); loadData()
}

async function onDelete(id: number) {
  await metricApi.remove(id); message.success('删除成功'); loadData()
}

onMounted(loadData)
</script>
