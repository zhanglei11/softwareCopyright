<template>
  <div>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="设备状态分布" :bordered="false" style="border-radius:8px;margin-bottom:16px">
          <v-chart :option="deviceStatusOption" style="height:260px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="任务状态分布" :bordered="false" style="border-radius:8px;margin-bottom:16px">
          <v-chart :option="taskStatusOption" style="height:260px" autoresize />
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="设备利用率趋势（近7天）" :bordered="false" style="border-radius:8px;margin-bottom:16px">
          <v-chart :option="deviceTrendOption" style="height:240px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="任务趋势（近7天）" :bordered="false" style="border-radius:8px;margin-bottom:16px">
          <v-chart :option="taskTrendOption" style="height:240px" autoresize />
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="各场景任务数" :bordered="false" style="border-radius:8px">
          <v-chart :option="bySceneOption" style="height:240px" autoresize />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="设备故障统计" :bordered="false" style="border-radius:8px">
          <v-chart :option="faultOption" style="height:240px" autoresize />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, LineChart, BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { getDeviceStatusStatsApi, getDeviceTrendApi, getTaskStatusStatsApi, getTaskTrendApi, getTaskBySceneApi, getDeviceFaultStatsApi } from '@/api/stats'

use([CanvasRenderer, PieChart, LineChart, BarChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

const deviceStatusOption = ref<any>({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: '60%', data: [] }] })
const taskStatusOption = ref<any>({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: '60%', data: [] }] })
const deviceTrendOption = ref<any>({ tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: [] }, yAxis: { type: 'value' }, series: [{ type: 'line', data: [], smooth: true }] })
const taskTrendOption = ref<any>({ tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: [] }, yAxis: { type: 'value' }, series: [{ type: 'bar', data: [] }] })
const bySceneOption = ref<any>({ tooltip: { trigger: 'axis' }, xAxis: { type: 'value' }, yAxis: { type: 'category', data: [] }, series: [{ type: 'bar', data: [] }] })
const faultOption = ref<any>({ tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: [] }, yAxis: { type: 'value' }, series: [{ type: 'bar', data: [], itemStyle: { color: '#ff4d4f' } }] })

async function loadAll() {
  try {
    const [ds, ts, dt, tt, bs, df] = await Promise.all([
      getDeviceStatusStatsApi(), getTaskStatusStatsApi(),
      getDeviceTrendApi({ days: 7 }), getTaskTrendApi({ days: 7 }),
      getTaskBySceneApi(), getDeviceFaultStatsApi(),
    ])

    // device status pie — overview: { online, busy, fault, offline }
    const dsOverview = (ds as any).data?.overview || {}
    deviceStatusOption.value = {
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { bottom: 0 },
      series: [{ type: 'pie', radius: '60%', data: [
        { name: '在线空闲', value: dsOverview.online || 0 },
        { name: '忙碌', value: dsOverview.busy || 0 },
        { name: '离线', value: dsOverview.offline || 0 },
        { name: '故障', value: dsOverview.fault || 0 },
      ].filter((d: any) => d.value > 0) }]
    }

    // task status pie — overview: { running, completed, pending, assigned, cancelled }
    const tsOverview = (ts as any).data?.overview || {}
    taskStatusOption.value = {
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { bottom: 0 },
      series: [{ type: 'pie', radius: '60%', data: [
        { name: '进行中', value: tsOverview.running || 0 },
        { name: '已完成', value: tsOverview.completed || 0 },
        { name: '待执行', value: tsOverview.pending || 0 },
        { name: '已分配', value: tsOverview.assigned || 0 },
        { name: '已取消', value: tsOverview.cancelled || 0 },
      ].filter((d: any) => d.value > 0) }]
    }

    // device trend — trend: [ { date, usedDevices } ]
    const dtTrend = (dt as any).data?.trend || []
    deviceTrendOption.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: dtTrend.map((d: any) => d.date) },
      yAxis: { type: 'value', minInterval: 1 },
      series: [{ name: '使用设备数', type: 'line', data: dtTrend.map((d: any) => d.usedDevices), smooth: true, areaStyle: { opacity: 0.3 } }]
    }

    // task trend — trend: [ { date, newTasks, completedTasks } ]
    const ttTrend = (tt as any).data?.trend || []
    taskTrendOption.value = {
      tooltip: { trigger: 'axis' },
      legend: { data: ['新建任务', '完成任务'] },
      xAxis: { type: 'category', data: ttTrend.map((d: any) => d.date) },
      yAxis: { type: 'value', minInterval: 1 },
      series: [
        { name: '新建任务', type: 'bar', data: ttTrend.map((d: any) => d.newTasks) },
        { name: '完成任务', type: 'bar', data: ttTrend.map((d: any) => d.completedTasks), itemStyle: { color: '#52c41a' } },
      ]
    }

    // by scene — data: [ { scene_name, taskCount } ]
    const bsArr = (bs as any).data?.data || []
    bySceneOption.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'value', minInterval: 1 },
      yAxis: { type: 'category', data: bsArr.map((d: any) => d.scene_name) },
      series: [{ type: 'bar', data: bsArr.map((d: any) => d.taskCount), itemStyle: { color: '#1677ff' } }]
    }

    // fault — data: [ { device_name, faultCount } ]
    const dfArr = (df as any).data?.data || []
    faultOption.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: dfArr.map((d: any) => d.device_name) },
      yAxis: { type: 'value', minInterval: 1 },
      series: [{ type: 'bar', data: dfArr.map((d: any) => d.faultCount), itemStyle: { color: '#ff4d4f' } }]
    }
  } catch (e) {
    console.error('stats load error', e)
  }
}

onMounted(loadAll)
</script>
