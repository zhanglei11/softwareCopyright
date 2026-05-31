<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-space wrap>
          <a-input v-model:value="query.keyword" placeholder="规则名称" allow-clear style="width:180px" @press-enter="loadData" />
          <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:100px">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="resetQuery"><reload-outlined />重置</a-button>
          <a-button type="primary" @click="handleAdd"><plus-outlined />新增规则</a-button>
        </a-space>
      </div>
      <a-table :columns="columns" :data-source="tableData" :loading="loading" row-key="id">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-switch :checked="record.status===1" checked-children="启用" un-checked-children="禁用" @change="(v:boolean) => handleStatusChange(record,v)" />
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-popconfirm title="确认删除？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="modalVisible" :title="modalTitle" @ok="handleSubmit" :confirm-loading="submitLoading" width="600px">
      <a-form ref="formRef" :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="规则名称" name="ruleName" :rules="[{required:true,message:'请输入规则名称'}]">
          <a-input v-model:value="form.ruleName" />
        </a-form-item>
        <a-form-item label="规则类型" name="ruleType">
          <a-select v-model:value="form.ruleType">
            <a-select-option value="THRESHOLD">阈值规则</a-select-option>
            <a-select-option value="LOGIC">逻辑规则</a-select-option>
            <a-select-option value="ML">机器学习</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="关联方案ID" name="schemeId">
          <a-input-number v-model:value="form.schemeId" style="width:100%" />
        </a-form-item>
        <a-form-item label="优先级" name="priority">
          <a-input-number v-model:value="form.priority" style="width:100%" :min="1" />
        </a-form-item>
        <a-form-item label="规则表达式" name="ruleExpression">
          <a-textarea v-model:value="form.ruleExpression" :rows="3" />
        </a-form-item>
        <a-form-item label="决策动作" name="action">
          <a-textarea v-model:value="form.action" :rows="2" />
        </a-form-item>
        <a-form-item label="描述" name="description">
          <a-textarea v-model:value="form.description" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getDecisionRuleListApi, addDecisionRuleApi, editDecisionRuleApi, deleteDecisionRuleApi, updateDecisionRuleStatusApi } from '@/api/decision/rule'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<any[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增决策规则')
const formRef = ref()
const query = reactive({ keyword: '', status: undefined as number | undefined })
const form = reactive<any>({ id: null, ruleName: '', ruleType: '', schemeId: null, priority: 1, ruleExpression: '', action: '', description: '' })

const columns = [
  { title: '规则名称', dataIndex: 'ruleName', key: 'ruleName' },
  { title: '规则类型', dataIndex: 'ruleType', key: 'ruleType' },
  { title: '优先级', dataIndex: 'priority', key: 'priority' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', width: 140 },
]

function resetQuery() { query.keyword = ''; query.status = undefined; loadData() }

async function loadData() {
  loading.value = true
  try { const res: any = await getDecisionRuleListApi(query); tableData.value = res.data || [] }
  finally { loading.value = false }
}

function handleAdd() {
  modalTitle.value = '新增决策规则'
  Object.assign(form, { id: null, ruleName: '', ruleType: '', schemeId: null, priority: 1, ruleExpression: '', action: '', description: '' })
  modalVisible.value = true
}

function handleEdit(record: any) {
  modalTitle.value = '编辑决策规则'; Object.assign(form, record); modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    form.id ? await editDecisionRuleApi(form.id, form) : await addDecisionRuleApi(form)
    message.success('操作成功'); modalVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

async function handleDelete(record: any) {
  await deleteDecisionRuleApi(record.id); message.success('删除成功'); loadData()
}

async function handleStatusChange(record: any, val: boolean) {
  await updateDecisionRuleStatusApi(record.id, val ? 1 : 0)
  record.status = val ? 1 : 0; message.success('状态已更新')
}

onMounted(loadData)
</script>
