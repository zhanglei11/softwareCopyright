package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.AlertRule;
import com.sursoft.vision.system.dto.AlertRuleDTO;

public interface AlertRuleService {
    TableDataInfo<AlertRule> list(Integer status, int pageNum, int pageSize);
    void add(AlertRuleDTO dto, Long createdBy);
    void edit(Long id, AlertRuleDTO dto);
    void updateStatus(Long id, Integer status);
    void delete(Long id);
}
