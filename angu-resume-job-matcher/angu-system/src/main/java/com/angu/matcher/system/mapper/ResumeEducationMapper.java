package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.ResumeEducation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResumeEducationMapper {
    List<ResumeEducation> selectByResumeId(Long resumeId);
    int insert(ResumeEducation edu);
    int deleteByResumeId(Long resumeId);
}
