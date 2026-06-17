package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.common.utils.SnowflakeUtils;
import com.sursoft.sfd.system.domain.*;
import com.sursoft.sfd.system.mapper.*;
import com.sursoft.sfd.system.service.IFusionSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FusionSchemeServiceImpl implements IFusionSchemeService {
    private final FusionSchemeMapper schemeMapper;
    private final FusionRuleMapper ruleMapper;
    private final FusionWeightMapper weightMapper;

    @Override public List<FusionScheme> list(String keyword, Integer status) { return schemeMapper.selectList(keyword, status); }
    @Override public FusionScheme getById(Long id) {
        FusionScheme s = schemeMapper.selectById(id);
        if (s == null) throw new ServiceException(404, "融合方案不存在");
        return s;
    }
    @Override public void add(FusionScheme scheme, Long operatorId) {
        scheme.setId(SnowflakeUtils.nextId());
        scheme.setCreatedBy(operatorId);
        scheme.setStatus(scheme.getStatus() != null ? scheme.getStatus() : 1);
        schemeMapper.insert(scheme);
    }
    @Override public void edit(Long id, FusionScheme scheme, Long operatorId) {
        getById(id);
        scheme.setId(id);
        scheme.setUpdatedBy(operatorId);
        schemeMapper.update(scheme);
    }
    @Override @Transactional public void delete(Long id) {
        getById(id);
        ruleMapper.selectBySchemeId(id).forEach(r -> {
            weightMapper.deleteBySchemeId(id);
            ruleMapper.deleteById(r.getId());
        });
        schemeMapper.deleteById(id);
    }
    @Override public void updateStatus(Long id, Integer status, Long operatorId) {
        getById(id);
        schemeMapper.updateStatus(id, status, operatorId);
    }
    @Override public List<FusionRule> getRules(Long schemeId) { return ruleMapper.selectBySchemeId(schemeId); }
    @Override public void addRule(FusionRule rule, Long operatorId) {
        rule.setId(SnowflakeUtils.nextId());
        rule.setCreatedBy(operatorId);
        ruleMapper.insert(rule);
    }
    @Override public void editRule(Long ruleId, FusionRule rule, Long operatorId) {
        if (ruleMapper.selectById(ruleId) == null) throw new ServiceException(404, "融合规则不存在");
        rule.setId(ruleId);
        rule.setUpdatedBy(operatorId);
        ruleMapper.update(rule);
    }
    @Override public void deleteRule(Long ruleId) { ruleMapper.deleteById(ruleId); }
    @Override public List<FusionWeight> getWeights(Long schemeId) { return weightMapper.selectBySchemeId(schemeId); }
    @Override @Transactional public void saveWeights(Long schemeId, List<FusionWeight> weights, Long operatorId) {
        weightMapper.deleteBySchemeId(schemeId);
        for (FusionWeight w : weights) {
            w.setId(SnowflakeUtils.nextId());
            w.setSchemeId(schemeId);
            w.setUpdatedBy(operatorId);
            weightMapper.insert(w);
        }
    }
}
