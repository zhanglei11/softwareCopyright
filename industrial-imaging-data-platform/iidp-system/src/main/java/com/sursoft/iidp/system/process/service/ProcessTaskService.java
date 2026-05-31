package com.sursoft.iidp.system.process.service;

import com.sursoft.iidp.system.process.domain.ProcessExecution;
import com.sursoft.iidp.system.process.domain.ProcessTask;
import java.util.List;

public interface ProcessTaskService {
    List<ProcessTask> listTasks(ProcessTask query);
    ProcessTask getById(Long id);
    int addTask(ProcessTask task, Long operatorId);
    int editTask(ProcessTask task, Long operatorId);
    int removeTask(Long id);
    int updateStatus(Long id, Integer status);
    ProcessExecution triggerExecution(Long id, Long operatorId);
    ProcessExecution terminateExecution(Long taskId);
    List<ProcessExecution> listExecutions(Long taskId, String status);
    ProcessExecution getRunningExecution(Long taskId);
}
