<template>
  <a-card title="告警规则管理">
    <div style="margin-bottom:12px">
      <a-button type="primary" @click="handleAdd"><PlusOutlined />新增</a-button>
    </div>
    <a-table :columns="columns" :data-source="tableData" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'conditionType'">
          {{ record.conditionType === 1 ? '合格率低于' : '缺陷数超过' }}
        </template>
        <template v-if="column.key === 'alertLevel'">
          <a-tag :color="record.alertLevel === 3 ? 'red' : record.alertLevel === 2 ? 'orange' : 'blue'">
            {{ ['', '一般', '重要', '紧急'][record.alertLevel] }}
          </a-tag>
        </template>
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'default'">{{ record.status === 1 ? '启用' : '禁用' }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
          <a-popconfirm title="确认删除?" @confirm="handleDelete(record.id)">
            <a-button type="link" size="small" danger>删除</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalVisible" :title="form.id ? '编辑规则' : '新增规则'" @ok="submitForm" :confirm-loading="submitLoading">
      <a-form :model="form" :label-col="{span:7}" :wrapper-col="{span:15}">
        <a-form-item label="规则名称" required><a-input v-model:value="form.ruleName" /></a-form-item>
        <a-form-item label="关联产线">
          <a-select v-model:value="form.lineId" allow-clear placeholder="全部产线">
            <a-select-option v-for="line in lineOptions" :key="line.id" :value="line.id">{{ line.lineName }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="条件类型" required>
          <a-select v-model:value="form.conditionType">
            <a-select-option :value="1">合格率低于</a-select-option>
            <a-select-option :value="2">缺陷数超过</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="阈值" required>
          <a-input-number v-model:value="form.threshold" :min="0" :step="0.01" style="width:100%" />
        </a-form-item>
        <a-form-item label="统计周期" required>
          <a-select v-model:value="form.statCycle">
            <a-select-option :value="1">实时</a-select-option>
            <a-select-option :value="2">小时</a-select-option>
            <a-select-option :value="3">天</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="告警级别" required>
          <a-select v-model:value="form.alertLevel">
            <a-select-option :value="1">一般</a-select-option>
            <a-select-option :value="2">重要</a-select-option>
            <a-select-option :value="3">紧急</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态"><a-switch v-model:checked="form.enabled" /></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getAlertRuleList, addAlertRule, updateAlertRule, deleteAlertRule } from '@/api/alert/rule'
import { getLineList } from '@/api/line'
const loading = ref(false)
const tableData = ref<any[]>([])
const lineOptions = ref<any[]>([])
const columns = [
  { title: '规则名称', dataIndex: 'ruleName' },
  { title: '条件类型', key: 'conditionType' },
  { title: '阈值', dataIndex: 'threshold' },
  { title: '告警级别', key: 'alertLevel' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action', width: 120 },
]
const loadData = async () => {
  loading.value = true
  try {
    const res = await getAlertRuleList({})
    tableData.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {} finally { loading.value = false }
}
const loadLines = async () => {
  try {
    const res = await getLineList({})
    lineOptions.value = (res as any).data?.rows ?? (res as any).rows ?? []
  } catch {}
}
const modalVisible = ref(false)
const submitLoading = ref(false)
const form = reactive<any>({ ruleName: '', lineId: undefined, conditionType: 1, threshold: 0.8, statCycle: 1, alertLevel: 1, enabled: true })
const handleAdd = () => {
  Object.assign(form, { id: undefined, ruleName: '', lineId: undefined, conditionType: 1, threshold: 0.8, statCycle: 1, alertLevel: 1, enabled: true })
  modalVisible.value = true
}
const handleEdit = (record: any) => {
  Object.assign(form, record, { enabled: record.status === 1 })
  modalVisible.value = true
}
const handleDelete = async (id: number) => {
  try { await deleteAlertRule(id); message.success('删除成功'); loadData() } catch {}
}
const submitForm = async () => {
  if (!form.ruleName) { message.warning('请输入规则名称'); return }
  submitLoading.value = true
  const payload = {
    ruleName: form.ruleName,
    lineId: form.lineId || null,
    conditionType: form.conditionType,
    threshold: form.threshold,
    statCycle: form.statCycle,
    alertLevel: form.alertLevel,
    status: form.enabled ? 1 : 0
  }
  try {
    if (form.id) { await updateAlertRule(form.id, payload); message.success('更新成功') }
    else { await addAlertRule(payload); message.success('新增成功') }
    modalVisible.value = false; loadData()
  } catch {} finally { submitLoading.value = false }
}
onMounted(() => { loadData(); loadLines() })
</script>
