package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.system.domain.FusionResult;
import com.sursoft.sfd.system.mapper.FusionResultMapper;
import com.sursoft.sfd.system.service.IFusionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FusionResultServiceImpl implements IFusionResultService {
    private final FusionResultMapper resultMapper;

    @Override public List<FusionResult> list(Long schemeId, Integer resultStatus, String startTime, String endTime) {
        return resultMapper.selectList(schemeId, resultStatus, startTime, endTime);
    }
    @Override public FusionResult getById(Long id) {
        FusionResult r = resultMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "融合结果不存在");
        return r;
    }
}
