package com.sva.system.service;

import com.sva.system.domain.RecognitionTask;
import com.sva.system.query.TaskQuery;
import com.sva.system.vo.TaskProgressVO;
import java.util.List;

public interface IRecognitionTaskService {
    List<RecognitionTask> list(TaskQuery query);
    RecognitionTask getById(Long id);
    RecognitionTask create(RecognitionTask task, Long operatorId);
    void start(Long id);
    void cancel(Long id);
    void deleteById(Long id);
    TaskProgressVO getProgress(Long id);
}
