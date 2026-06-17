package com.sursoft.iidp.system.process.service.impl;

import com.sursoft.iidp.common.constant.HttpStatus;
import com.sursoft.iidp.common.exception.BusinessException;
import com.sursoft.iidp.system.process.domain.ProcessExecution;
import com.sursoft.iidp.system.process.domain.ProcessTask;
import com.sursoft.iidp.system.process.mapper.ProcessExecutionMapper;
import com.sursoft.iidp.system.process.mapper.ProcessTaskMapper;
import com.sursoft.iidp.system.process.service.ProcessTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessTaskServiceImpl implements ProcessTaskService {

    private final ProcessTaskMapper taskMapper;
    private final ProcessExecutionMapper execMapper;

    @Override public List<ProcessTask> listTasks(ProcessTask query) { return taskMapper.selectList(query); }

    @Override
    public ProcessTask getById(Long id) {
        ProcessTask t = taskMapper.selectById(id);
        if (t == null) throw new BusinessException("处理任务不存在");
        return t;
    }

    @Override
    public int addTask(ProcessTask task, Long operatorId) {
        if (taskMapper.checkNameUnique(task.getTaskName(), null) > 0)
            throw new BusinessException(HttpStatus.CONFLICT, "任务名称已存在");
        task.setTaskCode(generateCode());
        task.setStatus(0);
        task.setCreatedBy(operatorId);
        return taskMapper.insert(task);
    }

    @Override
    public int editTask(ProcessTask task, Long operatorId) {
        ProcessTask existing = getById(task.getId());
        if (existing.getStatus() == 2) throw new BusinessException("执行中的任务不可编辑");
        if (taskMapper.checkNameUnique(task.getTaskName(), task.getId()) > 0)
            throw new BusinessException(HttpStatus.CONFLICT, "任务名称已存在");
        task.setUpdatedBy(operatorId);
        return taskMapper.update(task);
    }

    @Override public int removeTask(Long id) { return taskMapper.deleteById(id); }
    @Override public int updateStatus(Long id, Integer status) { return taskMapper.updateStatus(id, status); }

    @Override
    public ProcessExecution triggerExecution(Long id, Long operatorId) {
        ProcessTask task = getById(id);
        if (task.getStatus() == 0) throw new BusinessException("任务已停用，无法触发");
        if (task.getStatus() == 2) throw new BusinessException("任务已在执行中");
        taskMapper.updateStatus(id, 2);
        ProcessExecution exec = new ProcessExecution();
        exec.setExecCode(generateExecCode());
        exec.setTaskId(id);
        exec.setTaskName(task.getTaskName());
        exec.setStartTime(LocalDateTime.now());
        exec.setExecuteStatus("RUNNING");
        exec.setTotalCount(0);
        exec.setSuccessCount(0);
        exec.setFailCount(0);
        execMapper.insert(exec);
        return exec;
    }

    @Override
    public ProcessExecution terminateExecution(Long taskId) {
        ProcessExecution exec = execMapper.selectRunningByTaskId(taskId);
        if (exec == null) throw new BusinessException("未找到执行中的任务");
        exec.setExecuteStatus("TERMINATED");
        exec.setEndTime(LocalDateTime.now());
        execMapper.update(exec);
        taskMapper.updateStatus(taskId, 3);
        return exec;
    }

    @Override
    public List<ProcessExecution> listExecutions(Long taskId, String status) {
        return execMapper.selectList(taskId, status);
    }

    @Override
    public ProcessExecution getRunningExecution(Long taskId) {
        return execMapper.selectRunningByTaskId(taskId);
    }

    private String generateCode() {
        String max = taskMapper.selectMaxCode();
        if (max == null) return "PT-0001";
        try { return String.format("PT-%04d", Integer.parseInt(max.substring(3)) + 1); }
        catch (Exception e) { return "PT-" + System.currentTimeMillis(); }
    }
    private String generateExecCode() {
        String max = execMapper.selectMaxCode();
        if (max == null) return "PE-0001";
        try { return String.format("PE-%04d", Integer.parseInt(max.substring(3)) + 1); }
        catch (Exception e) { return "PE-" + System.currentTimeMillis(); }
    }
}
