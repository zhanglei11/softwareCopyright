package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.ApplicationLog;
import com.angu.matcher.system.domain.JobApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobApplicationMapper {
    List<JobApplication> selectList(@Param("positionId") Long positionId,
                                     @Param("status") String status);
    JobApplication selectById(Long id);
    JobApplication selectByPositionAndResume(@Param("positionId") Long positionId,
                                              @Param("resumeId") Long resumeId);
    int insert(JobApplication application);
    int updateStatus(JobApplication application);
    int insertLog(ApplicationLog log);
    List<ApplicationLog> selectLogsByApplicationId(Long applicationId);
}
