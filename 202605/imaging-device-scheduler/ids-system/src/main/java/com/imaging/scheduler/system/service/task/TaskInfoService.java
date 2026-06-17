package com.imaging.scheduler.system.service.task;

import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.task.TaskInfo;
import com.imaging.scheduler.system.dto.req.TaskAddReq;
import com.imaging.scheduler.system.dto.req.TaskAssignReq;
import com.imaging.scheduler.system.dto.req.TaskQueryReq;

public interface TaskInfoService {
    TableDataInfo<TaskInfo> getTaskList(TaskQueryReq req);
    TaskInfo getTaskById(Long id);
    void addTask(TaskAddReq req, Long operatorId);
    void editTask(Long id, TaskAddReq req, Long operatorId);
    void assignDevices(Long id, TaskAssignReq req, Long operatorId);
    void unassignDevices(Long id, Long operatorId);
    void startTask(Long id, Long operatorId);
    void completeTask(Long id, Long operatorId);
    void cancelTask(Long id, Long operatorId);
    void deleteTask(Long id, Long operatorId);
}
