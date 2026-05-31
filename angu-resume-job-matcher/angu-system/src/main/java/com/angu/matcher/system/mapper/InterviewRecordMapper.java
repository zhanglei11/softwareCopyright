package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.InterviewRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InterviewRecordMapper {
    List<InterviewRecord> selectList(@Param("interviewer") String interviewer);
    InterviewRecord selectById(Long id);
    int insert(InterviewRecord record);
    int update(InterviewRecord record);
}
