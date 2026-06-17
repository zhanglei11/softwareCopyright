package com.imaging.scheduler.system.service.impl.dispatch;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.device.DeviceInfo;
import com.imaging.scheduler.system.domain.dispatch.DispatchConfig;
import com.imaging.scheduler.system.domain.dispatch.DispatchLog;
import com.imaging.scheduler.system.dto.req.DispatchConfigReq;
import com.imaging.scheduler.system.dto.req.DispatchLogQueryReq;
import com.imaging.scheduler.system.mapper.device.DeviceInfoMapper;
import com.imaging.scheduler.system.mapper.dispatch.DispatchConfigMapper;
import com.imaging.scheduler.system.mapper.dispatch.DispatchLogMapper;
import com.imaging.scheduler.system.mapper.task.TaskInfoMapper;
import com.imaging.scheduler.system.service.dispatch.DispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {
    private final TaskInfoMapper taskMapper;
    private final DeviceInfoMapper deviceMapper;
    private final DispatchConfigMapper configMapper;
    private final DispatchLogMapper logMapper;

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> taskStats = new HashMap<>();
        taskStats.put("pendingCount", taskMapper.countByStatus(10));
        taskStats.put("runningCount", taskMapper.countByStatus(30));
        taskStats.put("todayCompletedCount", taskMapper.countTodayByStatus(50));
        taskStats.put("todayCancelledCount", taskMapper.countTodayByStatus(-20));
        result.put("taskStats", taskStats);

        Map<String, Object> overview = deviceMapper.selectStatusOverview();
        int online = overview.getOrDefault("online", 0) instanceof Number n ? n.intValue() : 0;
        int busy = overview.getOrDefault("busy", 0) instanceof Number n ? n.intValue() : 0;
        int total = online + busy;
        Map<String, Object> deviceStats = new HashMap<>();
        deviceStats.put("onlineTotalCount", total);
        deviceStats.put("idleCount", online);
        deviceStats.put("busyCount", busy);
        deviceStats.put("busyRate", total > 0 ? Math.round(busy * 1000.0 / total) / 10.0 : 0.0);
        result.put("deviceStats", deviceStats);
        result.put("alertCount", getTimeoutAlerts().size());
        return result;
    }

    @Override
    public List<Map<String, Object>> getGanttData(Long sceneId, String date) {
        return taskMapper.selectGanttData(date, sceneId);
    }

    @Override
    public List<Map<String, Object>> getTimeoutAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        List<DeviceInfo> offlineDevices = deviceMapper.selectByStatus(3);
        for (DeviceInfo d : offlineDevices) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("type", "DEVICE_OFFLINE");
            alert.put("deviceId", d.getId());
            alert.put("deviceCode", d.getDeviceCode());
            alert.put("deviceName", d.getDeviceName());
            alerts.add(alert);
        }
        List<Map<String, Object>> timeoutTasks = taskMapper.selectTimeoutTasks();
        for (Map<String, Object> t : timeoutTasks) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("type", "TASK_TIMEOUT");
            alert.putAll(t);
            alerts.add(alert);
        }
        return alerts;
    }

    @Override
    public DispatchConfig getConfig() {
        return configMapper.selectConfig();
    }

    @Override
    public void updateConfig(DispatchConfigReq req) {
        DispatchConfig config = configMapper.selectConfig();
        if (config == null) {
            config = new DispatchConfig();
        }
        if (req.getMaxDevicesPerTask() != null) config.setMaxDevicesPerTask(req.getMaxDevicesPerTask());
        if (req.getTaskTimeoutMinutes() != null) config.setTaskTimeoutMinutes(req.getTaskTimeoutMinutes());
        if (req.getAutoDispatchEnabled() != null) config.setAutoDispatchEnabled(req.getAutoDispatchEnabled());
        if (req.getDispatchStrategy() != null) config.setDispatchStrategy(req.getDispatchStrategy());
        if (req.getAlertThresholdMinutes() != null) config.setAlertThresholdMinutes(req.getAlertThresholdMinutes());
        configMapper.update(config);
    }

    @Override
    public TableDataInfo<DispatchLog> getLogList(DispatchLogQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DispatchLog> list = logMapper.selectList(req);
        PageInfo<DispatchLog> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(pageInfo.getTotal(), req.getPage(), req.getPageSize(), list);
    }

    @Override
    public List<DispatchLog> exportLogs(DispatchLogQueryReq req) {
        return logMapper.selectAllForExport(req);
    }
}
