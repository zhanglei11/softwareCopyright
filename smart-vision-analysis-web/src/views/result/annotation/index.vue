<template>
  <div class="annotation-page">
    <a-page-header
      :title="`标注审核 #${resultId}`"
      @back="router.back()"
    >
      <template #extra>
        <a-space>
          <a-tag :color="statusColor">{{ statusLabel }}</a-tag>
          <a-button @click="saveBoxes" :loading="saving">保存标注</a-button>
          <a-button type="primary" @click="handleConfirm" :disabled="detail?.reviewStatus === 1">
            确认通过
          </a-button>
          <a-button danger @click="handleRevision" :disabled="detail?.reviewStatus === 1">
            标记修正
          </a-button>
        </a-space>
      </template>
    </a-page-header>

    <div class="annotation-content">
      <!-- 左侧标注区 -->
      <div class="canvas-wrapper" ref="wrapperRef">
        <a-spin :spinning="loading" tip="加载中...">
          <canvas
            ref="canvasRef"
            :width="canvasWidth"
            :height="canvasHeight"
            @mousedown="onMouseDown"
            @mousemove="onMouseMove"
            @mouseup="onMouseUp"
            style="cursor: crosshair; display: block; border: 1px solid #e8e8e8;"
          />
        </a-spin>
      </div>

      <!-- 右侧标注列表 -->
      <div class="box-list">
        <div class="box-list-header">
          <span>识别框列表</span>
          <a-tag color="blue">{{ boxes.length }} 个</a-tag>
        </div>

        <a-list
          :data-source="boxes"
          size="small"
          bordered
          :style="{ maxHeight: '600px', overflowY: 'auto' }"
        >
          <template #renderItem="{ item, index }">
            <a-list-item
              :class="['box-item', { selected: selectedIdx === index }]"
              @click="selectBox(index)"
            >
              <div class="box-info">
                <a-tag :color="item.isManual ? 'orange' : 'blue'">
                  {{ item.isManual ? '手动' : 'AI' }}
                </a-tag>
                <span class="box-label">{{ item.label }}</span>
                <span v-if="item.confidence" class="box-conf">
                  {{ (item.confidence * 100).toFixed(1) }}%
                </span>
              </div>
              <div class="box-actions">
                <a-button
                  type="link"
                  size="small"
                  @click.stop="editBox(index)"
                >编辑</a-button>
                <a-button
                  type="link"
                  danger
                  size="small"
                  @click.stop="removeBox(index)"
                >删除</a-button>
              </div>
            </a-list-item>
          </template>
        </a-list>

        <div class="box-list-footer">
          <a-button block type="dashed" @click="showAddLabel = true">
            + 添加标注框
          </a-button>
          <p class="tip">提示：在图片上拖拽可直接绘制标注框</p>
        </div>
      </div>
    </div>

    <!-- 编辑标签弹窗 -->
    <a-modal
      v-model:open="labelModalVisible"
      title="编辑标签"
      @ok="confirmEditLabel"
      @cancel="cancelEditLabel"
    >
      <a-form layout="vertical">
        <a-form-item label="标签名称" required>
          <a-input v-model:value="editingLabel" placeholder="输入标签名称" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加标注提示 -->
    <a-modal
      v-model:open="showAddLabel"
      title="设置默认标签"
      @ok="showAddLabel = false"
    >
      <a-form layout="vertical">
        <a-form-item label="当前绘制标签">
          <a-input v-model:value="defaultLabel" placeholder="输入标签名称，然后在图片上拖拽绘制" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  getResultDetailApi,
  updateResultBoxesApi,
  confirmResultApi,
  markRevisionApi,
  addBoxApi,
  deleteBoxApi
} from '@/api/result/index'
import type { BoundingBox, ResultItem } from '@/types/index'

const route = useRoute()
const router = useRouter()

const resultId = Number(route.params.id)

const loading = ref(false)
const saving = ref(false)
const detail = ref<ResultItem | null>(null)
const boxes = ref<BoundingBox[]>([])
const selectedIdx = ref(-1)

const canvasRef = ref<HTMLCanvasElement | null>(null)
const wrapperRef = ref<HTMLDivElement | null>(null)
const canvasWidth = ref(800)
const canvasHeight = ref(600)
const imgElement = ref<HTMLImageElement | null>(null)
const imgScale = ref(1)
const imgOffsetX = ref(0)
const imgOffsetY = ref(0)

// 拖拽绘制状态
const isDrawing = ref(false)
const drawStart = ref({ x: 0, y: 0 })
const drawCurrent = ref({ x: 0, y: 0 })

