package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.ModelVersion;
import com.sva.system.domain.RecognitionTask;
import com.sva.system.mapper.ModelVersionMapper;
import com.sva.system.service.IModelVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelVersionServiceImpl implements IModelVersionService {

    private final ModelVersionMapper modelMapper;

    @Override public List<ModelVersion> list(String modelName, Integer status) { return modelMapper.selectList(modelName, status); }
    @Override public ModelVersion getById(Long id) {
        ModelVersion m = modelMapper.selectById(id);
        if (m == null) throw new ServiceException(404, "模型版本不存在");
        return m;
    }
    @Override public void add(ModelVersion version) { version.setStatus(1); version.setDeleted(0); modelMapper.insert(version); }
    @Override public void update(Long id, ModelVersion version) { version.setId(id); modelMapper.update(version); }
    @Override public void delete(Long id) {
        if (modelMapper.countTasksByModelId(id) > 0) throw new ServiceException(400, "该模型版本已被任务使用，无法删除");
        modelMapper.deleteById(id);
    }
    @Override public void deprecate(Long id) { getById(id); modelMapper.updateStatus(id, 0); }
    @Override public void restore(Long id) { getById(id); modelMapper.updateStatus(id, 1); }
    @Override public List<RecognitionTask> getTaskHistory(Long id) { return modelMapper.selectTasksByModelId(id); }
}
