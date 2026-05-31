<template>
  <div>
    <a-page-header :title="isNew ? '新增场景' : '编辑场景'" @back="$router.back()" style="padding:0 0 16px" />
    <a-card>
      <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 4 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="场景名称" name="name"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="场景分类" name="categoryId">
          <a-select v-model:value="form.categoryId" placeholder="请选择分类">
            <a-select-option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="AI模型" name="modelId">
          <a-select v-model:value="form.modelId" placeholder="请选择AI模型">
            <a-select-option v-for="m in models" :key="m.id" :value="m.id">{{ m.modelName }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="场景描述" name="description"><a-textarea v-model:value="form.description" :rows="3" /></a-form-item>
        <a-form-item label="系统提示词">
          <a-textarea v-model:value="form.systemPrompt" :rows="6" placeholder="输入系统提示词..." />
        </a-form-item>
        <a-form-item label="欢迎语">
          <a-textarea v-model:value="form.welcomeMessage" :rows="2" />
        </a-form-item>
        <a-form-item :wrapper-col="{ offset: 4 }">
          <a-space>
            <a-button type="primary" :loading="submitting" @click="handleSubmit">保存</a-button>
            <a-button @click="$router.back()">取消</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getSceneDetailApi, createSceneApi, updateSceneApi } from '@/api/ai/scene'
import { getCategoryListApi } from '@/api/ai/category'
import { getModelListApi } from '@/api/ai/model'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const submitting = ref(false)
const categories = ref<any[]>([])
const models = ref<any[]>([])
const id = computed(() => route.params.id as string)
const isNew = computed(() => !id.value || id.value === 'new')

const form = reactive<any>({ name: '', categoryId: undefined, modelId: undefined, description: '', systemPrompt: '', welcomeMessage: '' })
const rules = {
  name: [{ required: true, message: '请输入场景名称' }],
  categoryId: [{ required: true, message: '请选择场景分类' }],
  description: [{ required: true, message: '请输入场景描述' }],
}

onMounted(async () => {
  const [cRes, mRes]: any[] = await Promise.all([getCategoryListApi(), getModelListApi()])
  categories.value = cRes.data?.list || cRes.data || []
  models.value = mRes.data?.list || mRes.data || []
  if (!isNew.value) {
    const res: any = await getSceneDetailApi(Number(id.value))
    Object.assign(form, res.data)
  }
})

const handleSubmit = async () => {
  await formRef.value?.validate()
  submitting.value = true
  try {
    if (isNew.value) await createSceneApi(form)
    else await updateSceneApi(Number(id.value), form)
    message.success('保存成功')
    router.back()
  } finally { submitting.value = false }
}
</script>
