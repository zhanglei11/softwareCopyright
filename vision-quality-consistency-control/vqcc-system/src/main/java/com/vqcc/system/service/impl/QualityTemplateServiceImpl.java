package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.QualityMetric;
import com.vqcc.system.domain.QualityTemplate;
import com.vqcc.system.dto.request.TemplateCreateReq;
import com.vqcc.system.mapper.QualityMetricMapper;
import com.vqcc.system.mapper.QualityTemplateMapper;
import com.vqcc.system.service.IQualityTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityTemplateServiceImpl implements IQualityTemplateService {

    private final QualityTemplateMapper templateMapper;
    private final QualityMetricMapper metricMapper;

    @Override
    public List<QualityTemplate> list(String templateName, Integer status) {
        return templateMapper.selectList(templateName, status);
    }

    @Override
    public QualityTemplate getById(Long id) {
        QualityTemplate t = templateMapper.selectById(id);
        if (t == null) throw new BusinessException(404, "标准模板不存在");
        List<QualityMetric> metrics = metricMapper.selectByTemplateId(id);
        t.setMetrics(metrics);
        return t;
    }

    @Override
    @Transactional
    public void create(TemplateCreateReq req, Long operatorId) {
        QualityTemplate template = new QualityTemplate();
        template.setTemplateCode(generateCode());
        template.setTemplateName(req.getTemplateName());
        template.setApplicableScene(req.getApplicableScene());
        template.setRemark(req.getRemark());
        template.setStatus(1);
        template.setCreatedBy(operatorId);
        template.setUpdatedBy(operatorId);
        templateMapper.insert(template);
        templateMapper.insertTemplateMetrics(template.getId(), req.getMetricIds());
    }

    @Override
    @Transactional
    public void update(Long id, TemplateCreateReq req, Long operatorId) {
        QualityTemplate template = getById(id);
        template.setTemplateName(req.getTemplateName());
        template.setApplicableScene(req.getApplicableScene());
        template.setRemark(req.getRemark());
        template.setUpdatedBy(operatorId);
        templateMapper.update(template);
        templateMapper.deleteTemplateMetrics(id);
        if (req.getMetricIds() != null && !req.getMetricIds().isEmpty()) {
            templateMapper.insertTemplateMetrics(id, req.getMetricIds());
        }
    }

    @Override
    public void delete(Long id) {
        getById(id);
        templateMapper.deleteTemplateMetrics(id);
        templateMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status, Long operatorId) {
        QualityTemplate template = getById(id);
        template.setStatus(status);
        template.setUpdatedBy(operatorId);
        templateMapper.update(template);
    }

    private String generateCode() {
        String max = templateMapper.selectMaxCode();
        if (max == null) return "QT-001";
        try {
            int num = Integer.parseInt(max.substring(3)) + 1;
            return String.format("QT-%03d", num);
        } catch (Exception e) {
            return "QT-" + System.currentTimeMillis();
        }
    }
}
