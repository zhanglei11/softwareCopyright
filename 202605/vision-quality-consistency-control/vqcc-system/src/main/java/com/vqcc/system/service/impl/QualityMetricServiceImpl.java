package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.QualityMetric;
import com.vqcc.system.dto.request.MetricCreateReq;
import com.vqcc.system.mapper.QualityMetricMapper;
import com.vqcc.system.service.IQualityMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityMetricServiceImpl implements IQualityMetricService {

    private final QualityMetricMapper metricMapper;

    @Override
    public List<QualityMetric> list(String metricName, Integer metricType, Integer status) {
        return metricMapper.selectList(metricName, metricType, status);
    }

    @Override
    public QualityMetric getById(Long id) {
        QualityMetric m = metricMapper.selectById(id);
        if (m == null) throw new BusinessException(404, "质量指标不存在");
        return m;
    }

    @Override
    public void create(MetricCreateReq req, Long operatorId) {
        QualityMetric metric = new QualityMetric();
        metric.setMetricCode(generateCode());
        metric.setMetricName(req.getMetricName());
        metric.setMetricType(req.getMetricType());
        metric.setUnit(req.getUnit());
        metric.setMinValue(req.getMinValue());
        metric.setMaxValue(req.getMaxValue());
        metric.setLevelDesc(req.getLevelDesc());
        metric.setImportance(req.getImportance() != null ? req.getImportance() : 1);
        metric.setRemark(req.getRemark());
        metric.setStatus(1);
        metric.setCreatedBy(operatorId);
        metric.setUpdatedBy(operatorId);
        metricMapper.insert(metric);
    }

    @Override
    public void update(QualityMetric metric, Long operatorId) {
        getById(metric.getId());
        metric.setUpdatedBy(operatorId);
        metricMapper.update(metric);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        metricMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status, Long operatorId) {
        QualityMetric metric = getById(id);
        metric.setStatus(status);
        metric.setUpdatedBy(operatorId);
        metricMapper.update(metric);
    }

    private String generateCode() {
        String max = metricMapper.selectMaxCode();
        if (max == null) return "QM-001";
        try {
            int num = Integer.parseInt(max.substring(3)) + 1;
            return String.format("QM-%03d", num);
        } catch (Exception e) {
            return "QM-" + System.currentTimeMillis();
        }
    }
}
