import request from '@/utils/request'
export const getAppSceneListApi = (params?: any) => request.get('/api/app/scenes', { params })
export const toggleFavoriteApi = (id: number) => request.post(`/api/app/scenes/${id}/favorite`)
