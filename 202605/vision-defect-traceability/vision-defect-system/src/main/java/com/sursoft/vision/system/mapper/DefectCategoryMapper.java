package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.DefectCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DefectCategoryMapper {
    DefectCategory selectById(Long id);
    DefectCategory selectByCode(String code);
    List<DefectCategory> selectList(@Param("name") String name, @Param("level") Integer level, @Param("status") Integer status);
    long countByCategory(Long categoryId);
    int insert(DefectCategory category);
    int updateById(DefectCategory category);
    int deleteById(Long id);
}
