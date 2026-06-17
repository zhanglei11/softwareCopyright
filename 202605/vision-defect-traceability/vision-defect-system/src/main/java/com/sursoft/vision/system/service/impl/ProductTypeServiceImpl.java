package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.ProductType;
import com.sursoft.vision.system.dto.ProductTypeDTO;
import com.sursoft.vision.system.mapper.ProductTypeMapper;
import com.sursoft.vision.system.service.ProductTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeMapper productMapper;

    @Override
    public TableDataInfo<ProductType> list(Long lineId, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductType> list = productMapper.selectList(lineId, status);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public void add(ProductTypeDTO dto) {
        ProductType type = new ProductType();
        type.setTypeNo(dto.getTypeNo());
        type.setTypeName(dto.getTypeName());
        type.setLineId(dto.getLineId());
        type.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        type.setIsDeleted(0);
        productMapper.insert(type);
    }

    @Override
    public void edit(Long id, ProductTypeDTO dto) {
        ProductType type = productMapper.selectById(id);
        if (type == null) throw new ServiceException("产品型号不存在");
        type.setTypeName(dto.getTypeName());
        type.setLineId(dto.getLineId());
        type.setStatus(dto.getStatus());
        productMapper.updateById(type);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        ProductType type = new ProductType();
        type.setId(id);
        type.setStatus(status);
        productMapper.updateById(type);
    }

    @Override
    public void delete(Long id) {
        ProductType type = new ProductType();
        type.setId(id);
        type.setIsDeleted(1);
        productMapper.updateById(type);
    }
}
