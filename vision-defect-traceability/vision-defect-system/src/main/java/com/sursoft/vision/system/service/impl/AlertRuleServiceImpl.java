package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.AlertRule;
import com.sursoft.vision.system.dto.AlertRuleDTO;
import com.sursoft.vision.system.mapper.AlertRuleMapper;
import com.sursoft.vision.system.service.AlertRuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertRuleServiceImpl implements AlertRuleService {

    private final AlertRuleMapper ruleMapper;

    @Override
    public TableDataInfo<AlertRule> list(Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AlertRule> list = ruleMapper.selectList(status);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public void add(AlertRuleDTO dto, Long createdBy) {
        AlertRule rule = toEntity(dto);
        rule.setCreatedBy(createdBy);
        rule.setIsDeleted(0);
        ruleMapper.insert(rule);
    }

    @Override
    public void edit(Long id, AlertRuleDTO dto) {
        AlertRule rule = ruleMapper.selectById(id);
        if (rule == null) throw new ServiceException("告警规则不存在");
        AlertRule update = toEntity(dto);
        update.setId(id);
        ruleMapper.updateById(update);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        AlertRule rule = new AlertRule();
        rule.setId(id);
        rule.setStatus(status);
        ruleMapper.updateById(rule);
    }

    @Override
    public void delete(Long id) {
        AlertRule rule = new AlertRule();
        rule.setId(id);
        rule.setIsDeleted(1);
        ruleMapper.updateById(rule);
    }

    private AlertRule toEntity(AlertRuleDTO dto) {
        AlertRule rule = new AlertRule();
        rule.setRuleName(dto.getRuleName());
        rule.setLineId(dto.getLineId());
        rule.setConditionType(dto.getConditionType());
        rule.setThreshold(dto.getThreshold());
        rule.setStatCycle(dto.getStatCycle());
        rule.setAlertLevel(dto.getAlertLevel());
        rule.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        if (dto.getNotifyUserIds() != null) {
            rule.setNotifyUserIds(dto.getNotifyUserIds().toString());
        }
        return rule;
    }
}
