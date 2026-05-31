package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.InterviewRecord;
import com.angu.matcher.system.dto.InterviewRequest;
import com.angu.matcher.system.dto.InterviewResultRequest;

import java.util.List;

public interface IInterviewService {
    List<InterviewRecord> listInterviews(String interviewer);
    InterviewRecord getById(Long id);
    InterviewRecord createInterview(InterviewRequest req, Long operatorId, String operatorName);
    void updateInterview(Long id, InterviewRequest req);
    void fillResult(Long id, InterviewResultRequest req, Long operatorId, String operatorName);
}
