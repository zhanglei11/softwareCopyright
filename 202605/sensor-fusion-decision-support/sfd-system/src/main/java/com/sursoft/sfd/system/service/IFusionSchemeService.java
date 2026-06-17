package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.FusionScheme;
import com.sursoft.sfd.system.domain.FusionRule;
import com.sursoft.sfd.system.domain.FusionWeight;
import java.util.List;
public interface IFusionSchemeService {
    List<FusionScheme> list(String keyword, Integer status);
    FusionScheme getById(Long id);
    void add(FusionScheme scheme, Long operatorId);
    void edit(Long id, FusionScheme scheme, Long operatorId);
    void delete(Long id);
    void updateStatus(Long id, Integer status, Long operatorId);
    List<FusionRule> getRules(Long schemeId);
    void addRule(FusionRule rule, Long operatorId);
    void editRule(Long ruleId, FusionRule rule, Long operatorId);
    void deleteRule(Long ruleId);
    List<FusionWeight> getWeights(Long schemeId);
    void saveWeights(Long schemeId, List<FusionWeight> weights, Long operatorId);
}
