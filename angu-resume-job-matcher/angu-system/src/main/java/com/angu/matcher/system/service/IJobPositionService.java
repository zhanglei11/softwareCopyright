package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.JobPosition;
import com.angu.matcher.system.dto.JobPositionRequest;

import java.util.List;

public interface IJobPositionService {
    List<JobPosition> listPositions(String title, String department, String status,
                                     String jobType, String eduRequire);
    JobPosition getById(Long id);
    void createPosition(JobPositionRequest req, Long creatorId);
    void updatePosition(Long id, JobPositionRequest req);
    void deletePosition(Long id);
    void publishPosition(Long id);
    void closePosition(Long id);
}
