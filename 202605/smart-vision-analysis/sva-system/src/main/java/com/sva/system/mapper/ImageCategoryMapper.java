package com.sva.system.mapper;

import com.sva.system.domain.ImageCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ImageCategoryMapper {
    List<ImageCategory> selectAll();
    ImageCategory selectById(@Param("id") Long id);
    int insert(ImageCategory category);
    int update(ImageCategory category);
    int deleteById(@Param("id") Long id);
    long countImagesByCategoryId(@Param("categoryId") Long categoryId);
}
