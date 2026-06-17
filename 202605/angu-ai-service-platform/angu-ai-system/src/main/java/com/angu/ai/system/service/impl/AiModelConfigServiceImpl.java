package com.angu.ai.system.service.impl;

import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.AiModelConfigDTO;
import com.angu.ai.system.domain.entity.AiModelConfig;
import com.angu.ai.system.mapper.AiModelConfigMapper;
import com.angu.ai.system.service.IAiModelConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiModelConfigServiceImpl implements IAiModelConfigService {
    private final AiModelConfigMapper modelMapper;

    @Override
    public List<AiModelConfig> list() { return modelMapper.selectAll(); }

    @Override
    public void create(AiModelConfigDTO dto) {
        AiModelConfig m = new AiModelConfig();
        copyDto(dto, m);
        modelMapper.insert(m);
    }

    @Override
    public void update(Long id, AiModelConfigDTO dto) {
        AiModelConfig m = modelMapper.selectById(id);
        if (m == null) throw new ServiceException(404, "模型配置不存在");
        copyDto(dto, m);
        m.setId(id);
        modelMapper.updateById(m);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (modelMapper.selectById(id) == null) throw new ServiceException(404, "模型配置不存在");
        modelMapper.updateStatus(id, status);
    }

    @Override
    public boolean testConnectivity(Long id) {
        // 占位：实际应调用对应 provider 的健康接口
        AiModelConfig m = modelMapper.selectById(id);
        if (m == null) throw new ServiceException(404, "模型配置不存在");
        return m.getStatus() == 1;
    }

    private void copyDto(AiModelConfigDTO dto, AiModelConfig m) {
        m.setModelName(dto.getModelName()); m.setModelId(dto.getModelId());
        m.setProvider(dto.getProvider()); m.setApiUrl(dto.getApiUrl());
        if (dto.getApiKey() != null && !dto.getApiKey().isBlank())
            m.setApiKeyEncrypted(Base64.getEncoder().encodeToString(dto.getApiKey().getBytes()));
        m.setMaxContextTokens(dto.getMaxContextTokens());
        m.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        m.setRemark(dto.getRemark());
    }
}
