package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.DecisionCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DecisionConditionMapper {
    List<DecisionCondition> selectList(@Param("keyword") String keyword);
    DecisionCondition selectById(@Param("id") Long id);
    int insert(DecisionCondition condition);
    int update(DecisionCondition condition);
    int deleteById(@Param("id") Long id);
}
