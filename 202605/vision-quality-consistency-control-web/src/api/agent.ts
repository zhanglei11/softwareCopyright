import http from './http'

export const agentApi = {
  list:         (params?: any) => http.get('/v1/agents', { params }),
  get:          (id: number)   => http.get(`/v1/agents/${id}`),
  register:     (data: any)    => http.post('/v1/agents', data),
  update:       (data: any)    => http.put('/v1/agents', data),
  delete:       (id: number)   => http.delete(`/v1/agents/${id}`),
  toggleStatus: (id: number, status: number) =>
                  http.post(`/v1/agents/${id}/toggle-status`, null, { params: { status } }),
  dispatch:     (data: any)    => http.post('/v1/agents/dispatch', data),
  agentTasks:   (agentId: number) => http.get(`/v1/agents/${agentId}/tasks`),
  taskAgents:   (taskId: number)  => http.get(`/v1/agents/by-task/${taskId}`),
}
