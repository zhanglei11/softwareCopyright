package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.ApplicationLog;
import com.angu.matcher.system.domain.JobApplication;
import com.angu.matcher.system.domain.JobPosition;
import com.angu.matcher.system.dto.ApplicationCreateRequest;
import com.angu.matcher.system.dto.ApplicationStatusRequest;
import com.angu.matcher.system.mapper.JobApplicationMapper;
import com.angu.matcher.system.mapper.JobPositionMapper;
import com.angu.matcher.system.service.IJobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements IJobApplicationService {

    private final JobApplicationMapper applicationMapper;
    private final JobPositionMapper positionMapper;

    private static final Set<String> VALID_NEXT = Set.of(
        "PENDING:RESUME_PASSED", "PENDING:RESUME_REJECTED",
        "RESUME_PASSED:INTERVIEW_WAITING",
        "INTERVIEW_WAITING:INTERVIEWING",
        "INTERVIEWING:INTERVIEW_PASSED", "INTERVIEWING:INTERVIEW_REJECTED",
        "INTERVIEW_PASSED:HIRED"
    );

    @Override
    public List<JobApplication> listApplications(Long positionId, String status) {
        return applicationMapper.selectList(positionId, status);
    }

    @Override
    public JobApplication getById(Long id) {
        JobApplication app = applicationMapper.selectById(id);
        if (app == null) throw new ServiceException(404, "投递记录不存在");
        return app;
    }

    @Override
    @Transactional
    public JobApplication createApplication(ApplicationCreateRequest req, Long operatorId) {
        JobPosition pos = positionMapper.selectById(req.getPositionId());
        if (pos == null || !"OPEN".equals(pos.getStatus())) {
            throw new ServiceException(400, "该职位已关闭，无法创建投递");
        }
        JobApplication existing = applicationMapper.selectByPositionAndResume(
                req.getPositionId(), req.getResumeId());
        if (existing != null) {
            throw new ServiceException(409, "该简历已投递此职位，当前状态：" + existing.getStatus());
        }
        JobApplication app = new JobApplication();
        app.setPositionId(req.getPositionId());
        app.setResumeId(req.getResumeId());
        app.setStatus("PENDING");
        app.setOperatorId(operatorId);
        app.setRemark(req.getRemark());
        applicationMapper.insert(app);
        return applicationMapper.selectById(app.getId());
    }

    @Override
    @Transactional
    public void changeStatus(Long id, ApplicationStatusRequest req, Long operatorId, String operatorName) {
        JobApplication app = getById(id);
        String transition = app.getStatus() + ":" + req.getStatus();
        if (!VALID_NEXT.contains(transition)) {
            throw new ServiceException(400, "非法状态流转: " + app.getStatus() + " -> " + req.getStatus());
        }
        String fromStatus = app.getStatus();
        app.setStatus(req.getStatus());
        app.setOperatorId(operatorId);
        app.setRemark(req.getRemark());
        applicationMapper.updateStatus(app);

        ApplicationLog log = new ApplicationLog();
        log.setApplicationId(id);
        log.setFromStatus(fromStatus);
        log.setToStatus(req.getStatus());
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setRemark(req.getRemark());
        applicationMapper.insertLog(log);
    }

    @Override
    public List<ApplicationLog> getLogs(Long applicationId) {
        return applicationMapper.selectLogsByApplicationId(applicationId);
    }
}
