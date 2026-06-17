export interface ApiResponse<T = unknown> {
  code: number
  msg?: string
  data?: T
}

export interface PageResult<T> {
  rows: T[]
  total: number
}

export interface UserInfo {
  id: number
  username: string
  realName: string
  roles: string[]
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
}

// 数据源
export interface DatasourceConfig {
  id?: number
  name?: string
  type?: string
  datasourceCode?: string
  datasourceName?: string
  datasourceType?: string
  host?: string
  port?: number
  basePath?: string
  username?: string
  password?: string
  authType?: string
  authUsername?: string
  authPassword?: string
  authKey?: string
  dataFormat?: string
  extConfig?: string
  ownerId?: number
  status?: number
  enabled?: boolean
  description?: string
  remark?: string
  createTime?: string
}

// 接入任务
export interface IngestTask {
  id?: number
  name?: string
  taskCode?: string
  taskName?: string
  datasourceId?: number
  datasourceName?: string
  ingestType?: string
  cronExpression?: string
  filterFileTypes?: string
  fileTypeFilter?: string
  maxFileSizeMb?: number
  storageDir?: string
  status?: number
  enabled?: boolean
  description?: string
  remark?: string
  createTime?: string
}

// 接入记录
export interface IngestRecord {
  id?: number
  taskId?: number
  taskName?: string
  startTime?: string
  endTime?: string
  fileCount?: number
  totalSize?: number
  status?: string
  errorMsg?: string
}

// 处理任务
export interface ProcessTask {
  id?: number
  name?: string
  taskCode?: string
  taskName?: string
  inputDir?: string
  processType?: string
  processParams?: string
  outputDir?: string
  executeType?: string
  cronExpression?: string
  status?: number
  description?: string
  createTime?: string
}

// 处理执行
export interface ProcessExecution {
  id?: number
  taskId?: number
  taskName?: string
  startTime?: string
  endTime?: string
  progress?: number
  processedCount?: number
  status?: string
  errorMsg?: string
}

// 存储清理规则
export interface StorageCleanRule {
  id?: number
  name?: string
  ruleName?: string
  targetDir?: string
  storagePath?: string
  conditionType?: string
  conditionValue?: string
  retentionDays?: number
  maxStorageGb?: number
  executeType?: string
  cronExpression?: string
  afterAction?: string
  archiveDir?: string
  status?: number
  enabled?: boolean
  description?: string
  remark?: string
}

// 存储总览
export interface StorageOverview {
  totalSize?: number
  usedSize?: number
  freeSize?: number
  usageRate?: number
  usagePercent?: number
  totalFiles?: number
  fileCount?: number
  dirCount?: number
}

// 统计概览
export interface OverviewDTO {
  datasourceCount?: number
  activeDatasourceCount?: number
  todayIngestCount?: number
  todayIngestSize?: number
  todayProcessCount?: number
  storageUsageRate?: number
  ingestTaskCount?: number
  processTaskCount?: number
}

// 系统用户
export interface SysUser {
  id?: number
  username?: string
  realName?: string
  phone?: string
  email?: string
  dept?: string
  status?: number
  createTime?: string
  roleIds?: number[]
}

// 系统角色
export interface SysRole {
  id?: number
  roleCode?: string
  roleName?: string
  status?: number
  remark?: string
  menuIds?: number[]
}

// 菜单
export interface SysMenu {
  id?: number
  menuName?: string
  menuType?: string
  parentId?: number
  path?: string
  component?: string
  perms?: string
  icon?: string
  sortOrder?: number
  visible?: number
  status?: number
  children?: SysMenu[]
}

// 操作日志
export interface SysOperLog {
  id?: number
  operatorName?: string
  module?: string
  action?: string
  requestUrl?: string
  requestMethod?: string
  requestParam?: string
  responseCode?: number
  costTime?: number
  operTime?: string
}

// 路由 meta
export interface RouteMeta {
  title?: string
  icon?: string
  permission?: string
  hidden?: boolean
  keepAlive?: boolean
}

// 分页查询基础
export interface PageQuery {
  pageNum?: number
  pageSize?: number
}
