package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityAgentTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QualityAgentTaskMapper {
    List<QualityAgentTask> selectByAgentId(Long agentId);
    List<QualityAgentTask> selectByTaskId(Long taskId);
    int insert(QualityAgentTask agentTask);
    int updateStatus(@Param("id") Long id,
                     @Param("dispatchStatus") Integer dispatchStatus,
                     @Param("resultSummary") String resultSummary);
}
