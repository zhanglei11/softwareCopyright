package com.sursoft.iidp.system.storage.service;

import com.sursoft.iidp.system.storage.domain.StorageCleanLog;
import com.sursoft.iidp.system.storage.domain.StorageCleanRule;
import com.sursoft.iidp.system.storage.domain.StorageOverview;
import java.util.List;

public interface StorageService {
    StorageOverview getOverview();
    List<StorageCleanRule> listRules(StorageCleanRule query);
    StorageCleanRule getRuleById(Long id);
    int addRule(StorageCleanRule rule, Long operatorId);
    int editRule(StorageCleanRule rule, Long operatorId);
    int removeRule(Long id);
    int updateRuleStatus(Long id, Integer status);
    StorageCleanLog executeClean(Long ruleId, Long operatorId);
    List<StorageCleanLog> listCleanLogs(Long ruleId);
}
