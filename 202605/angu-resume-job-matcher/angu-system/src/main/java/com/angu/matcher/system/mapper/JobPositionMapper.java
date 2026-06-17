package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.JobPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobPositionMapper {
    List<JobPosition> selectList(@Param("title") String title,
                                  @Param("department") String department,
                                  @Param("status") String status,
                                  @Param("jobType") String jobType,
                                  @Param("eduRequire") String eduRequire);
    JobPosition selectById(Long id);
    int countApplicationsByPositionId(Long id);
    int insert(JobPosition position);
    int update(JobPosition position);
    int deleteById(Long id);
    List<JobPosition> selectOpenPositions();
}
