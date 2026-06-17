package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.AlertRecord;
import com.sursoft.vision.system.query.AlertRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AlertRecordMapper {
    AlertRecord selectById(Long id);
    List<AlertRecord> selectList(AlertRecordQuery query);
    int insert(AlertRecord record);
    int updateById(AlertRecord record);
}
