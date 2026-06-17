package com.imaging.scheduler.system.mapper.device;

import com.imaging.scheduler.system.domain.device.DeviceInfo;
import com.imaging.scheduler.system.dto.req.DeviceQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceInfoMapper {
    List<DeviceInfo> selectList(DeviceQueryReq req);
    DeviceInfo selectById(@Param("id") Long id);
    List<DeviceInfo> selectByStatus(@Param("status") int status);
    List<DeviceInfo> selectAvailableDevices(@Param("sceneId") Long sceneId, @Param("deviceType") Integer deviceType);
    int insert(DeviceInfo device);
    int update(DeviceInfo device);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") int status);
    Map<String, Object> selectStatusOverview();
    Map<String, Object> selectStatOverview();
    List<Map<String, Object>> selectStatTrend(@Param("days") int days);
    List<Map<String, Object>> selectStatByScene();
}
