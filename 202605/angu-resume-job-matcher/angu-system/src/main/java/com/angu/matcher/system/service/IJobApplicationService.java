package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.ApplicationLog;
import com.angu.matcher.system.domain.JobApplication;
import com.angu.matcher.system.dto.ApplicationCreateRequest;
import com.angu.matcher.system.dto.ApplicationStatusRequest;

import java.util.List;

public interface IJobApplicationService {
    List<JobApplication> listApplications(Long positionId, String status);
    JobApplication getById(Long id);
    JobApplication createApplication(ApplicationCreateRequest req, Long operatorId);
    void changeStatus(Long id, ApplicationStatusRequest req, Long operatorId, String operatorName);
    List<ApplicationLog> getLogs(Long applicationId);
}
