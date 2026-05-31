package com.imaging.scheduler.system.service.impl.dispatch;

import com.imaging.scheduler.system.mapper.device.DeviceFaultRecordMapper;
import com.imaging.scheduler.system.mapper.device.DeviceInfoMapper;
import com.imaging.scheduler.system.mapper.task.TaskInfoMapper;
import com.imaging.scheduler.system.service.dispatch.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final DeviceInfoMapper deviceMapper;
    private final DeviceFaultRecordMapper faultMapper;
    private final TaskInfoMapper taskMapper;

    @Override
    public Map<String, Object> getDeviceStatusStat() {
        Map<String, Object> result = new HashMap<>();
        result.put("overview", deviceMapper.selectStatOverview());
        result.put("byScene", deviceMapper.selectStatByScene());
        return result;
    }

    @Override
    public Map<String, Object> getDeviceTrend(int days) {
        Map<String, Object> result = new HashMap<>();
        result.put("trend", deviceMapper.selectStatTrend(days));
        return result;
    }

    @Override
    public Map<String, Object> getTaskStatusStat() {
        Map<String, Object> result = new HashMap<>();
        result.put("overview", taskMapper.selectTaskOverview());
        result.put("breakdown", taskMapper.selectStatusBreakdown());
        return result;
    }

    @Override
    public Map<String, Object> getTaskTrend(int days) {
        Map<String, Object> result = new HashMap<>();
        result.put("trend", taskMapper.selectTaskTrend(days));
        return result;
    }

    @Override
    public Map<String, Object> getTaskByScene() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", taskMapper.selectTaskByScene());
        return result;
    }

    @Override
    public Map<String, Object> getDeviceFaultStat() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", faultMapper.selectFaultStat());
        return result;
    }
}
