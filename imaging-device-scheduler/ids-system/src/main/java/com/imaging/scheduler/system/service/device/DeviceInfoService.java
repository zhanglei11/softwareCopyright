package com.imaging.scheduler.system.service.device;

import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.device.DeviceInfo;
import com.imaging.scheduler.system.dto.req.DeviceAddReq;
import com.imaging.scheduler.system.dto.req.DeviceQueryReq;

import java.util.List;
import java.util.Map;

public interface DeviceInfoService {
    TableDataInfo<DeviceInfo> getDeviceList(DeviceQueryReq req);
    DeviceInfo getDeviceById(Long id);
    void addDevice(DeviceAddReq req, Long operatorId);
    void editDevice(Long id, DeviceAddReq req, Long operatorId);
    void updateDeviceStatus(Long id, Integer status);
    void deleteDevice(Long id, Long operatorId);
    Map<String, Object> getStatusOverview();
    List<DeviceInfo> getAvailableDevices(Long sceneId, Integer deviceType);
}
