<template>
  <div>
    <a-card>
      <div style="margin-bottom:12px">
        <a-space>
          <a-input v-model:value="query.keyword" placeholder="角色名称/编码" allow-clear style="width:200px" @press-enter="loadData" />
          <a-button type="primary" @click="loadData"><search-outlined />搜索</a-button>
          <a-button @click="()=>{query.keyword='';loadData()}"><reload-outlined />重置</a-button>
          <a-button type="primary" @click="handleAdd"><plus-outlined />新增角色</a-button>
        </a-space>
      </div>
      <a-table :columns="columns" :data-source="tableData" :loading="loading" row-key="id">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-button type="link" size="small" @click="handleAssignMenus(record)">分配权限</a-button>
              <a-popconfirm title="确认删除？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="modalVisible" :title="modalTitle" @ok="handleSubmit" :confirm-loading="submitLoading">
      <a-form ref="formRef" :model="form" :label-col="{span:6}" :wrapper-col="{span:16}">
        <a-form-item label="角色名称" name="roleName" :rules="[{required:true,message:'请输入角色名称'}]">
          <a-input v-model:value="form.roleName" />
        </a-form-item>
        <a-form-item label="角色编码" name="roleCode" :rules="[{required:true,message:'请输入角色编码'}]">
          <a-input v-model:value="form.roleCode" />
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="form.remark" />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal v-model:open="menuModalVisible" title="分配菜单权限" @ok="handleSaveMenus" :confirm-loading="menuSaveLoading" width="500px">
      <a-tree checkable v-model:checked-keys="checkedMenuIds" :tree-data="menuTree" :field-names="{key:'id',title:'menuName',children:'children'}" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getRoleListApi, addRoleApi, editRoleApi, deleteRoleApi, getRoleMenusApi, assignRoleMenusApi } from '@/api/system/role'
import { getMenuTreeApi } from '@/api/system/menu'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<any[]>([])
const modalVisible = ref(false)
const menuModalVisible = ref(false)
const menuSaveLoading = ref(false)
const modalTitle = ref('新增角色')
const formRef = ref()
const currentRoleId = ref<number | null>(null)
const menuTree = ref<any[]>([])
const checkedMenuIds = ref<number[]>([])
const query = reactive({ keyword: '' })
const form = reactive<any>({ id: null, roleName: '', roleCode: '', remark: '' })

const columns = [
  { title: '角色名称', dataIndex: 'roleName', key: 'roleName' },
  { title: '角色编码', dataIndex: 'roleCode', key: 'roleCode' },
  { title: '备注', dataIndex: 'remark', key: 'remark' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', width: 200 },
]

async function loadData() {
  loading.value = true
  try {
    const res: any = await getRoleListApi(query)
    tableData.value = res.data || []
  } finally { loading.value = false }
}

function handleAdd() {
  modalTitle.value = '新增角色'
  Object.assign(form, { id: null, roleName: '', roleCode: '', remark: '' })
  modalVisible.value = true
}

function handleEdit(record: any) {
  modalTitle.value = '编辑角色'
  Object.assign(form, record)
  modalVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    form.id ? await editRoleApi(form.id, form) : await addRoleApi(form)
    message.success('操作成功')
    modalVisible.value = false
    loadData()
  } finally { submitLoading.value = false }
}

async function handleDelete(record: any) {
  await deleteRoleApi(record.id)
  message.success('删除成功'); loadData()
}

async function handleAssignMenus(record: any) {
  currentRoleId.value = record.id
  const [menuRes, assignedRes]: any[] = await Promise.all([getMenuTreeApi(), getRoleMenusApi(record.id)])
  menuTree.value = menuRes.data || []
  checkedMenuIds.value = assignedRes.data || []
  menuModalVisible.value = true
}

async function handleSaveMenus() {
  if (!currentRoleId.value) return
  menuSaveLoading.value = true
  try {
    await assignRoleMenusApi(currentRoleId.value, checkedMenuIds.value)
    message.success('权限已保存'); menuModalVisible.value = false
  } finally { menuSaveLoading.value = false }
}

onMounted(loadData)
</script>
