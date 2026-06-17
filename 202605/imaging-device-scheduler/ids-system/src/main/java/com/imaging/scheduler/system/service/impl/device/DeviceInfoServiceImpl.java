package com.imaging.scheduler.system.service.impl.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.device.DeviceInfo;
import com.imaging.scheduler.system.dto.req.DeviceAddReq;
import com.imaging.scheduler.system.dto.req.DeviceQueryReq;
import com.imaging.scheduler.system.mapper.device.DeviceInfoMapper;
import com.imaging.scheduler.system.service.device.DeviceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceInfoServiceImpl implements DeviceInfoService {
    private final DeviceInfoMapper deviceMapper;

    @Override
    public TableDataInfo<DeviceInfo> getDeviceList(DeviceQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DeviceInfo> list = deviceMapper.selectList(req);
        PageInfo<DeviceInfo> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(pageInfo.getTotal(), req.getPage(), req.getPageSize(), list);
    }

    @Override
    public DeviceInfo getDeviceById(Long id) {
        DeviceInfo device = deviceMapper.selectById(id);
        if (device == null) throw new BusinessException(404, "设备不存在");
        return device;
    }

    @Override
    @Transactional
    public void addDevice(DeviceAddReq req, Long operatorId) {
        DeviceInfo device = new DeviceInfo();
        device.setDeviceName(req.getDeviceName());
        device.setDeviceType(req.getDeviceType());
        device.setModelSpec(req.getModelSpec());
        device.setSceneId(req.getSceneId());
        device.setIpAddress(req.getIpAddress());
        device.setLocation(req.getLocation());
        device.setRegisteredBy(operatorId);
        device.setStatus(3); // 初始离线
        device.setIsDeleted(0);
        device.setCreatedAt(LocalDateTime.now());
        device.setCreatedBy(operatorId);
        deviceMapper.insert(device);
    }

    @Override
    @Transactional
    public void editDevice(Long id, DeviceAddReq req, Long operatorId) {
        DeviceInfo device = getDeviceById(id);
        device.setDeviceName(req.getDeviceName());
        device.setDeviceType(req.getDeviceType());
        device.setModelSpec(req.getModelSpec());
        device.setSceneId(req.getSceneId());
        device.setIpAddress(req.getIpAddress());
        device.setLocation(req.getLocation());
        device.setUpdatedAt(LocalDateTime.now());
        device.setUpdatedBy(operatorId);
        deviceMapper.update(device);
    }

    @Override
    @Transactional
    public void deleteDevice(Long id, Long operatorId) {
        getDeviceById(id);
        deviceMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getStatusOverview() {
        return deviceMapper.selectStatusOverview();
    }

    @Override
    public void updateDeviceStatus(Long id, Integer status) {
        deviceMapper.updateStatus(id, status);
    }

    @Override
    public List<DeviceInfo> getAvailableDevices(Long sceneId, Integer deviceType) {
        return deviceMapper.selectAvailableDevices(sceneId, deviceType);
    }
}
