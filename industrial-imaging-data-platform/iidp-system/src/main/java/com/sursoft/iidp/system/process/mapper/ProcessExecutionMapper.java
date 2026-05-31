package com.sursoft.iidp.system.process.mapper;

import com.sursoft.iidp.system.process.domain.ProcessExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProcessExecutionMapper {
    List<ProcessExecution> selectList(@Param("taskId") Long taskId, @Param("status") String status);
    ProcessExecution selectById(@Param("id") Long id);
    ProcessExecution selectRunningByTaskId(@Param("taskId") Long taskId);
    int insert(ProcessExecution exec);
    int update(ProcessExecution exec);
    String selectMaxCode();
}
