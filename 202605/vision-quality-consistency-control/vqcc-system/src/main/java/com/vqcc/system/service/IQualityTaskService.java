package com.vqcc.system.service;

import com.vqcc.system.domain.QualityDetectionRecord;
import com.vqcc.system.domain.QualityTask;
import com.vqcc.system.dto.request.DetectionRecordSubmitReq;
import com.vqcc.system.dto.request.TaskCreateReq;
import java.util.List;

public interface IQualityTaskService {
    List<QualityTask> list(String taskName, String detectionTarget, Integer status, Long templateId);
    QualityTask getById(Long id);
    void create(TaskCreateReq req, Long operatorId);
    void startTask(Long id, Long operatorId);
    void completeTask(Long id, Long operatorId);
    void cancelTask(Long id, Long operatorId);
    QualityDetectionRecord submitRecord(DetectionRecordSubmitReq req, Long operatorId);
    List<QualityDetectionRecord> getRecords(Long taskId, Integer isQualified);
}
