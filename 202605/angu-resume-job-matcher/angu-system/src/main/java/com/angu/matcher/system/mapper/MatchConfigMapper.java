package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.MatchConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchConfigMapper {
    MatchConfig selectOne();
    int updateConfig(MatchConfig config);
}
