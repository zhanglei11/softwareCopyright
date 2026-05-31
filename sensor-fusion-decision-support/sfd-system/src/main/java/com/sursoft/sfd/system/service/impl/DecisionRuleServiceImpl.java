package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.common.utils.SnowflakeUtils;
import com.sursoft.sfd.system.domain.*;
import com.sursoft.sfd.system.mapper.*;
import com.sursoft.sfd.system.service.IDecisionRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DecisionRuleServiceImpl implements IDecisionRuleService {
    private final DecisionRuleMapper ruleMapper;
    private final DecisionConditionMapper condMapper;

    @Override public List<DecisionRule> list(Long schemeId, Integer status, String keyword) { return ruleMapper.selectList(schemeId, status, keyword); }
    @Override public DecisionRule getById(Long id) {
        DecisionRule r = ruleMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "决策规则不存在");
        return r;
    }
    @Override public void add(DecisionRule rule, Long operatorId) {
        rule.setId(SnowflakeUtils.nextId());
        rule.setCreatedBy(operatorId);
        rule.setStatus(rule.getStatus() != null ? rule.getStatus() : 1);
        ruleMapper.insert(rule);
    }
    @Override public void edit(Long id, DecisionRule rule, Long operatorId) {
        getById(id);
        rule.setId(id);
        rule.setUpdatedBy(operatorId);
        ruleMapper.update(rule);
    }
    @Override public void delete(Long id) { getById(id); ruleMapper.deleteById(id); }
    @Override public void updateStatus(Long id, Integer status, Long operatorId) {
        getById(id);
        ruleMapper.updateStatus(id, status, operatorId);
    }
    @Override public List<DecisionCondition> listConditions(String keyword) { return condMapper.selectList(keyword); }
    @Override public DecisionCondition getConditionById(Long id) {
        DecisionCondition c = condMapper.selectById(id);
        if (c == null) throw new ServiceException(404, "决策条件不存在");
        return c;
    }
    @Override public void addCondition(DecisionCondition c, Long operatorId) {
        c.setId(SnowflakeUtils.nextId());
        condMapper.insert(c);
    }
    @Override public void editCondition(Long id, DecisionCondition c, Long operatorId) {
        getConditionById(id);
        c.setId(id);
        condMapper.update(c);
    }
    @Override public void deleteCondition(Long id) { getConditionById(id); condMapper.deleteById(id); }
}
