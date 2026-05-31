package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.FusionRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FusionRuleMapper {
    List<FusionRule> selectBySchemeId(@Param("schemeId") Long schemeId);
    FusionRule selectById(@Param("id") Long id);
    int insert(FusionRule rule);
    int update(FusionRule rule);
    int deleteById(@Param("id") Long id);
}
