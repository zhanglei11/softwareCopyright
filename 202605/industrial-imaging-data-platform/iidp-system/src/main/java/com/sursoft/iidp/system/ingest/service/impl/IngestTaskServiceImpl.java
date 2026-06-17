package com.sursoft.iidp.system.ingest.service.impl;

import com.sursoft.iidp.common.constant.HttpStatus;
import com.sursoft.iidp.common.exception.BusinessException;
import com.sursoft.iidp.system.ingest.domain.IngestRecord;
import com.sursoft.iidp.system.ingest.domain.IngestTask;
import com.sursoft.iidp.system.ingest.mapper.IngestRecordMapper;
import com.sursoft.iidp.system.ingest.mapper.IngestTaskMapper;
import com.sursoft.iidp.system.ingest.service.IngestTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngestTaskServiceImpl implements IngestTaskService {

    private final IngestTaskMapper taskMapper;
    private final IngestRecordMapper recordMapper;

    @Override public List<IngestTask> listTasks(IngestTask query) { return taskMapper.selectList(query); }

    @Override
    public IngestTask getById(Long id) {
        IngestTask t = taskMapper.selectById(id);
        if (t == null) throw new BusinessException("接入任务不存在");
        return t;
    }

    @Override
    public int addTask(IngestTask task, Long operatorId) {
        if (taskMapper.checkNameUnique(task.getTaskName(), null) > 0)
            throw new BusinessException(HttpStatus.CONFLICT, "任务名称已存在");
        task.setTaskCode(generateCode());
        task.setStatus(0);
        task.setCreatedBy(operatorId);
        return taskMapper.insert(task);
    }

    @Override
    public int editTask(IngestTask task, Long operatorId) {
        if (taskMapper.checkNameUnique(task.getTaskName(), task.getId()) > 0)
            throw new BusinessException(HttpStatus.CONFLICT, "任务名称已存在");
        task.setUpdatedBy(operatorId);
        return taskMapper.update(task);
    }

    @Override public int removeTask(Long id) { return taskMapper.deleteById(id); }
    @Override public int updateStatus(Long id, Integer status) { return taskMapper.updateStatus(id, status); }

    @Override
    public IngestRecord triggerManually(Long id, Long operatorId) {
        IngestTask task = getById(id);
        if (task.getStatus() == 0) throw new BusinessException("任务已停用，无法触发");
        IngestRecord record = new IngestRecord();
        record.setRecordCode(generateRecordCode());
        record.setTaskId(id);
        record.setTaskName(task.getTaskName());
        record.setStartTime(LocalDateTime.now());
        record.setExecuteStatus("SUCCESS");
        record.setIngestCount(0);
        record.setDataSizeBytes(0L);
        record.setCostSeconds(0);
        recordMapper.insert(record);
        return record;
    }

    @Override
    public List<IngestRecord> listRecords(Long taskId, String status) {
        return recordMapper.selectList(taskId, status);
    }

    private String generateCode() {
        String max = taskMapper.selectMaxCode();
        if (max == null) return "IT-0001";
        try { return String.format("IT-%04d", Integer.parseInt(max.substring(3)) + 1); }
        catch (Exception e) { return "IT-" + System.currentTimeMillis(); }
    }
    private String generateRecordCode() {
        String max = recordMapper.selectMaxCode();
        if (max == null) return "IR-0001";
        try { return String.format("IR-%04d", Integer.parseInt(max.substring(3)) + 1); }
        catch (Exception e) { return "IR-" + System.currentTimeMillis(); }
    }
}
