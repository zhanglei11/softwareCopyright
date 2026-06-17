package com.imaging.scheduler.system.mapper.device;

import com.imaging.scheduler.system.domain.device.DeviceFaultRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceFaultRecordMapper {
    List<DeviceFaultRecord> selectByDeviceId(@Param("deviceId") Long deviceId);
    List<Map<String, Object>> selectFaultStat();
    int insert(DeviceFaultRecord record);
}
