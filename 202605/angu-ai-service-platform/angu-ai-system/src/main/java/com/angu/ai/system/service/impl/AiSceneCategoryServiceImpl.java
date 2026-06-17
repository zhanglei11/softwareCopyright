package com.angu.ai.system.service.impl;

import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.entity.AiSceneCategory;
import com.angu.ai.system.mapper.AiSceneCategoryMapper;
import com.angu.ai.system.service.IAiSceneCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiSceneCategoryServiceImpl implements IAiSceneCategoryService {
    private final AiSceneCategoryMapper categoryMapper;

    @Override
    public List<AiSceneCategory> list() { return categoryMapper.selectAll(); }

    @Override
    public void create(AiSceneCategory category) { categoryMapper.insert(category); }

    @Override
    public void update(Long id, AiSceneCategory category) {
        if (categoryMapper.selectById(id) == null) throw new ServiceException(404, "分类不存在");
        category.setId(id);
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteById(Long id) {
        if (categoryMapper.selectById(id) == null) throw new ServiceException(404, "分类不存在");
        categoryMapper.deleteById(id);
    }
}
