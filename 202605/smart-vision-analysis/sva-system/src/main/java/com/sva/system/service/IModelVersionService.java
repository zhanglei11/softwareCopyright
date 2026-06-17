package com.sva.system.service;
import com.sva.system.domain.ModelVersion;
import com.sva.system.domain.RecognitionTask;
import java.util.List;
public interface IModelVersionService {
    List<ModelVersion> list(String modelName, Integer status);
    ModelVersion getById(Long id);
    void add(ModelVersion version);
    void update(Long id, ModelVersion version);
    void delete(Long id);
    void deprecate(Long id);
    void restore(Long id);
    List<RecognitionTask> getTaskHistory(Long id);
}
