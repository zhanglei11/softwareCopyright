package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QualityAgentMapper {
    List<QualityAgent> selectList(@Param("agentName") String agentName,
                                   @Param("agentType") Integer agentType,
                                   @Param("status") Integer status);
    QualityAgent selectById(Long id);
    QualityAgent selectByCode(String agentCode);
    int insert(QualityAgent agent);
    int update(QualityAgent agent);
    int deleteById(Long id);
    /** 心跳更新（状态 + 时间） */
    int updateHeartbeat(@Param("id") Long id, @Param("status") Integer status);
}
