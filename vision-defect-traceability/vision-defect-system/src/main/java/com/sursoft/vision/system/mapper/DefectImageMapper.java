package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.DefectImage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DefectImageMapper {
    List<DefectImage> selectByRecordId(Long recordId);
    int insert(DefectImage image);
}
