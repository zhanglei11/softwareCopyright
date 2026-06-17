package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.DecisionResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface DecisionResultMapper {
    List<DecisionResult> selectList(@Param("ruleId") Long ruleId,
                                     @Param("schemeId") Long schemeId,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("keyword") String keyword);
    DecisionResult selectById(@Param("id") Long id);
    DecisionResult selectWithFusionResult(@Param("id") Long id);
    int insert(DecisionResult result);
    // 统计接口
    long countByDateRange(@Param("startTime") String startTime,
                          @Param("endTime") String endTime);
    List<Map<String, Object>> countByRule(@Param("startTime") String startTime,
                                           @Param("endTime") String endTime);
    List<Map<String, Object>> avgResponseTime(@Param("startTime") String startTime,
                                               @Param("endTime") String endTime);
}