// 标签编辑
const labelModalVisible = ref(false)
const editingLabel = ref('')
const editingIdx = ref(-1)
const defaultLabel = ref('目标')
const showAddLabel = ref(false)

const statusColor = computed(() => {
  const s = detail.value?.reviewStatus
  if (s === 1) return 'green'
  if (s === 2) return 'orange'
  if (s === 3) return 'blue'
  return 'default'
})

const statusLabel = computed(() => {
  const s = detail.value?.reviewStatus
  if (s === 1) return '已确认'
  if (s === 2) return '需修正'
  if (s === 3) return '已修正'
  return '待审核'
})

async function loadDetail() {
  loading.value = true
  try {
    const res = await getResultDetailApi(resultId)
    const data = res.data?.data || res.data
    detail.value = data
    boxes.value = [...(data?.boxes || data?.recognitionBoxes || [])]
    if (data?.imageUrl) {
      await loadImage(data.imageUrl)
    }
  } catch {
    message.error('加载识别结果失败')
  } finally {
    loading.value = false
  }
}

function loadImage(url: string): Promise<void> {
  return new Promise((resolve) => {
    const img = new Image()
    img.crossOrigin = 'anonymous'
    img.onload = () => {
      imgElement.value = img
      // 计算适合容器的尺寸
      const maxW = wrapperRef.value?.clientWidth || 800
      const maxH = 600
      const scale = Math.min(maxW / img.width, maxH / img.height, 1)
      canvasWidth.value = Math.round(img.width * scale)
      canvasHeight.value = Math.round(img.height * scale)
      imgScale.value = scale
      imgOffsetX.value = 0
      imgOffsetY.value = 0
      nextTick(() => drawCanvas())
      resolve()
    }
    img.onerror = () => resolve()
    img.src = url
  })
}

function drawCanvas() {
  const canvas = canvasRef.value
  if (!canvas) return
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  ctx.clearRect(0, 0, canvas.width, canvas.height)

  // 绘制图片
  if (imgElement.value) {
    ctx.drawImage(imgElement.value, imgOffsetX.value, imgOffsetY.value, canvasWidth.value, canvasHeight.value)
  } else {
    ctx.fillStyle = '#f5f5f5'
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    ctx.fillStyle = '#aaa'
    ctx.font = '16px Arial'
    ctx.textAlign = 'center'
    ctx.fillText('图片加载中...', canvas.width / 2, canvas.height / 2)
  }

  // 绘制所有标注框
  boxes.value.forEach((box, i) => {
    const sx = box.x * imgScale.value
    const sy = box.y * imgScale.value
    const sw = box.width * imgScale.value
    const sh = box.height * imgScale.value
    ctx.strokeStyle = i === selectedIdx.value ? '#ff4d4f' : (box.isManual ? '#fa8c16' : '#1677ff')
    ctx.lineWidth = i === selectedIdx.value ? 2.5 : 1.5
    ctx.strokeRect(sx, sy, sw, sh)

    // 标签背景
    ctx.fillStyle = i === selectedIdx.value ? '#ff4d4f' : (box.isManual ? '#fa8c16' : '#1677ff')
    const labelText = box.label + (box.confidence ? ` ${(box.confidence * 100).toFixed(0)}%` : '')
    ctx.font = '12px Arial'
    const textW = ctx.measureText(labelText).width + 6
    ctx.fillRect(sx, sy - 18, textW, 18)
    ctx.fillStyle = '#fff'
    ctx.fillText(labelText, sx + 3, sy - 4)
  })

  // 绘制正在绘制的框
  if (isDrawing.value) {
    const { x: x1, y: y1 } = drawStart.value
    const { x: x2, y: y2 } = drawCurrent.value
    ctx.strokeStyle = '#52c41a'
    ctx.lineWidth = 2
    ctx.setLineDash([5, 3])
    ctx.strokeRect(x1, y1, x2 - x1, y2 - y1)
    ctx.setLineDash([])
  }
}

function getCanvasPos(e: MouseEvent): { x: number; y: number } {
  const canvas = canvasRef.value!
  const rect = canvas.getBoundingClientRect()
  return {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  }
}

function onMouseDown(e: MouseEvent) {
  const pos = getCanvasPos(e)
  // 检查是否点击了已有框
  const hitIdx = findBoxAt(pos.x, pos.y)
  if (hitIdx >= 0) {
    selectBox(hitIdx)
    return
  }
  isDrawing.value = true
  drawStart.value = pos
  drawCurrent.value = pos
}

function onMouseMove(e: MouseEvent) {
  if (!isDrawing.value) return
  drawCurrent.value = getCanvasPos(e)
  drawCanvas()
}

