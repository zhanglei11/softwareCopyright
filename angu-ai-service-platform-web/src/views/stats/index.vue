<template>
  <div>
    <a-page-header title="场景调用统计" style="padding:0 0 16px" />
    <a-card style="margin-bottom:16px">
      <a-form layout="inline" :model="query">
        <a-form-item label="日期范围">
          <a-range-picker v-model:value="query.dateRange" :value-format="'YYYY-MM-DD'" @change="loadData" />
        </a-form-item>
        <a-form-item><a-button @click="resetQuery">重置</a-button></a-form-item>
      </a-form>
    </a-card>
    <a-row :gutter="16">
      <a-col :span="14">
        <a-card title="场景调用排行">
          <a-table :columns="columns" :data-source="sceneList" :loading="loading" row-key="sceneId" :pagination="false">
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'rank'">
                <a-badge :count="index + 1" :color="index < 3 ? 'gold' : 'blue'" />
              </template>
              <template v-if="column.key === 'rate'">
                <a-progress :percent="Number((record.call_count / (sceneList[0]?.call_count || 1) * 100).toFixed(0))" size="small" />
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
      <a-col :span="10">
        <a-card title="用户使用排行">
          <a-table :columns="userColumns" :data-source="userRank" :loading="loading" row-key="userId" :pagination="false" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getSceneStatsApi, getUserRankApi } from '@/api/stats'

const sceneList = ref<any[]>([])
const userRank = ref<any[]>([])
const loading = ref(false)
const query = reactive({ dateRange: [] as any[] })
const columns = [
  { title: '排名', key: 'rank', width: 60 },
  { title: '场景名称', dataIndex: 'scene_name', key: 'sceneName' },
  { title: '调用次数', dataIndex: 'call_count', key: 'callCount' },
  { title: '占比', key: 'rate' },
]
const userColumns = [
  { title: '用户', dataIndex: 'real_name', key: 'userName' },
  { title: '调用次数', dataIndex: 'call_count', key: 'callCount' },
]
const loadData = async () => {
  loading.value = true
  const params = { startDate: query.dateRange[0], endDate: query.dateRange[1] }
  try {
    const [sRes, uRes]: any[] = await Promise.all([getSceneStatsApi(params), getUserRankApi(params)])
    sceneList.value = sRes.data?.list || sRes.data || []
    userRank.value = uRes.data?.list || uRes.data || []
  } finally { loading.value = false }
}
const resetQuery = () => { query.dateRange = []; loadData() }
onMounted(loadData)
</script>
