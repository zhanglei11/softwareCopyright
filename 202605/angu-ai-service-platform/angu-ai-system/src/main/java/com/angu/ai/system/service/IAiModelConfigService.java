package com.angu.ai.system.service;

import com.angu.ai.system.domain.dto.AiModelConfigDTO;
import com.angu.ai.system.domain.entity.AiModelConfig;

import java.util.List;

public interface IAiModelConfigService {
    List<AiModelConfig> list();
    void create(AiModelConfigDTO dto);
    void update(Long id, AiModelConfigDTO dto);
    void updateStatus(Long id, Integer status);
    boolean testConnectivity(Long id);
}
