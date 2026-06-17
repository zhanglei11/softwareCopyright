package com.angu.ai.system.service;

import com.angu.ai.system.domain.entity.AiSceneCategory;

import java.util.List;

public interface IAiSceneCategoryService {
    List<AiSceneCategory> list();
    void create(AiSceneCategory category);
    void update(Long id, AiSceneCategory category);
    void deleteById(Long id);
}
