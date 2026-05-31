package com.sursoft.iidp.system.ingest.mapper;

import com.sursoft.iidp.system.ingest.domain.IngestTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IngestTaskMapper {
    List<IngestTask> selectList(IngestTask query);
    IngestTask selectById(@Param("id") Long id);
    int insert(IngestTask task);
    int update(IngestTask task);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int checkNameUnique(@Param("name") String name, @Param("excludeId") Long excludeId);
    String selectMaxCode();
    long countTotal();
}
