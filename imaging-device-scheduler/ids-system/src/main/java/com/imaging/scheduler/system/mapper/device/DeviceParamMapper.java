package com.imaging.scheduler.system.mapper.device;

import com.imaging.scheduler.system.domain.device.DeviceParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceParamMapper {
    List<DeviceParam> selectListByDeviceId(@Param("deviceId") Long deviceId);
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
    int insertBatch(@Param("list") List<DeviceParam> list);
}
