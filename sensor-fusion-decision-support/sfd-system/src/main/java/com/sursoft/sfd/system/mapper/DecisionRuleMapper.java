package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.DecisionRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DecisionRuleMapper {
    List<DecisionRule> selectList(@Param("schemeId") Long schemeId,
                                   @Param("status") Integer status,
                                   @Param("keyword") String keyword);
    DecisionRule selectById(@Param("id") Long id);
    int insert(DecisionRule rule);
    int update(DecisionRule rule);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("updatedBy") Long updatedBy);
    List<DecisionRule> selectEnabledBySchemeId(@Param("schemeId") Long schemeId);
}