function onMouseUp(e: MouseEvent) {
  if (!isDrawing.value) return
  isDrawing.value = false
  const { x: x1, y: y1 } = drawStart.value
  const { x: x2, y: y2 } = getCanvasPos(e)
  const w = Math.abs(x2 - x1)
  const h = Math.abs(y2 - y1)
  if (w > 8 && h > 8) {
    // 转换回原始坐标
    const newBox: BoundingBox = {
      id: 0,
      x: Math.round(Math.min(x1, x2) / imgScale.value),
      y: Math.round(Math.min(y1, y2) / imgScale.value),
      width: Math.round(w / imgScale.value),
      height: Math.round(h / imgScale.value),
      label: defaultLabel.value,
      isManual: true
    }
    boxes.value.push(newBox)
    selectedIdx.value = boxes.value.length - 1
    drawCanvas()
    // 立即提示编辑标签
    editBox(selectedIdx.value)
  } else {
    drawCanvas()
  }
}

function findBoxAt(cx: number, cy: number): number {
  for (let i = boxes.value.length - 1; i >= 0; i--) {
    const box = boxes.value[i]
    const sx = box.x * imgScale.value
    const sy = box.y * imgScale.value
    const sw = box.width * imgScale.value
    const sh = box.height * imgScale.value
    if (cx >= sx && cx <= sx + sw && cy >= sy && cy <= sy + sh) {
      return i
    }
  }
  return -1
}

function selectBox(idx: number) {
  selectedIdx.value = idx
  drawCanvas()
}

function editBox(idx: number) {
  editingIdx.value = idx
  editingLabel.value = boxes.value[idx]?.label || ''
  labelModalVisible.value = true
}

function confirmEditLabel() {
  if (editingIdx.value >= 0 && editingLabel.value.trim()) {
    boxes.value[editingIdx.value].label = editingLabel.value.trim()
    drawCanvas()
  }
  labelModalVisible.value = false
  editingIdx.value = -1
}

function cancelEditLabel() {
  // 如果是新绘制的框且没填标签，删除
  if (editingIdx.value >= 0 && !boxes.value[editingIdx.value].label) {
    boxes.value.splice(editingIdx.value, 1)
    drawCanvas()
  }
  labelModalVisible.value = false
  editingIdx.value = -1
}

async function removeBox(idx: number) {
  const box = boxes.value[idx]
  if (box.id > 0) {
    try {
      await deleteBoxApi(resultId, box.id)
    } catch {
      message.error('删除失败')
      return
    }
  }
  boxes.value.splice(idx, 1)
  if (selectedIdx.value === idx) selectedIdx.value = -1
  else if (selectedIdx.value > idx) selectedIdx.value--
  drawCanvas()
  message.success('已删除')
}

async function saveBoxes() {
  saving.value = true
  try {
    // 新增的框（id=0）单独调 addBox
    for (const box of boxes.value) {
      if (box.id === 0) {
        await addBoxApi(resultId, {
          x: box.x, y: box.y, width: box.width, height: box.height,
          label: box.label, isManual: true
        })
      }
    }
    // 全量更新已有框位置标签
    const existing = boxes.value.filter(b => b.id > 0)
    if (existing.length > 0) {
      await updateResultBoxesApi(resultId, { boxes: existing })
    }
    message.success('保存成功')
    await loadDetail()
  } catch {
    message.error('保存失败')
  } finally {
    saving.value = false
  }
}

async function handleConfirm() {
  try {
    await confirmResultApi(resultId)
    message.success('已确认通过')
    await loadDetail()
  } catch {
    message.error('操作失败')
  }
}

async function handleRevision() {
  try {
    await markRevisionApi(resultId)
    message.success('已标记需修正')
    await loadDetail()
  } catch {
    message.error('操作失败')
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped lang="scss">
.annotation-page {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.annotation-content {
  flex: 1;
  display: flex;
  gap: 16px;
  padding: 16px;
  overflow: hidden;
}

.canvas-wrapper {
  flex: 1;
  min-width: 0;
  overflow: auto;
  background: #fafafa;
  border-radius: 4px;
}

.box-list {
  width: 280px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.box-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 14px;
}

.box-item {
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background 0.2s;

  &.selected {
    background: #e6f4ff;
  }

  &:hover {
    background: #f5f5f5;
  }
}

.box-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.box-label {
  font-size: 13px;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.box-conf {
  font-size: 12px;
  color: #999;
}

.box-actions {
  display: flex;
  gap: 4px;
}

.box-list-footer {
  .tip {
    font-size: 12px;
    color: #999;
    margin-top: 8px;
    text-align: center;
  }
}
</style>
