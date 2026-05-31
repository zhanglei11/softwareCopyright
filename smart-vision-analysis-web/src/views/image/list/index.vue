<template>
  <div class="page-container">
    <div class="search-form">
      <a-form layout="inline">
        <a-form-item label="影像编号"><a-input v-model:value="q.imageNo" allow-clear /></a-form-item>
        <a-form-item label="分类">
          <a-select v-model:value="q.categoryId" :options="catOptions" allow-clear placeholder="全部" style="width:140px" />
        </a-form-item>
        <a-form-item label="识别状态">
          <a-select v-model:value="q.recognitionStatus" allow-clear placeholder="全部" style="width:120px">
            <a-select-option :value="0">未识别</a-select-option>
            <a-select-option :value="1">识别中</a-select-option>
            <a-select-option :value="2">已识别</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="load"><SearchOutlined /> 查询</a-button>
          <a-button style="margin-left:8px" @click="resetQ">重置</a-button>
        </a-form-item>
      </a-form>
    </div>
    <div class="table-toolbar">
      <a-space>
        <a-button type="primary" @click="uploadSingle = true"><UploadOutlined /> 上传影像</a-button>
        <a-button @click="uploadBatch = true"><FolderAddOutlined /> 批量上传(ZIP)</a-button>
      </a-space>
      <a-radio-group v-model:value="viewMode" button-style="solid">
        <a-radio-button value="table"><TableOutlined /></a-radio-button>
        <a-radio-button value="grid"><AppstoreOutlined /></a-radio-button>
      </a-radio-group>
    </div>

    <!-- 列表视图 -->
    <a-table v-if="viewMode==='table'" :columns="cols" :data-source="list" :loading="loading" :pagination="page" @change="c=>{page.current=c.current;load()}" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key==='thumb'">
          <img :src="record.thumbnailUrl || record.url" width="60" height="40" style="object-fit:cover;cursor:pointer;border-radius:4px" @click="preview(record)" />
        </template>
        <template v-if="column.key==='recognitionStatus'">
          <a-tag :color="['default','processing','success'][record.recognitionStatus]">{{ ['未识别','识别中','已识别'][record.recognitionStatus] }}</a-tag>
        </template>
        <template v-if="column.key==='fileSize'">{{ (record.fileSize/1024).toFixed(1) }} KB</template>
        <template v-if="column.key==='action'">
          <a-button size="small" type="link" @click="preview(record)">预览</a-button>
          <a-popconfirm title="确认删除？" @confirm="del(record.id)"><a-button size="small" type="link" danger>删除</a-button></a-popconfirm>
        </template>
      </template>
    </a-table>

    <!-- 缩略图视图 -->
    <div v-else>
      <a-spin :spinning="loading">
        <a-row :gutter="[12,12]">
          <a-col :span="4" v-for="img in list" :key="img.id">
            <a-card hoverable :body-style="{padding:'8px'}" @click="preview(img)">
              <img :src="img.thumbnailUrl || img.url || '/favicon.ico'" style="width:100%;height:120px;object-fit:cover;border-radius:4px" />
              <div style="margin-top:6px;font-size:12px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ img.fileName }}</div>
              <a-tag size="small" :color="['default','processing','success'][img.recognitionStatus]">{{ ['未识别','识别中','已识别'][img.recognitionStatus] }}</a-tag>
            </a-card>
          </a-col>
        </a-row>
        <div style="text-align:right;margin-top:12px">
          <a-pagination v-model:current="page.current" :total="page.total" :page-size="page.pageSize" @change="(p:number)=>{page.current=p;load()}" />
        </div>
      </a-spin>
    </div>

    <!-- 单张上传 -->
    <a-modal v-model:open="uploadSingle" title="上传影像" @ok="doUpload" :confirm-loading="uploading">
      <a-form :model="uploadForm">
        <a-form-item label="所属分类"><a-select v-model:value="uploadForm.categoryId" :options="catOptions" style="width:100%" /></a-form-item>
        <a-form-item label="备注"><a-input v-model:value="uploadForm.remark" /></a-form-item>
        <a-form-item label="影像文件">
          <a-upload :before-upload="(f:File)=>{ uploadForm.file=f; return false }" accept=".jpg,.jpeg,.png,.bmp" :max-count="1">
            <a-button><UploadOutlined /> 选择文件</a-button>
          </a-upload>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 批量上传 -->
    <a-modal v-model:open="uploadBatch" title="批量上传 (ZIP)" @ok="doBatchUpload" :confirm-loading="uploading">
      <a-form :model="uploadForm">
        <a-form-item label="所属分类"><a-select v-model:value="uploadForm.categoryId" :options="catOptions" style="width:100%" /></a-form-item>
        <a-form-item label="ZIP文件">
          <a-upload :before-upload="(f:File)=>{ uploadForm.file=f; return false }" accept=".zip" :max-count="1">
            <a-button><FolderAddOutlined /> 选择 ZIP</a-button>
          </a-upload>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 预览弹窗 -->
    <a-modal v-model:open="previewOpen" :title="previewImg?.fileName" :footer="null" width="800px">
      <img :src="previewImg?.url" style="width:100%;max-height:500px;object-fit:contain" />
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { SearchOutlined, UploadOutlined, FolderAddOutlined, TableOutlined, AppstoreOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getImageListApi, uploadImageApi, uploadBatchImageApi, deleteImageApi, getImageCategoriesApi } from '@/api/image'
const loading = ref(false); const list = ref<any[]>([])
const q = reactive<any>({ imageNo: '', categoryId: undefined, recognitionStatus: undefined })
const page = reactive({ current: 1, pageSize: 20, total: 0 })
const viewMode = ref('table')
const catOptions = ref<{ label: string; value: number }[]>([])
const uploadSingle = ref(false); const uploadBatch = ref(false); const uploading = ref(false)
const uploadForm = reactive<any>({ categoryId: undefined, remark: '', file: null })
const previewOpen = ref(false); const previewImg = ref<any>(null)
const cols = [
  { title: '缩略图', key: 'thumb', width: 80 }, { title: '影像编号', dataIndex: 'imageNo' },
  { title: '文件名', dataIndex: 'fileName' }, { title: '大小', key: 'fileSize', width: 90 },
  { title: '分类', key: 'category', customRender: ({ record }: any) => catOptions.value.find((c: any) => c.value === record.categoryId)?.label || '-' },
  { title: '识别状态', key: 'recognitionStatus' },
  { title: '上传时间', dataIndex: 'uploadedAt', customRender: ({ text }: any) => text ? text.replace('T', ' ').slice(0, 19) : '-' },
  { title: '操作', key: 'action', width: 140 },
]
async function load() { loading.value = true; try { const r = await getImageListApi({ pageNum: page.current, pageSize: page.pageSize, ...q }); list.value = r.data?.rows || r.data?.list || []; page.total = r.data?.total || 0 } finally { loading.value = false } }
function resetQ() { Object.assign(q, { imageNo: '', categoryId: undefined, recognitionStatus: undefined }); load() }
function preview(img: any) { previewImg.value = img; previewOpen.value = true }
async function loadCategories() { const r = await getImageCategoriesApi(); const flat: any[] = []; const walk = (nodes: any[]) => nodes?.forEach(n => { flat.push(n); if (n.children?.length) walk(n.children) }); walk(r.data || []); catOptions.value = flat.map((c: any) => ({ label: c.categoryName, value: c.id })) }
async function doUpload() { if (!uploadForm.file) { message.warning('请选择文件'); return }; const fd = new FormData(); fd.append('file', uploadForm.file); fd.append('categoryId', uploadForm.categoryId); fd.append('remark', uploadForm.remark || ''); uploading.value = true; try { await uploadImageApi(fd); message.success('上传成功'); uploadSingle.value = false; load() } finally { uploading.value = false } }
async function doBatchUpload() { if (!uploadForm.file) { message.warning('请选择 ZIP 文件'); return }; const fd = new FormData(); fd.append('file', uploadForm.file); fd.append('categoryId', uploadForm.categoryId); uploading.value = true; try { const r = await uploadBatchImageApi(fd); message.success(`批量上传完成：成功 ${r.data?.successCount || 0} 张`); uploadBatch.value = false; load() } finally { uploading.value = false } }
async function del(id: number) { await deleteImageApi(id); message.success('删除成功'); load() }
onMounted(() => { load(); loadCategories() })
</script>
