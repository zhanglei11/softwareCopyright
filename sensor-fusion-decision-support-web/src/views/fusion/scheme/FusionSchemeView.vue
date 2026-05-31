<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-space>
          <a-input v-model:value="query.keyword" placeholder="方案名称" allow-clear style="width:200px" @press-enter="loadData" />
          <a-select v-model:value="query.status" placeholder="状态" allow-clear style="width:100px">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="()=>{query.keyword='';query.status=undefined;loadData()}"><reload-outlined />重置</a-button>
          <a-button type="primary" @click="handleAdd"><plus-outlined />新增方案</a-button>
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
              <a-button type="link" size="small" @click="handleRules(record)">规则配置</a-button>
              <a-popconfirm title="确认删除？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal v-model:open="modalVisible" :title="modalTitle" @ok="handleSubmit" :confirm-loading="submitLoading">
      <a-form ref="formRef" :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="方案名称" name="schemeName" :rules="[{required:true,message:'请输入方案名称'}]">
          <a-input v-model:value="form.schemeName" />
        </a-form-item>
        <a-form-item label="融合算法" name="algorithm">
          <a-select v-model:value="form.algorithm">
            <a-select-option value="WEIGHTED_AVG">加权平均</a-select-option>
            <a-select-option value="KALMAN">卡尔曼滤波</a-select-option>
            <a-select-option value="DEMPSTER_SHAFER">D-S证据理论</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述" name="description">
          <a-textarea v-model:value="form.description" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 规则配置抽屉 -->
    <a-drawer v-model:open="rulesDrawerVisible" :title="`${currentScheme?.schemeName} - 规则配置`" width="700px">
      <div style="margin-bottom:12px">
        <a-button type="primary" size="small" @click="handleAddRule"><plus-outlined />新增规则</a-button>
      </div>
      <a-table :columns="ruleColumns" :data-source="rulesData" :loading="rulesLoading" row-key="id" size="small">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEditRule(record)">编辑</a-button>
              <a-popconfirm title="确认删除？" @confirm="handleDeleteRule(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
      <a-modal v-model:open="ruleModalVisible" :title="ruleModalTitle" @ok="handleRuleSubmit" :confirm-loading="ruleSubmitLoading">
        <a-form ref="ruleFormRef" :model="ruleForm" :label-col="{span:6}" :wrapper-col="{span:16}">
          <a-form-item label="数据源ID" name="datasourceId" :rules="[{required:true,message:'请输入数据源ID'}]">
            <a-input-number v-model:value="ruleForm.datasourceId" style="width:100%" />
          </a-form-item>
          <a-form-item label="权重" name="weight">
            <a-input-number v-model:value="ruleForm.weight" :min="0" :max="1" :step="0.01" style="width:100%" />
          </a-form-item>
          <a-form-item label="预处理规则" name="preprocessRule">
            <a-textarea v-model:value="ruleForm.preprocessRule" />
          </a-form-item>
        </a-form>
      </a-modal>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getFusionSchemeListApi, addFusionSchemeApi, editFusionSchemeApi, deleteFusionSchemeApi, updateFusionSchemeStatusApi, getSchemeRulesApi, addSchemeRuleApi, editSchemeRuleApi, deleteSchemeRuleApi } from '@/api/fusion/scheme'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<any[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增方案')
const formRef = ref()
const query = reactive({ keyword: '', status: undefined as number | undefined })
const form = reactive<any>({ id: null, schemeName: '', algorithm: '', description: '' })

const rulesDrawerVisible = ref(false)
const rulesLoading = ref(false)
const rulesData = ref<any[]>([])
const currentScheme = ref<any>(null)
const ruleModalVisible = ref(false)
const ruleModalTitle = ref('新增规则')
const ruleSubmitLoading = ref(false)
const ruleFormRef = ref()
const ruleForm = reactive<any>({ id: null, datasourceId: null, weight: 1, preprocessRule: '' })

const columns = [
  { title: '方案名称', dataIndex: 'schemeName', key: 'schemeName' },
  { title: '融合算法', dataIndex: 'algorithm', key: 'algorithm' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '描述', dataIndex: 'description', key: 'description' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', width: 200 },
]

const ruleColumns = [
  { title: '数据源ID', dataIndex: 'datasourceId', key: 'datasourceId' },
  { title: '权重', dataIndex: 'weight', key: 'weight' },
  { title: '预处理规则', dataIndex: 'preprocessRule', key: 'preprocessRule' },
  { title: '操作', key: 'action', width: 120 },
]

async function loadData() {
  loading.value = true
  try { const res: any = await getFusionSchemeListApi(query); tableData.value = res.data || [] }
  finally { loading.value = false }
}

function handleAdd() {
  modalTitle.value = '新增方案'
  Object.assign(form, { id: null, schemeName: '', algorithm: '', description: '' })
  modalVisible.value = true
}

function handleEdit(record: any) {
  modalTitle.value = '编辑方案'; Object.assign(form, record); modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    form.id ? await editFusionSchemeApi(form.id, form) : await addFusionSchemeApi(form)
    message.success('操作成功'); modalVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

async function handleDelete(record: any) {
  await deleteFusionSchemeApi(record.id); message.success('删除成功'); loadData()
}

async function handleStatusChange(record: any, val: boolean) {
  await updateFusionSchemeStatusApi(record.id, val ? 1 : 0)
  record.status = val ? 1 : 0; message.success('状态已更新')
}

async function handleRules(record: any) {
  currentScheme.value = record
  rulesDrawerVisible.value = true
  await loadRules()
}

async function loadRules() {
  if (!currentScheme.value) return
  rulesLoading.value = true
  try { const res: any = await getSchemeRulesApi(currentScheme.value.id); rulesData.value = res.data || [] }
  finally { rulesLoading.value = false }
}

function handleAddRule() {
  ruleModalTitle.value = '新增规则'
  Object.assign(ruleForm, { id: null, datasourceId: null, weight: 1, preprocessRule: '' })
  ruleModalVisible.value = true
}

function handleEditRule(record: any) {
  ruleModalTitle.value = '编辑规则'; Object.assign(ruleForm, record); ruleModalVisible.value = true
}

async function handleRuleSubmit() {
  await ruleFormRef.value.validate()
  ruleSubmitLoading.value = true
  try {
    ruleForm.id ? await editSchemeRuleApi(currentScheme.value.id, ruleForm.id, ruleForm) : await addSchemeRuleApi(currentScheme.value.id, ruleForm)
    message.success('操作成功'); ruleModalVisible.value = false; loadRules()
  } finally { ruleSubmitLoading.value = false }
}

async function handleDeleteRule(record: any) {
  await deleteSchemeRuleApi(currentScheme.value.id, record.id); message.success('删除成功'); loadRules()
}

onMounted(loadData)
</script>
