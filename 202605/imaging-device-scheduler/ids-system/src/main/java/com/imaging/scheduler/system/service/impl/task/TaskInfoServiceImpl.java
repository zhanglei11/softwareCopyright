package com.imaging.scheduler.system.service.impl.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.common.enums.DispatchAction;
import com.imaging.scheduler.common.enums.TaskStatus;
import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.device.DeviceInfo;
import com.imaging.scheduler.system.domain.dispatch.DispatchLog;
import com.imaging.scheduler.system.domain.task.TaskDeviceRel;
import com.imaging.scheduler.system.domain.task.TaskInfo;
import com.imaging.scheduler.system.dto.req.TaskAddReq;
import com.imaging.scheduler.system.dto.req.TaskAssignReq;
import com.imaging.scheduler.system.dto.req.TaskQueryReq;
import com.imaging.scheduler.system.mapper.device.DeviceInfoMapper;
import com.imaging.scheduler.system.mapper.dispatch.DispatchLogMapper;
import com.imaging.scheduler.system.mapper.task.TaskDeviceRelMapper;
import com.imaging.scheduler.system.mapper.task.TaskInfoMapper;
import com.imaging.scheduler.system.service.task.TaskInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskInfoServiceImpl implements TaskInfoService {
    private final TaskInfoMapper taskMapper;
    private final TaskDeviceRelMapper relMapper;
    private final DeviceInfoMapper deviceMapper;
    private final DispatchLogMapper logMapper;

    @Override
    public TableDataInfo<TaskInfo> getTaskList(TaskQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<TaskInfo> list = taskMapper.selectList(req);
        PageInfo<TaskInfo> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(pageInfo.getTotal(), req.getPage(), req.getPageSize(), list);
    }

    @Override
    public TaskInfo getTaskById(Long id) {
        TaskInfo task = taskMapper.selectById(id);
        if (task == null) throw new BusinessException(404, "任务不存在");
        return task;
    }

    @Override
    @Transactional
    public void addTask(TaskAddReq req, Long operatorId) {
        TaskInfo task = new TaskInfo();
        task.setTaskName(req.getTaskName());
        task.setSceneId(req.getSceneId());
        task.setTaskType(req.getTaskType());
        task.setPlanStartTime(req.getPlanStartTime());
        task.setPlanEndTime(req.getPlanEndTime());
        task.setDeviceCount(req.getDeviceCount());
        task.setDeviceTypeReq(req.getDeviceTypeReq());
        task.setPriority(req.getPriority());
        task.setDescription(req.getDescription());
        task.setStatus(TaskStatus.PENDING.getCode());
        task.setIsDeleted(0);
        task.setCreatedAt(LocalDateTime.now());
        task.setCreatedBy(operatorId);
        taskMapper.insert(task);
        insertLog(task.getId(), task.getTaskCode(), null, null, operatorId, DispatchAction.CREATE, null);
    }

    @Override
    @Transactional
    public void editTask(Long id, TaskAddReq req, Long operatorId) {
        TaskInfo task = getTaskById(id);
        if (task.getStatus() != TaskStatus.PENDING.getCode()) {
            throw new BusinessException(422, "只有待分配状态的任务才能编辑");
        }
        task.setTaskName(req.getTaskName());
        task.setSceneId(req.getSceneId());
        task.setTaskType(req.getTaskType());
        task.setPlanStartTime(req.getPlanStartTime());
        task.setPlanEndTime(req.getPlanEndTime());
        task.setDeviceCount(req.getDeviceCount());
        task.setDeviceTypeReq(req.getDeviceTypeReq());
        task.setPriority(req.getPriority());
        task.setDescription(req.getDescription());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedBy(operatorId);
        taskMapper.update(task);
    }

    @Override
    @Transactional
    public void assignDevices(Long id, TaskAssignReq req, Long operatorId) {
        TaskInfo task = getTaskById(id);
        if (task.getStatus() != TaskStatus.PENDING.getCode()) {
            throw new BusinessException(422, "只有待分配状态的任务才能分配设备");
        }
        if (req.getDeviceIds().size() != task.getDeviceCount()) {
            throw new BusinessException(422, "分配设备数量必须等于任务所需设备数量 " + task.getDeviceCount());
        }
        for (Long deviceId : req.getDeviceIds()) {
            DeviceInfo device = deviceMapper.selectById(deviceId);
            if (device == null || device.getStatus() != 1) {
                throw new BusinessException(422, "设备 " + deviceId + " 不可用，请选择在线设备");
            }
            TaskDeviceRel rel = new TaskDeviceRel();
            rel.setTaskId(id);
            rel.setDeviceId(deviceId);
            rel.setStatus(1);
            rel.setAssignedAt(LocalDateTime.now());
            rel.setAssignedBy(operatorId);
            relMapper.insert(rel);
            deviceMapper.updateStatus(deviceId, 2); // 占用中
            insertLog(id, task.getTaskCode(), deviceId, device.getDeviceCode(), operatorId, DispatchAction.ASSIGN, null);
        }
        taskMapper.updateStatus(id, TaskStatus.ASSIGNED.getCode());
    }

    @Override
    @Transactional
    public void unassignDevices(Long id, Long operatorId) {
        TaskInfo task = getTaskById(id);
        if (task.getStatus() != TaskStatus.ASSIGNED.getCode()) {
            throw new BusinessException(422, "只有已分配状态的任务才能取消分配");
        }
        List<TaskDeviceRel> rels = relMapper.selectActiveByTaskId(id);
        for (TaskDeviceRel rel : rels) {
            deviceMapper.updateStatus(rel.getDeviceId(), 1); // 恢复在线
            insertLog(id, task.getTaskCode(), rel.getDeviceId(), null, operatorId, DispatchAction.UNASSIGN, null);
        }
        relMapper.releaseByTaskId(id);
        taskMapper.updateStatus(id, TaskStatus.PENDING.getCode());
    }

    @Override
    @Transactional
    public void startTask(Long id, Long operatorId) {
        TaskInfo task = getTaskById(id);
        if (task.getStatus() != TaskStatus.ASSIGNED.getCode()) {
            throw new BusinessException(422, "只有已分配状态的任务才能开始执行");
        }
        task.setActualStartTime(LocalDateTime.now());
        task.setStatus(TaskStatus.RUNNING.getCode());
        taskMapper.update(task);
        insertLog(id, task.getTaskCode(), null, null, operatorId, DispatchAction.START, null);
    }

    @Override
    @Transactional
    public void completeTask(Long id, Long operatorId) {
        TaskInfo task = getTaskById(id);
        if (task.getStatus() != TaskStatus.RUNNING.getCode()) {
            throw new BusinessException(422, "只有执行中状态的任务才能完成");
        }
        releaseDevices(id);
        task.setActualEndTime(LocalDateTime.now());
        task.setStatus(TaskStatus.COMPLETED.getCode());
        taskMapper.update(task);
        insertLog(id, task.getTaskCode(), null, null, operatorId, DispatchAction.COMPLETE, null);
    }

    @Override
    @Transactional
    public void cancelTask(Long id, Long operatorId) {
        TaskInfo task = getTaskById(id);
        int s = task.getStatus();
        if (s == TaskStatus.COMPLETED.getCode() || s == TaskStatus.CANCELLED.getCode()) {
            throw new BusinessException(422, "该状态的任务无法取消");
        }
        if (s == TaskStatus.ASSIGNED.getCode() || s == TaskStatus.RUNNING.getCode()) {
            releaseDevices(id);
        }
        task.setStatus(TaskStatus.CANCELLED.getCode());
        taskMapper.update(task);
        insertLog(id, task.getTaskCode(), null, null, operatorId, DispatchAction.CANCEL, null);
    }

    private void releaseDevices(Long taskId) {
        List<TaskDeviceRel> rels = relMapper.selectActiveByTaskId(taskId);
        for (TaskDeviceRel rel : rels) {
            deviceMapper.updateStatus(rel.getDeviceId(), 1);
        }
        relMapper.releaseByTaskId(taskId);
    }

    @Override
    @Transactional
    public void deleteTask(Long id, Long operatorId) {
        TaskInfo task = getTaskById(id);
        int s = task.getStatus();
        if (s == TaskStatus.RUNNING.getCode()) {
            throw new BusinessException(422, "运行中的任务不能删除");
        }
        if (s == TaskStatus.ASSIGNED.getCode()) {
            releaseDevices(id);
        }
        taskMapper.deleteById(id);
    }

    private void insertLog(Long taskId, String taskCode, Long deviceId, String deviceCode,
                           Long operatorId, DispatchAction action, String remark) {
        DispatchLog log = new DispatchLog();
        log.setTaskId(taskId);
        log.setAction(action.name());
        log.setActionDesc(remark);
        log.setDeviceIds(deviceId != null ? deviceId.toString() : null);
        log.setOperatorId(operatorId);
        logMapper.insert(log);
    }
}
