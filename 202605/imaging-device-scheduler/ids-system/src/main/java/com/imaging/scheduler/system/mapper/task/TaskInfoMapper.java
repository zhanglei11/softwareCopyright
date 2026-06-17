package com.imaging.scheduler.system.mapper.task;

import com.imaging.scheduler.system.domain.task.TaskInfo;
import com.imaging.scheduler.system.dto.req.TaskQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskInfoMapper {
    List<TaskInfo> selectList(TaskQueryReq req);
    TaskInfo selectById(@Param("id") Long id);
    int insert(TaskInfo task);
    int update(TaskInfo task);
    int updateStatus(@Param("id") Long id, @Param("status") int status);
    int deleteById(@Param("id") Long id);
    int countByStatus(@Param("status") int status);
    int countTodayByStatus(@Param("status") int status);
    List<Map<String, Object>> selectGanttData(@Param("date") String date, @Param("sceneId") Long sceneId);
    List<Map<String, Object>> selectTimeoutTasks();
    Map<String, Object> selectTaskOverview();
    List<Map<String, Object>> selectStatusBreakdown();
    List<Map<String, Object>> selectTaskByScene();
    List<Map<String, Object>> selectTaskTrend(@Param("days") int days);
}
