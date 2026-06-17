package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.DecisionResult;
import java.util.List;
import java.util.Map;
public interface IDecisionResultService {
    List<DecisionResult> list(Long ruleId, Long schemeId, String startTime, String endTime, String keyword);
    DecisionResult getById(Long id);
    DecisionResult getWithTrace(Long id);
    Map<String, Object> triggerFrequency(Long schemeId, String startTime, String endTime);
}
