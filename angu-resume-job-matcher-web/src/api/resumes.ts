import request from '@/utils/request'

export const resumeApi = {
  list: (params?: any) => request.get('/api/resumes', { params }),
  get: (id: number) => request.get(`/api/resumes/${id}`),
  create: (data: any) => request.post('/api/resumes', data),
  update: (id: number, data: any) => request.put(`/api/resumes/${id}`, data),
  remove: (id: number) => request.delete(`/api/resumes/${id}`),
  upload: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/api/resumes/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
}