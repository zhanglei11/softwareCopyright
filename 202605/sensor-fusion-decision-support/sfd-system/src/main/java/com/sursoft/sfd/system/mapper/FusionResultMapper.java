package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.FusionResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface FusionResultMapper {
    List<FusionResult> selectList(@Param("schemeId") Long schemeId,
                                   @Param("resultStatus") Integer resultStatus,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime);
    FusionResult selectById(@Param("id") Long id);
    int insert(FusionResult result);
    // 统计接口
    long countByDateRange(@Param("schemeId") Long schemeId,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime);
    List<Map<String, Object>> countTrendByDay(@Param("schemeId") Long schemeId,
                                               @Param("startTime") String startTime,
                                               @Param("endTime") String endTime);
    List<Map<String, Object>> countBySceneType(@Param("startTime") String startTime,
                                                @Param("endTime") String endTime);
}
