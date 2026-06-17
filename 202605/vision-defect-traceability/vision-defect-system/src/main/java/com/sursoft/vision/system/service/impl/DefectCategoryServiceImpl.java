package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.DefectCategory;
import com.sursoft.vision.system.dto.DefectCategoryDTO;
import com.sursoft.vision.system.mapper.DefectCategoryMapper;
import com.sursoft.vision.system.service.DefectCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefectCategoryServiceImpl implements DefectCategoryService {

    private final DefectCategoryMapper categoryMapper;

    @Override
    public TableDataInfo<DefectCategory> list(String name, Integer level, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DefectCategory> list = categoryMapper.selectList(name, level, status);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public void add(DefectCategoryDTO dto) {
        if (categoryMapper.selectByCode(dto.getCode()) != null) {
            throw new ServiceException("缺陷编码已存在");
        }
        DefectCategory category = new DefectCategory();
        category.setCode(dto.getCode());
        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        category.setIsDeleted(0);
        categoryMapper.insert(category);
    }

    @Override
    public void edit(Long id, DefectCategoryDTO dto) {
        DefectCategory category = categoryMapper.selectById(id);
        if (category == null) throw new ServiceException("缺陷分类不存在");
        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());
        categoryMapper.updateById(category);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        DefectCategory category = new DefectCategory();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        long count = categoryMapper.countByCategory(id);
        if (count > 0) {
            throw new ServiceException("该缺陷分类已有关联记录，无法删除，可将其停用");
        }
        categoryMapper.deleteById(id);
    }
}
