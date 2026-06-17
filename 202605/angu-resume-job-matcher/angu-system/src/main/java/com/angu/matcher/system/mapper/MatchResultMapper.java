package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.MatchResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchResultMapper {
    List<MatchResult> selectByPositionId(Long positionId);
    int insertBatch(@Param("list") List<MatchResult> list);
    int deleteByPositionId(Long positionId);
}
