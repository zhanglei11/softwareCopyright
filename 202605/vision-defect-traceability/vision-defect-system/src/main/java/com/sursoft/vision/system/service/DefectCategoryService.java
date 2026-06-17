package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.DefectCategory;
import com.sursoft.vision.system.dto.DefectCategoryDTO;

public interface DefectCategoryService {
    TableDataInfo<DefectCategory> list(String name, Integer level, Integer status, int pageNum, int pageSize);
    void add(DefectCategoryDTO dto);
    void edit(Long id, DefectCategoryDTO dto);
    void updateStatus(Long id, Integer status);
    void delete(Long id);
}
