<template>
  <div>
    <a-page-header title="场景广场" style="padding:0 0 16px" />
    <a-row :gutter="16" style="margin-bottom:16px">
      <a-col :span="12">
        <a-input-search v-model:value="query.name" placeholder="搜索场景..." enter-button @search="loadData" />
      </a-col>
      <a-col>
        <a-select v-model:value="query.categoryId" placeholder="全部分类" style="width:160px" allow-clear @change="loadData">
          <a-select-option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <a-spin :spinning="loading">
      <a-row :gutter="[16, 16]">
        <a-col :xs="24" :sm="12" :md="8" :lg="6" v-for="scene in list" :key="scene.id">
          <a-card hoverable @click="goChat(scene)">
            <template #cover>
              <div style="height:120px;background:linear-gradient(135deg,#667eea,#764ba2);display:flex;align-items:center;justify-content:center;color:#fff;font-size:36px">🤖</div>
            </template>
            <a-card-meta :title="scene.name" :description="scene.description" />
          </a-card>
        </a-col>
      </a-row>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAppSceneListApi } from '@/api/app/scene'
import { getCategoryListApi } from '@/api/ai/category'

const router = useRouter()
const list = ref<any[]>([])
const categories = ref<any[]>([])
const loading = ref(false)
const query = reactive({ name: '', categoryId: undefined })

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getAppSceneListApi(query)
    list.value = res.data?.list || res.data || []
  } finally { loading.value = false }
}

const goChat = (scene: any) => {
  router.push({ path: '/app/chat', query: { sceneId: scene.id, sceneName: scene.name } })
}

onMounted(async () => {
  const res: any = await getCategoryListApi()
  categories.value = res.data?.list || res.data || []
  await loadData()
})
</script>
