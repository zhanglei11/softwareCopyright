package com.sva.system.mapper;

import com.sva.system.domain.ImageFile;
import com.sva.system.query.ImageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ImageFileMapper {
    List<ImageFile> selectList(ImageQuery query);
    ImageFile selectById(@Param("id") Long id);
    int insert(ImageFile imageFile);
    int deleteById(@Param("id") Long id);
    List<ImageFile> selectByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
    int countByTaskId(@Param("taskId") Long taskId);
    int updateRecognitionStatus(@Param("id") Long id, @Param("status") Integer status);
}
