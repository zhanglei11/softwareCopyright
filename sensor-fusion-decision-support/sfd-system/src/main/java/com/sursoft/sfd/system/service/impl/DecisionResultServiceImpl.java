package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.system.domain.DecisionResult;
import com.sursoft.sfd.system.mapper.DecisionResultMapper;
import com.sursoft.sfd.system.service.IDecisionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DecisionResultServiceImpl implements IDecisionResultService {
    private final DecisionResultMapper resultMapper;

    @Override public List<DecisionResult> list(Long ruleId, Long schemeId, String startTime, String endTime, String keyword) {
        return resultMapper.selectList(ruleId, schemeId, startTime, endTime, keyword);
    }
    @Override public DecisionResult getById(Long id) {
        DecisionResult r = resultMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "决策结果不存在");
        return r;
    }
    @Override public DecisionResult getWithTrace(Long id) {
        DecisionResult r = resultMapper.selectWithFusionResult(id);
        if (r == null) throw new ServiceException(404, "决策结果不存在");
        return r;
    }
    @Override public Map<String, Object> triggerFrequency(Long schemeId, String startTime, String endTime) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("byRule", resultMapper.countByRule(startTime, endTime));
        return result;
    }
}
