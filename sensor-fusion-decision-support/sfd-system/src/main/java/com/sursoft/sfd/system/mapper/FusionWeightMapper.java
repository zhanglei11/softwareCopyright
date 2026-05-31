package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.FusionWeight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FusionWeightMapper {
    List<FusionWeight> selectBySchemeId(@Param("schemeId") Long schemeId);
    List<FusionWeight> selectBySchemeAndRule(@Param("schemeId") Long schemeId,
                                              @Param("ruleId") Long ruleId);
    int insert(FusionWeight weight);
    int update(FusionWeight weight);
    void deleteBySchemeId(@Param("schemeId") Long schemeId);
}
