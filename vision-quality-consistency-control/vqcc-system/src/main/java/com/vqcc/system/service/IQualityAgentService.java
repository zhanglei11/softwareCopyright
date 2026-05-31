package com.vqcc.system.service;

import com.vqcc.system.domain.QualityAgent;
import com.vqcc.system.domain.QualityAgentTask;
import com.vqcc.system.dto.request.AgentDispatchReq;
import com.vqcc.system.dto.request.AgentRegisterReq;
import com.vqcc.system.dto.request.AgentUpdateReq;
import java.util.List;

public interface IQualityAgentService {
    List<QualityAgent> list(String agentName, Integer agentType, Integer status);
    QualityAgent getById(Long id);
    void register(AgentRegisterReq req, Long operatorId);
    void update(AgentUpdateReq req, Long operatorId);
    void delete(Long id);
    void toggleStatus(Long id, Integer status, Long operatorId);
    QualityAgentTask dispatch(AgentDispatchReq req, Long operatorId);
    List<QualityAgentTask> getAgentTasks(Long agentId);
    List<QualityAgentTask> getTaskAgents(Long taskId);
}
