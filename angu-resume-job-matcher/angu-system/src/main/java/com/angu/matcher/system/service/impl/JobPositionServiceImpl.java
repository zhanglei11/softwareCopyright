package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.JobPosition;
import com.angu.matcher.system.dto.JobPositionRequest;
import com.angu.matcher.system.mapper.JobPositionMapper;
import com.angu.matcher.system.service.IJobPositionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPositionServiceImpl implements IJobPositionService {

    private final JobPositionMapper positionMapper;
    private final ObjectMapper objectMapper;

    @Override
    public List<JobPosition> listPositions(String title, String department, String status,
                                            String jobType, String eduRequire) {
        return positionMapper.selectList(title, department, status, jobType, eduRequire);
    }

    @Override
    public JobPosition getById(Long id) {
        JobPosition pos = positionMapper.selectById(id);
        if (pos == null) throw new ServiceException(404, "职位不存在");
        return pos;
    }

    @Override
    public void createPosition(JobPositionRequest req, Long creatorId) {
        JobPosition pos = new JobPosition();
        copyFromRequest(pos, req);
        pos.setStatus("DRAFT");
        pos.setCreatorId(creatorId);
        positionMapper.insert(pos);
    }

    @Override
    public void updatePosition(Long id, JobPositionRequest req) {
        JobPosition pos = getById(id);
        if ("CLOSED".equals(pos.getStatus())) throw new ServiceException(400, "已关闭职位不可编辑");
        copyFromRequest(pos, req);
        positionMapper.update(pos);
    }

    @Override
    public void deletePosition(Long id) {
        JobPosition pos = getById(id);
        if (!"DRAFT".equals(pos.getStatus())) throw new ServiceException(400, "仅 DRAFT 状态可删除");
        positionMapper.deleteById(id);
    }

    @Override
    public void publishPosition(Long id) {
        JobPosition pos = getById(id);
        if ("OPEN".equals(pos.getStatus())) throw new ServiceException(400, "职位已是发布状态");
        pos.setStatus("OPEN");
        positionMapper.update(pos);
    }

    @Override
    public void closePosition(Long id) {
        JobPosition pos = getById(id);
        if (!"OPEN".equals(pos.getStatus())) throw new ServiceException(400, "只有发布中的职位可关闭");
        pos.setStatus("CLOSED");
        positionMapper.update(pos);
    }

    private void copyFromRequest(JobPosition pos, JobPositionRequest req) {
        pos.setTitle(req.getTitle());
        pos.setDepartment(req.getDepartment());
        pos.setJobType(req.getJobType());
        pos.setLocation(req.getLocation());
        pos.setSalaryMin(req.getSalaryMin());
        pos.setSalaryMax(req.getSalaryMax());
        pos.setEduRequire(req.getEduRequire());
        pos.setExpRequire(req.getExpRequire());
        pos.setDescription(req.getDescription());
        if (req.getSkillTags() != null) {
            try {
                pos.setSkillTags(objectMapper.writeValueAsString(req.getSkillTags()));
            } catch (JsonProcessingException e) {
                pos.setSkillTags("[]");
            }
        }
    }
}
