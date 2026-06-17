package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.InterviewRecord;
import com.angu.matcher.system.domain.JobApplication;
import com.angu.matcher.system.dto.InterviewRequest;
import com.angu.matcher.system.dto.InterviewResultRequest;
import com.angu.matcher.system.mapper.InterviewRecordMapper;
import com.angu.matcher.system.mapper.JobApplicationMapper;
import com.angu.matcher.system.service.IInterviewService;
import com.angu.matcher.system.service.IJobApplicationService;
import com.angu.matcher.system.dto.ApplicationStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements IInterviewService {

    private static final Set<String> CREATABLE_STATUSES = Set.of("INTERVIEW_WAITING", "INTERVIEWING");

    private final InterviewRecordMapper interviewMapper;
    private final IJobApplicationService applicationService;

    @Override
    public List<InterviewRecord> listInterviews(String interviewer) {
        return interviewMapper.selectList(interviewer);
    }

    @Override
    public InterviewRecord getById(Long id) {
        InterviewRecord record = interviewMapper.selectById(id);
        if (record == null) throw new ServiceException(404, "面试记录不存在");
        return record;
    }

    @Override
    @Transactional
    public InterviewRecord createInterview(InterviewRequest req, Long operatorId, String operatorName) {
        JobApplication application = applicationService.getById(req.getApplicationId());
        if (!CREATABLE_STATUSES.contains(application.getStatus())) {
            throw new ServiceException(400, "当前投递状态不允许创建面试");
        }
        InterviewRecord record = new InterviewRecord();
        record.setApplicationId(req.getApplicationId());
        record.setInterviewTime(req.getInterviewTime());
        record.setInterviewer(req.getInterviewer());
        record.setLocation(req.getLocation());
        interviewMapper.insert(record);

        if ("INTERVIEW_WAITING".equals(application.getStatus())) {
            ApplicationStatusRequest statusReq = new ApplicationStatusRequest();
            statusReq.setStatus("INTERVIEWING");
            statusReq.setRemark("创建面试后进入面试中");
            applicationService.changeStatus(req.getApplicationId(), statusReq, operatorId, operatorName);
        }

        return interviewMapper.selectById(record.getId());
    }

    @Override
    public void updateInterview(Long id, InterviewRequest req) {
        getById(id);
        InterviewRecord record = new InterviewRecord();
        record.setId(id);
        record.setInterviewTime(req.getInterviewTime());
        record.setInterviewer(req.getInterviewer());
        record.setLocation(req.getLocation());
        interviewMapper.update(record);
    }

    @Override
    @Transactional
    public void fillResult(Long id, InterviewResultRequest req, Long operatorId, String operatorName) {
        InterviewRecord record = getById(id);
        record.setScore(req.getScore());
        record.setComment(req.getComment());
        record.setResult(req.getResult());
        interviewMapper.update(record);
        String newStatus = "PASS".equals(req.getResult()) ? "INTERVIEW_PASSED" : "INTERVIEW_REJECTED";
        ApplicationStatusRequest statusReq = new ApplicationStatusRequest();
        statusReq.setStatus(newStatus);
        statusReq.setRemark("面试结果：" + req.getResult());
        applicationService.changeStatus(record.getApplicationId(), statusReq, operatorId, operatorName);
    }
}
