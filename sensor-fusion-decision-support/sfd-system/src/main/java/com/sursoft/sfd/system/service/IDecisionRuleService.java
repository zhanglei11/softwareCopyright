package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.DecisionRule;
import com.sursoft.sfd.system.domain.DecisionCondition;
import java.util.List;
public interface IDecisionRuleService {
    List<DecisionRule> list(Long schemeId, Integer status, String keyword);
    DecisionRule getById(Long id);
    void add(DecisionRule rule, Long operatorId);
    void edit(Long id, DecisionRule rule, Long operatorId);
    void delete(Long id);
    void updateStatus(Long id, Integer status, Long operatorId);
    List<DecisionCondition> listConditions(String keyword);
    DecisionCondition getConditionById(Long id);
    void addCondition(DecisionCondition condition, Long operatorId);
    void editCondition(Long id, DecisionCondition condition, Long operatorId);
    void deleteCondition(Long id);
}
