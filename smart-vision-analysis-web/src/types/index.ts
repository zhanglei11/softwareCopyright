export interface UserInfo {
  id?: number; username?: string; realName?: string
  avatar?: string; phone?: string; email?: string
  roles?: string[]; permissions?: string[]
}
export interface LoginParams { username: string; password: string }
export interface PageResult<T> { rows: T[]; total: number }

export interface ImageItem {
  id: number; imageNo: string; fileName: string
  fileSize: number; categoryId: number; categoryName?: string
  remark?: string; recognitionStatus: number; uploadBy?: string
  uploadTime?: string; url?: string
}
export interface TaskItem {
  id: number; taskName: string; taskStatus: number
  modelId: number; modelName?: string; confidenceThreshold: number
  totalImages?: number; processedImages?: number
  createBy?: string; createTime?: string; finishTime?: string
}
export interface ResultItem {
  id: number; taskId: number; imageId: number
  imageUrl?: string; reviewStatus: number
  recognitionBoxes?: BoundingBox[]
}
export interface BoundingBox {
  id: number; x: number; y: number; width: number; height: number
  label: string; confidence?: number; isManual?: boolean
}
export interface ModelVersion {
  id: number; modelName: string; versionNo: string
  sceneDesc?: string; supportLabels?: string[]; releaseDate?: string
  status: number; remark?: string; createTime?: string
}
