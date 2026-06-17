package com.imaging.scheduler.system.service.impl.device;

import com.imaging.scheduler.system.domain.device.DeviceParam;
import com.imaging.scheduler.system.dto.req.DeviceParamReq;
import com.imaging.scheduler.system.mapper.device.DeviceParamMapper;
import com.imaging.scheduler.system.service.device.DeviceParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceParamServiceImpl implements DeviceParamService {

    private final DeviceParamMapper deviceParamMapper;

    @Override
    public List<DeviceParam> getParamsByDeviceId(Long deviceId) {
        return deviceParamMapper.selectListByDeviceId(deviceId);
    }

    @Override
    @Transactional
    public void saveParams(Long deviceId, DeviceParamReq req) {
        deviceParamMapper.deleteByDeviceId(deviceId);
        if (req.getParams() == null || req.getParams().isEmpty()) {
            return;
        }
        List<DeviceParam> list = new ArrayList<>();
        for (DeviceParamReq.ParamItem item : req.getParams()) {
            DeviceParam p = new DeviceParam();
            p.setDeviceId(deviceId);
            p.setParamKey(item.getParamKey());
            p.setParamValue(item.getParamValue());
            p.setParamDesc(item.getParamDesc());
            list.add(p);
        }
        deviceParamMapper.insertBatch(list);
    }
}
