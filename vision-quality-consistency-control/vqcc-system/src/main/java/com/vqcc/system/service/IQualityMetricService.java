package com.vqcc.system.service;

import com.vqcc.system.domain.QualityMetric;
import com.vqcc.system.dto.request.MetricCreateReq;
import java.util.List;

public interface IQualityMetricService {
    List<QualityMetric> list(String metricName, Integer metricType, Integer status);
    QualityMetric getById(Long id);
    void create(MetricCreateReq req, Long operatorId);
    void update(QualityMetric metric, Long operatorId);
    void delete(Long id);
    void updateStatus(Long id, Integer status, Long operatorId);
}
