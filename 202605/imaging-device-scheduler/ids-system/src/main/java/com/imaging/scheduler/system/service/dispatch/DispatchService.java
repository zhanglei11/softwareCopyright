package com.imaging.scheduler.system.service.dispatch;

import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.dispatch.DispatchConfig;
import com.imaging.scheduler.system.domain.dispatch.DispatchLog;
import com.imaging.scheduler.system.dto.req.DispatchConfigReq;
import com.imaging.scheduler.system.dto.req.DispatchLogQueryReq;

import java.util.List;
import java.util.Map;

public interface DispatchService {
    Map<String, Object> getOverview();
    List<Map<String, Object>> getGanttData(Long sceneId, String date);
    List<Map<String, Object>> getTimeoutAlerts();
    DispatchConfig getConfig();
    void updateConfig(DispatchConfigReq req);
    TableDataInfo<DispatchLog> getLogList(DispatchLogQueryReq req);
    List<DispatchLog> exportLogs(DispatchLogQueryReq req);
}
