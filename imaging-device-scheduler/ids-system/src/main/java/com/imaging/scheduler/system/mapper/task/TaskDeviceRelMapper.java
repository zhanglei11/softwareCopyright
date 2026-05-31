package com.imaging.scheduler.system.mapper.task;

import com.imaging.scheduler.system.domain.task.TaskDeviceRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskDeviceRelMapper {
    List<TaskDeviceRel> selectActiveByTaskId(@Param("taskId") Long taskId);
    int insert(TaskDeviceRel rel);
    int releaseByTaskId(@Param("taskId") Long taskId);
    int countActiveByDeviceId(@Param("deviceId") Long deviceId);
}
