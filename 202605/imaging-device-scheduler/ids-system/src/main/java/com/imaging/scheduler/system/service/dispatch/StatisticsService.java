package com.imaging.scheduler.system.service.dispatch;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getDeviceStatusStat();
    Map<String, Object> getDeviceTrend(int days);
    Map<String, Object> getTaskStatusStat();
    Map<String, Object> getTaskTrend(int days);
    Map<String, Object> getTaskByScene();
    Map<String, Object> getDeviceFaultStat();
}
