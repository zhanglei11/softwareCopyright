package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.ImageCategory;
import com.sva.system.mapper.ImageCategoryMapper;
import com.sva.system.service.IImageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageCategoryServiceImpl implements IImageCategoryService {

    private final ImageCategoryMapper categoryMapper;

    @Override
    public List<ImageCategory> tree() {
        List<ImageCategory> all = categoryMapper.selectAll();
        Map<Long, String> nameMap = all.stream().collect(java.util.stream.Collectors.toMap(ImageCategory::getId, ImageCategory::getCategoryName));
        return buildTree(all, 0L, nameMap);
    }

    @Override
    public void add(ImageCategory category, Long operatorId) {
        if (category.getParentId() == null) category.setParentId(0L);
        category.setCreatedBy(operatorId);
        categoryMapper.insert(category);
    }

    @Override
    public void update(ImageCategory category) {
        if (categoryMapper.selectById(category.getId()) == null) throw new ServiceException(404, "分类不存在");
        categoryMapper.update(category);
    }

    @Override
    public void deleteById(Long id) {
        if (categoryMapper.countImagesByCategoryId(id) > 0) throw new ServiceException(400, "分类下存在图像，无法删除");
        categoryMapper.deleteById(id);
    }

    private List<ImageCategory> buildTree(List<ImageCategory> all, Long parentId, Map<Long, String> nameMap) {
        List<ImageCategory> result = new ArrayList<>();
        for (ImageCategory c : all) {
            if (parentId.equals(c.getParentId())) {
                if (c.getParentId() != null && c.getParentId() != 0) {
                    c.setParentName(nameMap.get(c.getParentId()));
                }
                c.setChildren(buildTree(all, c.getId(), nameMap));
                result.add(c);
            }
        }
        return result;
    }
}
