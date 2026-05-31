package com.vqcc.system.service;

import com.vqcc.system.domain.QualityDefect;
import com.vqcc.system.domain.QualityDefectDispose;
import com.vqcc.system.dto.request.DefectDisposeReq;
import com.vqcc.system.dto.request.DefectIgnoreReq;
import com.vqcc.system.dto.request.DefectVerifyReq;
import java.util.List;
import java.util.Map;

public interface IQualityDefectService {
    List<QualityDefect> list(Long taskId, Integer disposeStatus, Integer verifyStatus, String imageId);
    QualityDefect getById(Long id);
    void dispose(DefectDisposeReq req, Long operatorId);
    void ignore(DefectIgnoreReq req, Long operatorId);
    void verify(DefectVerifyReq req, Long operatorId);
    List<QualityDefectDispose> getDisposeHistory(Long defectId);
    Map<String, Object> summaryStats();
    List<Map<String, Object>> trendStats(String startDate, String endDate);
}
