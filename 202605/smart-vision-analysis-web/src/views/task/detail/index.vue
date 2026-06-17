<template>
  <div class="page-container">
    <a-page-header :title="isNew ? '创建识别任务' : '任务详情'" @back="$router.back()" />
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="12">
        <a-card title="基本信息">
          <a-form v-if="isNew || editing" :model="form" :rules="rules" ref="fRef" :label-col="{span:6}">
            <a-form-item label="任务名称" name="taskName"><a-input v-model:value="form.taskName" /></a-form-item>
            <a-form-item label="识别模型" name="modelId">
              <a-select v-model:value="form.modelId" :options="modelOptions" style="width:100%" />
            </a-form-item>
            <a-form-item label="置信度阈值">
              <a-slider v-model:value="form.confidenceThreshold" :min="0" :max="1" :step="0.05" :marks="{0:'0',0.5:'0.5',1:'1'}" />
            </a-form-item>
            <a-form-item label="影像范围" name="imageRange">
              <a-radio-group v-model:value="form.imageRange">
                <a-radio value="category">按分类选择</a-radio>
                <a-radio value="manual">手动勾选</a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item label="选择分类" v-if="form.imageRange==='category'">
              <a-select v-model:value="form.categoryIds" mode="multiple" :options="catOptions" style="width:100%" />
            </a-form-item>
            <a-form-item :wrapper-col="{offset:6}">
              <a-space>
                <a-button type="primary" @click="save(false)" :loading="saving">保存草稿</a-button>
                <a-button type="primary" @click="save(true)" :loading="saving" danger>保存并提交</a-button>
                <a-button @click="$router.back()">取消</a-button>
              </a-space>
            </a-form-item>
          </a-form>
          <a-descriptions v-else :column="1" bordered>
            <a-descriptions-item label="任务名称">{{ detail?.taskName }}</a-descriptions-item>
            <a-descriptions-item label="模型版本">{{ detail?.modelVersionNo }}</a-descriptions-item>
            <a-descriptions-item label="置信度阈值">{{ detail?.confidenceThreshold }}</a-descriptions-item>
            <a-descriptions-item label="状态"><a-tag :color="statusColor(detail?.status)">{{ statusLabel(detail?.status) }}</a-tag></a-descriptions-item>
            <a-descriptions-item label="影像总数">{{ detail?.totalCount }}</a-descriptions-item>
            <a-descriptions-item label="已处理">{{ detail?.processedCount }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ detail?.createdAt ? detail.createdAt.replace('T',' ').slice(0,19) : '-' }}</a-descriptions-item>
            <a-descriptions-item label="完成时间">{{ detail?.finishedAt ? detail.finishedAt.replace('T',' ').slice(0,19) : '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
      <a-col :span="12" v-if="!isNew">
        <a-card title="任务进度">
          <a-progress type="circle" :percent="progress" />
          <div style="margin-top:16px">
            <a-space>
              <a-button type="primary" v-if="detail?.status===0" @click="submit">提交执行</a-button>
              <a-button danger v-if="[0,1].includes(detail?.status)" @click="cancel">取消任务</a-button>
              <a-button v-if="detail?.status===2 || detail?.status===3" @click="$router.push({ name: 'ResultList', query: { taskId: detail.id } })">查看结果</a-button>
              <a-button v-if="detail?.status===2 || detail?.status===3" @click="$router.push({ name: 'ReportTask', query: { taskId: detail.id } })">查看报告</a-button>
            </a-space>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getTaskDetailApi, createTaskApi, submitTaskApi, cancelTaskApi } from '@/api/task'
import { getModelListApi } from '@/api/model'
import { getImageCategoriesApi } from '@/api/image'
const route = useRoute(); const router = useRouter()
const isNew = computed(() => route.params.id === 'new')
const detail = ref<any>(null); const editing = ref(false); const saving = ref(false); const fRef = ref()
const modelOptions = ref<any[]>([]); const catOptions = ref<any[]>([])
const form = reactive<any>({ taskName: '', modelId: undefined, confidenceThreshold: 0.5, imageRange: 'category', categoryIds: [] })
const rules = { taskName: [{ required: true }], modelId: [{ required: true }] }
const progress = computed(() => detail.value?.totalCount ? Math.round((detail.value.processedCount || 0) / detail.value.totalCount * 100) : 0)
const statusLabel = (s?: number) => s !== undefined ? ['待提交','执行中','已完成','失败','已取消'][s] || '-' : '-'
const statusColor = (s?: number) => s !== undefined ? ['default','processing','success','error','default'][s] || 'default' : 'default'
async function loadDetail() { if (isNew.value) return; const r = await getTaskDetailApi(Number(route.params.id)); detail.value = r.data }
async function loadOptions() { const [mr, cr] = await Promise.all([getModelListApi({ status: 1 }), getImageCategoriesApi()]); modelOptions.value = (mr.data?.list || mr.data || []).map((m: any) => ({ label: `${m.modelName} ${m.versionNo}`, value: m.id })); catOptions.value = (cr.data || []).map((c: any) => ({ label: c.categoryName, value: c.id })) }
async function save(submit: boolean) { await fRef.value?.validate(); saving.value = true; try { const r = await createTaskApi({ ...form, autoSubmit: submit }); message.success(submit ? '创建并提交成功' : '草稿已保存'); router.push({ name: 'TaskDetail', params: { id: r.data?.id } }) } finally { saving.value = false } }
async function submit() { await submitTaskApi(Number(route.params.id)); message.success('提交成功'); loadDetail() }
async function cancel() { await cancelTaskApi(Number(route.params.id)); message.success('已取消'); loadDetail() }
onMounted(() => { loadDetail(); loadOptions() })
</script>
