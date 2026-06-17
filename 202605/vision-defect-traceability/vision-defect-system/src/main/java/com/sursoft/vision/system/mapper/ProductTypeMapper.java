package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.ProductType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductTypeMapper {
    ProductType selectById(Long id);
    List<ProductType> selectList(@Param("lineId") Long lineId, @Param("status") Integer status);
    int insert(ProductType type);
    int updateById(ProductType type);
    int deleteById(Long id);
}
