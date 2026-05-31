<template>
  <div class="model-page">
    <a-card :bordered="false">
      <template #title>模型版本管理</template>
      <template #extra>
        <a-button type="primary" @click="openCreate">
          <template #icon><PlusOutlined /></template>
          新增模型
        </a-button>
      </template>

      <!-- 搜索栏 -->
      <a-form layout="inline" :model="queryForm" class="search-form" @finish="fetchList">
        <a-form-item label="模型名称">
          <a-input v-model:value="queryForm.modelName" placeholder="模型名称" allow-clear />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="queryForm.status" style="width:110px" allow-clear placeholder="全部">
            <a-select-option :value="1">可用</a-select-option>
            <a-select-option :value="0">已废弃</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit">搜索</a-button>
            <a-button @click="resetQuery">重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <a-table
        :data-source="list"
        :columns="columns"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '可用' : '已废弃' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'supportLabels'">
            <a-space wrap>
              <a-tag v-for="l in (record.supportLabels || [])" :key="l" color="blue">{{ l }}</a-tag>
            </a-space>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="openEdit(record)">编辑</a-button>
              <a-button
                v-if="record.status === 1"
                type="link"
                size="small"
                danger
                @click="handleDeprecate(record)"
              >废弃</a-button>
              <a-button
                v-else
                type="link"
                size="small"
                @click="handleRestore(record)"
              >恢复</a-button>
              <a-popconfirm title="确认删除该模型版本？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="editId ? '编辑模型' : '新增模型'"
      width="600px"
      @ok="submitForm"
      :confirm-loading="submitting"
    >
      <a-form :model="form" :rules="rules" ref="formRef" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="模型名称" name="modelName">
              <a-input v-model:value="form.modelName" placeholder="如：YOLOv8" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="版本号" name="versionNo">
              <a-input v-model:value="form.versionNo" placeholder="如：v1.0.0" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="应用场景描述" name="sceneDesc">
          <a-textarea v-model:value="form.sceneDesc" :rows="3" placeholder="描述该模型适用的场景..." />
        </a-form-item>
        <a-form-item label="支持标签（回车添加）" name="supportLabels">
          <div class="labels-input">
            <a-tag
              v-for="(l, i) in form.supportLabels"
              :key="l"
              closable
              @close="removeLabel(i)"
            >{{ l }}</a-tag>
            <a-input
              v-if="labelInputVisible"
              ref="labelInputRef"
              v-model:value="labelInputVal"
              size="small"
              style="width:100px"
              @blur="addLabel"
              @pressEnter="addLabel"
            />
            <a-tag v-else style="cursor:pointer;border-style:dashed" @click="showLabelInput">
              <PlusOutlined /> 添加标签
            </a-tag>
          </div>
        </a-form-item>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="发布日期">
              <a-date-picker v-model:value="releaseDate" style="width:100%" format="YYYY-MM-DD" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注">
              <a-input v-model:value="form.remark" placeholder="可选" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import dayjs, { type Dayjs } from 'dayjs'
import {
  getModelListApi,
  createModelApi,
  updateModelApi,
  deprecateModelApi,
  restoreModelApi,
  deleteModelApi
} from '@/api/model/index'
import type { ModelVersion } from '@/types/index'

const loading = ref(false)
const submitting = ref(false)
const list = ref<ModelVersion[]>([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })

const queryForm = reactive<{ modelName?: string; status?: number }>({})

const columns = [
  { title: '模型名称', dataIndex: 'modelName', key: 'modelName' },
  { title: '版本号', dataIndex: 'versionNo', key: 'versionNo' },
  { title: '应用场景', dataIndex: 'sceneDesc', key: 'sceneDesc', ellipsis: true },
  { title: '支持标签', key: 'supportLabels' },
  { title: '发布日期', dataIndex: 'releaseDate', key: 'releaseDate' },
  { title: '状态', key: 'status', width: 90 },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt', width: 180, customRender: ({ text }: any) => text ? text.replace('T', ' ').slice(0, 19) : '-' },
  { title: '操作', key: 'action', width: 200, fixed: 'right' }
]

const modalVisible = ref(false)
const editId = ref(0)
const formRef = ref()
const form = reactive<{
  modelName: string; versionNo: string; sceneDesc: string
  supportLabels: string[]; remark: string
}>({ modelName: '', versionNo: '', sceneDesc: '', supportLabels: [], remark: '' })
const releaseDate = ref<Dayjs | null>(null)

const rules = {
  modelName: [{ required: true, message: '请输入模型名称' }],
  versionNo: [{ required: true, message: '请输入版本号' }]
}

const labelInputVisible = ref(false)
const labelInputVal = ref('')
const labelInputRef = ref()

async function fetchList() {
  loading.value = true
  try {
    const res = await getModelListApi({
      ...queryForm,
      pageNum: pagination.current,
      pageSize: pagination.pageSize
    })
    const data = res.data?.data || res.data
    list.value = data?.rows || data || []
    pagination.total = data?.total || list.value.length
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  queryForm.modelName = undefined
  queryForm.status = undefined
  pagination.current = 1
  fetchList()
}

function handleTableChange(pag: { current: number; pageSize: number }) {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchList()
}

function openCreate() {
  editId.value = 0
  Object.assign(form, { modelName: '', versionNo: '', sceneDesc: '', supportLabels: [], remark: '' })
  releaseDate.value = null
  modalVisible.value = true
}

function openEdit(record: ModelVersion) {
  editId.value = record.id
  Object.assign(form, {
    modelName: record.modelName,
    versionNo: record.versionNo,
    sceneDesc: record.sceneDesc || '',
    supportLabels: [...(record.supportLabels || [])],
    remark: record.remark || ''
  })
  releaseDate.value = record.releaseDate ? dayjs(record.releaseDate) : null
  modalVisible.value = true
}

async function submitForm() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  submitting.value = true
  const payload = {
    ...form,
    releaseDate: releaseDate.value ? releaseDate.value.format('YYYY-MM-DD') : undefined
  }
  try {
    if (editId.value) {
      await updateModelApi(editId.value, payload)
      message.success('更新成功')
    } else {
      await createModelApi(payload)
      message.success('创建成功')
    }
    modalVisible.value = false
    fetchList()
  } catch {
    message.error('操作失败')
  } finally {
    submitting.value = false
  }
}

async function handleDeprecate(record: ModelVersion) {
  try {
    await deprecateModelApi(record.id)
    message.success('已废弃')
    fetchList()
  } catch {
    message.error('操作失败')
  }
}

async function handleRestore(record: ModelVersion) {
  try {
    await restoreModelApi(record.id)
    message.success('已恢复')
    fetchList()
  } catch {
    message.error('操作失败')
  }
}

async function handleDelete(record: ModelVersion) {
  try {
    await deleteModelApi(record.id)
    message.success('已删除')
    fetchList()
  } catch {
    message.error('删除失败，可能存在关联任务')
  }
}

function showLabelInput() {
  labelInputVisible.value = true
  nextTick(() => labelInputRef.value?.focus())
}

function addLabel() {
  const v = labelInputVal.value.trim()
  if (v && !form.supportLabels.includes(v)) {
    form.supportLabels.push(v)
  }
  labelInputVal.value = ''
  labelInputVisible.value = false
}

function removeLabel(i: number) {
  form.supportLabels.splice(i, 1)
}

onMounted(() => fetchList())
</script>

<style scoped lang="scss">
.model-page {
  padding: 16px;
}

.search-form {
  margin-bottom: 16px;
}

.labels-input {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
  padding: 4px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  min-height: 36px;
}
</style>
