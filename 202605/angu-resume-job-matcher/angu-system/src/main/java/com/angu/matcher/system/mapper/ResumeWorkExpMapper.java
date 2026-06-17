package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.ResumeWorkExp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResumeWorkExpMapper {
    List<ResumeWorkExp> selectByResumeId(Long resumeId);
    int insert(ResumeWorkExp exp);
    int deleteByResumeId(Long resumeId);
}
