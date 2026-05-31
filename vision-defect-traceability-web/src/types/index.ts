// ===== 通用响应 =====
export interface ApiResponse<T = unknown> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T = unknown> {
  rows: T[]
  total: number
}

export interface PaginationParams {
  pageNum?: number
  pageSize?: number
}

// ===== 用户/认证 =====
export interface LoginParams {
  username: string
  password: string
}

export interface UserInfo {
  userId: number
  realName: string
  roles: string[]
  perms: string[]
}

export interface LoginResult {
  accessToken: string
  expiresIn: number
  userInfo: UserInfo
}

// ===== 产线 =====
export interface LineInfo {
  id: number
  lineNo: string
  lineName: string
  workshop?: string
  managerId?: number
  status: number
  remark?: string
  createdAt?: string
}

export interface LineDTO {
  lineNo: string
  lineName: string
  workshop?: string
  managerId?: number
  remark?: string
}

// ===== 产品型号 =====
export interface ProductType {
  id: number
  typeNo: string
  typeName: string
  lineId: number
  status: number
  createdAt?: string
}

// ===== 缺陷分类 =====
export interface DefectCategory {
  id: number
  code: string
  name: string
  level?: number
  description?: string
  status: number
  createdAt?: string
}

// ===== 缺陷记录 =====
export interface DefectRecord {
  id: number
  serialNo: string
  batchNo: string
  lineName: string
  productTypeName: string
  categoryName: string
  level: number
  levelLabel: string
  result: number
  resultLabel: string
  shift: string
  detectTime: string
  disposeStatus: number
  disposeStatusLabel: string
}

export interface DefectRecordQuery extends PaginationParams {
  serialNo?: string
  batchNo?: string
  lineId?: number
  categoryId?: number
  result?: number
  disposeStatus?: number
  startTime?: string
  endTime?: string
}

// ===== 告警规则 =====
export interface AlertRule {
  id: number
  ruleName: string
  lineId: number
  conditionType: number
  threshold: number
  statCycle: number
  alertLevel: number
  notifyUserIds?: string
  status: number
  createdAt?: string
}

// ===== 告警记录 =====
export interface AlertRecord {
  id: number
  ruleId: number
  alertContent: string
  alertTime: string
  handleStatus: number
  handleRemark?: string
  handleBy?: number
  handleAt?: string
  createdAt?: string
}

// ===== 系统用户/角色 =====
export interface SysUser {
  id: number
  username: string
  realName: string
  phone?: string
  department?: string
  status: number
  createdAt?: string
}

export interface SysRole {
  id: number
  roleName: string
  roleKey: string
  status: number
  remark?: string
}

export interface SysMenu {
  id: number
  menuName: string
  parentId: number
  orderNum: number
  perm?: string
  menuType: number
  status: number
  children?: SysMenu[]
}

// ===== Dashboard =====
export interface DashboardVO {
  todayTotal: number
  todayQualifiedRate: number
  todayDefectCount: number
  monthTrend: Array<{ date: string; qualifiedRate: number }>
  categoryDistribution: Array<{ name: string; value: number }>
  lineComparison: Array<{ lineName: string; defectCount: number }>
}

// ===== 追溯 =====
export interface BatchTraceVO {
  batchNo: string
  lineName: string
  totalCount: number
  qualifiedCount: number
  defectCount: number
  qualifiedRate: number
  categoryDistribution: Array<{ categoryName: string; count: number; ratio: number }>
  records: DefectRecord[]
}

// ===== 路由 Meta =====
export interface RouteMeta {
  title?: string
  icon?: string
  requiresAuth?: boolean
  permission?: string
  permissions?: string[]
  hidden?: boolean
}
