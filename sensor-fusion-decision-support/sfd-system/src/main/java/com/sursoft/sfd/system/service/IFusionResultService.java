package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.FusionResult;
import java.util.List;
public interface IFusionResultService {
    List<FusionResult> list(Long schemeId, Integer resultStatus, String startTime, String endTime);
    FusionResult getById(Long id);
}
