package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.ResumeSkill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResumeSkillMapper {
    List<ResumeSkill> selectByResumeId(Long resumeId);
    int insert(ResumeSkill skill);
    int deleteByResumeId(Long resumeId);
}
