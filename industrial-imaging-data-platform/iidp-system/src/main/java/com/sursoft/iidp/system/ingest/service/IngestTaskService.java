package com.sursoft.iidp.system.ingest.service;

import com.sursoft.iidp.system.ingest.domain.IngestRecord;
import com.sursoft.iidp.system.ingest.domain.IngestTask;
import java.util.List;

public interface IngestTaskService {
    List<IngestTask> listTasks(IngestTask query);
    IngestTask getById(Long id);
    int addTask(IngestTask task, Long operatorId);
    int editTask(IngestTask task, Long operatorId);
    int removeTask(Long id);
    int updateStatus(Long id, Integer status);
    IngestRecord triggerManually(Long id, Long operatorId);
    List<IngestRecord> listRecords(Long taskId, String status);
}
