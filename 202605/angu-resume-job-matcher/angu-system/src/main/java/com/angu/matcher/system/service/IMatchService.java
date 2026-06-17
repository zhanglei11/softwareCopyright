package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.MatchConfig;
import com.angu.matcher.system.domain.MatchResult;
import com.angu.matcher.system.dto.MatchConfigRequest;

import java.util.List;

public interface IMatchService {
    List<MatchResult> runMatch(Long positionId);
    List<MatchResult> getResults(Long positionId);
    MatchConfig getConfig();
    void updateConfig(MatchConfigRequest req, Long updaterId);
}
