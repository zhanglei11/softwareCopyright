package com.vqcc.system.service;

import com.vqcc.system.domain.QualityTemplate;
import com.vqcc.system.dto.request.TemplateCreateReq;
import java.util.List;

public interface IQualityTemplateService {
    List<QualityTemplate> list(String templateName, Integer status);
    QualityTemplate getById(Long id);
    void create(TemplateCreateReq req, Long operatorId);
    void update(Long id, TemplateCreateReq req, Long operatorId);
    void delete(Long id);
    void updateStatus(Long id, Integer status, Long operatorId);
}
