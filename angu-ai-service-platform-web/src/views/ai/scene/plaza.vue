<template>
  <div class="page-container">
    <a-row :gutter="[16, 16]">
      <a-col :span="6" v-for="scene in sceneList" :key="scene.id">
        <a-card hoverable @click="goToChat(scene)">
          <template #cover>
            <div class="scene-cover">
              <RobotOutlined style="font-size: 40px; color: #1677ff" />
            </div>
          </template>
          <a-card-meta :title="scene.sceneName || scene.name">
            <template #description>
              <p class="scene-desc">{{ scene.description || '暂无描述' }}</p>
              <a-tag color="blue">{{ scene.categoryName || '通用' }}</a-tag>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
    <a-empty v-if="!loading && sceneList.length === 0" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { RobotOutlined } from '@ant-design/icons-vue'
import { getSceneListApi } from '@/api/ai/scene'

const router = useRouter()
const loading = ref(false)
const sceneList = ref<any[]>([])

async function fetchData() {
  loading.value = true
  try {
    const res = await getSceneListApi()
    sceneList.value = res.data?.rows || res.data?.list || res.data || []
  } finally { loading.value = false }
}

function goToChat(scene: any) {
  router.push({ name: 'Chat', query: { sceneId: scene.id } })
}

onMounted(fetchData)
</script>

<style scoped>
.scene-cover {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e6f7ff 0%, #f0f5ff 100%);
}
.scene-desc {
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 44px;
  color: #8c8c8c;
}
</style>
