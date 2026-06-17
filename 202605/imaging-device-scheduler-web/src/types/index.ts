export interface AjaxResult<T = unknown> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

// Device
export interface DeviceInfo {
  id: number
  deviceCode: string
  deviceName: string
  deviceType: number
  modelSpec: string
  sceneId: number
  sceneName?: string
  ipAddress: string
  location: string
  status: number
  lastHeartbeat: string
  createTime: string
}

export interface DeviceParam {
  paramKey: string
  paramValue: string
  paramDesc: string
}

// Scene
export interface SceneGroup {
  id: number
  groupCode: string
  groupName: string
  remark: string
}

export interface SceneInfo {
  id: number
  sceneCode: string
  sceneName: string
  sceneType: number
  groupId: number
  groupName?: string
  description: string
  status: number
  createTime: string
}

// Task
export interface TaskInfo {
  id: number
  taskCode: string
  taskName: string
  sceneId: number
  sceneName?: string
  taskType: number
  planStartTime: string
  planEndTime: string
  actualStartTime: string
  actualEndTime: string
  deviceCount: number
  deviceTypeReq: number
  priority: number
  description: string
  status: number
  createTime: string
}

// Dispatch
export interface DispatchConfig {
  id: number
  maxDevicesPerTask: number
  taskTimeoutMinutes: number
  autoDispatchEnabled: number
  dispatchStrategy: string
  alertThresholdMinutes: number
}

export interface DispatchLog {
  id: number
  taskId: number
  taskName: string
  action: string
  actionDesc: string
  deviceIds: string
  operatorName: string
  createdAt: string
}

// System
export interface SysUser {
  id: number
  username: string
  realName: string
  email: string
  phone: string
  status: number
  createTime: string
  roles?: string[]
}

export interface SysRole {
  id: number
  roleCode: string
  roleName: string
  remark: string
  status: number
  createTime: string
}

export interface SysMenu {
  id: number
  menuName: string
  menuType: number
  parentId: number
  path: string
  component: string
  perms: string
  icon: string
  orderNum: number
  status: number
  children?: SysMenu[]
}
