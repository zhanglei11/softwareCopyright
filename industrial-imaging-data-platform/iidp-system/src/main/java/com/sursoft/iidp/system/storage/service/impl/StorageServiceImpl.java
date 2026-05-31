package com.sursoft.iidp.system.storage.service.impl;

import com.sursoft.iidp.common.exception.BusinessException;
import com.sursoft.iidp.system.storage.domain.StorageCleanLog;
import com.sursoft.iidp.system.storage.domain.StorageCleanRule;
import com.sursoft.iidp.system.storage.domain.StorageOverview;
import com.sursoft.iidp.system.storage.mapper.StorageCleanLogMapper;
import com.sursoft.iidp.system.storage.mapper.StorageCleanRuleMapper;
import com.sursoft.iidp.system.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageCleanRuleMapper ruleMapper;
    private final StorageCleanLogMapper logMapper;

    @Override
    public StorageOverview getOverview() {
        StorageOverview overview = new StorageOverview();
        File root = new File("/");
        overview.setTotalBytes(root.getTotalSpace());
        overview.setFreeBytes(root.getFreeSpace());
        overview.setUsedBytes(root.getTotalSpace() - root.getFreeSpace());
        overview.setUsageRate(root.getTotalSpace() > 0
                ? Math.round((root.getTotalSpace() - root.getFreeSpace()) * 10000.0 / root.getTotalSpace()) / 100.0
                : 0.0);
        overview.setSnapshotTime(LocalDateTime.now());
        overview.setDirStats(new ArrayList<>());
        return overview;
    }

    @Override public List<StorageCleanRule> listRules(StorageCleanRule query) { return ruleMapper.selectList(query); }

    @Override
    public StorageCleanRule getRuleById(Long id) {
        StorageCleanRule r = ruleMapper.selectById(id);
        if (r == null) throw new BusinessException("清理规则不存在");
        return r;
    }

    @Override
    public int addRule(StorageCleanRule rule, Long operatorId) {
        rule.setCreatedBy(operatorId);
        rule.setStatus(1);
        return ruleMapper.insert(rule);
    }

    @Override
    public int editRule(StorageCleanRule rule, Long operatorId) {
        rule.setUpdatedBy(operatorId);
        return ruleMapper.update(rule);
    }

    @Override public int removeRule(Long id) { return ruleMapper.deleteById(id); }
    @Override public int updateRuleStatus(Long id, Integer status) { return ruleMapper.updateStatus(id, status); }

    @Override
    public StorageCleanLog executeClean(Long ruleId, Long operatorId) {
        StorageCleanRule rule = getRuleById(ruleId);
        StorageCleanLog log = new StorageCleanLog();
        log.setRuleId(ruleId);
        log.setRuleName(rule.getRuleName());
        log.setExecuteTime(LocalDateTime.now());
        log.setExecuteType("MANUAL");
        log.setDeletedCount(0);
        log.setFreedBytes(0L);
        log.setExecuteStatus("SUCCESS");
        log.setExecutedBy(operatorId);
        logMapper.insert(log);
        return log;
    }

    @Override
    public List<StorageCleanLog> listCleanLogs(Long ruleId) {
        return logMapper.selectList(ruleId);
    }
}
