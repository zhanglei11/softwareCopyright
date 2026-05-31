package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.ImageFile;
import com.sva.system.domain.RecognitionTask;
import com.sva.system.mapper.ImageFileMapper;
import com.sva.system.mapper.RecognitionTaskMapper;
import com.sva.system.query.ImageQuery;
import com.sva.system.query.TaskQuery;
import com.sva.system.service.IRecognitionTaskService;
import com.sva.system.vo.TaskProgressVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecognitionTaskServiceImpl implements IRecognitionTaskService {

    private final RecognitionTaskMapper taskMapper;
    private final ImageFileMapper imageFileMapper;

    @Override
    public List<RecognitionTask> list(TaskQuery query) {
        return taskMapper.selectList(query);
    }

    @Override
    public RecognitionTask getById(Long id) {
        RecognitionTask task = taskMapper.selectById(id);
        if (task == null) throw new ServiceException(404, "任务不存在");
        return task;
    }

    @Override
    @Transactional
    public RecognitionTask create(RecognitionTask task, Long operatorId) {
        // 生成任务编号
        String taskNo = "TASK-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%06d", new Random().nextInt(999999));
        task.setTaskNo(taskNo);
        task.setCreatedBy(operatorId);
        task.setStatus(0);

        // 按分类加载图像 (selectMode=0)
        if (task.getSelectMode() != null && task.getSelectMode() == 0) {
            ImageQuery iq = new ImageQuery();
            iq.setRecognitionStatus(0);
            List<ImageFile> images = imageFileMapper.selectList(iq);
            task.setTotalCount(images.size());
            task.setProcessedCount(0);
            task.setSuccessCount(0);
            task.setFailCount(0);
            taskMapper.insert(task);
            if (!images.isEmpty()) {
                List<Long> imageIds = images.stream().map(ImageFile::getId).toList();
                taskMapper.insertTaskImageRels(task.getId(), imageIds);
            }
        } else {
            task.setTotalCount(0);
            task.setProcessedCount(0);
            task.setSuccessCount(0);
            task.setFailCount(0);
            taskMapper.insert(task);
        }
        return task;
    }

    @Override
    public void start(Long id) {
        RecognitionTask task = getById(id);
        if (task.getStatus() != 0) throw new ServiceException(400, "只有待执行状态的任务才能启动");
        task.setStatus(1);
        task.setStartedAt(LocalDateTime.now());
        taskMapper.update(task);
    }

    @Override
    public void cancel(Long id) {
        RecognitionTask task = getById(id);
        if (task.getStatus() == 2 || task.getStatus() == 3) throw new ServiceException(400, "已完成或已失败的任务无法取消");
        task.setStatus(4);
        task.setFinishedAt(LocalDateTime.now());
        taskMapper.update(task);
    }

    @Override
    public void deleteById(Long id) {
        RecognitionTask task = getById(id);
        if (task.getStatus() == 1) throw new ServiceException(400, "执行中的任务无法删除");
        taskMapper.deleteById(id);
    }

    @Override
    public TaskProgressVO getProgress(Long id) {
        RecognitionTask task = getById(id);
        TaskProgressVO vo = new TaskProgressVO();
        vo.setTaskId(id);
        vo.setStatus(task.getStatus());
        vo.setTotalCount(task.getTotalCount());
        vo.setProcessedCount(task.getProcessedCount());
        vo.setSuccessCount(task.getSuccessCount());
        vo.setFailCount(task.getFailCount());
        int total = task.getTotalCount() == null ? 0 : task.getTotalCount();
        int processed = task.getProcessedCount() == null ? 0 : task.getProcessedCount();
        vo.setProgress(total > 0 ? processed * 100.0 / total : 0.0);
        return vo;
    }
}
