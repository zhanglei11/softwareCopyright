package com.imaging.scheduler.system.service.device;

import com.imaging.scheduler.system.domain.device.DeviceParam;
import com.imaging.scheduler.system.dto.req.DeviceParamReq;

import java.util.List;

public interface DeviceParamService {
    List<DeviceParam> getParamsByDeviceId(Long deviceId);
    void saveParams(Long deviceId, DeviceParamReq req);
}
