package com.sursoft.iidp.system.process.mapper;

import com.sursoft.iidp.system.process.domain.ProcessTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProcessTaskMapper {
    List<ProcessTask> selectList(ProcessTask query);
    ProcessTask selectById(@Param("id") Long id);
    int insert(ProcessTask task);
    int update(ProcessTask task);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int checkNameUnique(@Param("name") String name, @Param("excludeId") Long excludeId);
    String selectMaxCode();
    long countTotal();
    long countByStatus(@Param("status") Integer status);
}
