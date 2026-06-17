package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.ProductType;
import com.sursoft.vision.system.dto.ProductTypeDTO;

public interface ProductTypeService {
    TableDataInfo<ProductType> list(Long lineId, Integer status, int pageNum, int pageSize);
    void add(ProductTypeDTO dto);
    void edit(Long id, ProductTypeDTO dto);
    void updateStatus(Long id, Integer status);
    void delete(Long id);
}
