package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.QualityAgent;
import com.vqcc.system.domain.QualityAgentTask;
import com.vqcc.system.dto.request.AgentDispatchReq;
import com.vqcc.system.dto.request.AgentRegisterReq;
import com.vqcc.system.dto.request.AgentUpdateReq;
import com.vqcc.system.mapper.QualityAgentMapper;
import com.vqcc.system.mapper.QualityAgentTaskMapper;
import com.vqcc.system.mapper.QualityTaskMapper;
import com.vqcc.system.service.IQualityAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityAgentServiceImpl implements IQualityAgentService {

    private final QualityAgentMapper agentMapper;
    private final QualityAgentTaskMapper agentTaskMapper;
    private final QualityTaskMapper taskMapper;

    @Override
    public List<QualityAgent> list(String agentName, Integer agentType, Integer status) {
        return agentMapper.selectList(agentName, agentType, status);
    }

    @Override
    public QualityAgent getById(Long id) {
        QualityAgent agent = agentMapper.selectById(id);
        if (agent == null) throw new BusinessException(404, "智能体不存在");
        return agent;
    }

    @Override
    @Transactional
    public void register(AgentRegisterReq req, Long operatorId) {
        if (agentMapper.selectByCode(req.getAgentCode()) != null) {
            throw new BusinessException(400, "智能体编码已存在：" + req.getAgentCode());
        }
        QualityAgent agent = new QualityAgent();
        agent.setAgentName(req.getAgentName());
        agent.setAgentCode(req.getAgentCode());
        agent.setAgentType(req.getAgentType());
        agent.setEndpointUrl(req.getEndpointUrl());
        agent.setAuthToken(req.getAuthToken());
        agent.setRemark(req.getRemark());
        agent.setCreatedBy(operatorId);
        agent.setUpdatedBy(operatorId);
        agentMapper.insert(agent);
    }

    @Override
    @Transactional
    public void update(AgentUpdateReq req, Long operatorId) {
        QualityAgent agent = agentMapper.selectById(req.getId());
        if (agent == null) throw new BusinessException(404, "智能体不存在");
        agent.setAgentName(req.getAgentName());
        agent.setAgentType(req.getAgentType());
        agent.setEndpointUrl(req.getEndpointUrl());
        agent.setAuthToken(req.getAuthToken());
        agent.setRemark(req.getRemark());
        agent.setUpdatedBy(operatorId);
        agentMapper.update(agent);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        QualityAgent agent = agentMapper.selectById(id);
        if (agent == null) throw new BusinessException(404, "智能体不存在");
        agentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleStatus(Long id, Integer status, Long operatorId) {
        QualityAgent agent = agentMapper.selectById(id);
        if (agent == null) throw new BusinessException(404, "智能体不存在");
        if (status != 0 && status != 1) throw new BusinessException(400, "状态值非法，0=离线 1=空闲");
        agentMapper.updateHeartbeat(id, status);
    }

    @Override
    @Transactional
    public QualityAgentTask dispatch(AgentDispatchReq req, Long operatorId) {
        QualityAgent agent = agentMapper.selectById(req.getAgentId());
        if (agent == null) throw new BusinessException(404, "智能体不存在");
        if (agent.getStatus() == 0) throw new BusinessException(400, "智能体当前离线，无法调度");
        if (taskMapper.selectById(req.getTaskId()) == null) {
            throw new BusinessException(404, "检测任务不存在");
        }
        QualityAgentTask record = new QualityAgentTask();
        record.setAgentId(req.getAgentId());
        record.setTaskId(req.getTaskId());
        record.setCreatedBy(operatorId);
        agentTaskMapper.insert(record);
        // 标记智能体为运行中
        agentMapper.updateHeartbeat(req.getAgentId(), 2);
        return record;
    }

    @Override
    public List<QualityAgentTask> getAgentTasks(Long agentId) {
        return agentTaskMapper.selectByAgentId(agentId);
    }

    @Override
    public List<QualityAgentTask> getTaskAgents(Long taskId) {
        return agentTaskMapper.selectByTaskId(taskId);
    }
}
