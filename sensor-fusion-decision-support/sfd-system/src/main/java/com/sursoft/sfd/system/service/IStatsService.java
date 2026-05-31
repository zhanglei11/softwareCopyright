package com.sursoft.sfd.system.service;
import java.util.Map;
public interface IStatsService {
    Map<String, Object> fusionSummary(Long schemeId, String startTime, String endTime, String granularity);
    Map<String, Object> decisionSummary(String startTime, String endTime);
}
