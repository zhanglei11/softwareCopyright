package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.AlertRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AlertRuleMapper {
    AlertRule selectById(Long id);
    List<AlertRule> selectList(@Param("status") Integer status);
    List<AlertRule> selectEnabledRules();
    int insert(AlertRule rule);
    int updateById(AlertRule rule);
    int deleteById(Long id);
}
