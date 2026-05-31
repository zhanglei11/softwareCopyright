package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.enums.EduLevel;
import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.*;
import com.angu.matcher.system.dto.MatchConfigRequest;
import com.angu.matcher.system.mapper.*;
import com.angu.matcher.system.service.IMatchService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements IMatchService {

    private final MatchResultMapper matchResultMapper;
    private final MatchConfigMapper matchConfigMapper;
    private final JobPositionMapper positionMapper;
    private final ResumeMainMapper resumeMapper;
    private final ResumeSkillMapper skillMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public List<MatchResult> runMatch(Long positionId) {
        JobPosition pos = positionMapper.selectById(positionId);
        if (pos == null || !"OPEN".equals(pos.getStatus())) {
            throw new ServiceException(400, "职位不存在或未发布");
        }
        MatchConfig config = getConfig();
        List<ResumeMain> resumes = resumeMapper.selectAllValid();
        List<String> posSkills = parseSkillTags(pos.getSkillTags());

        List<MatchResult> results = resumes.stream().map(resume -> {
            List<ResumeSkill> resumeSkills = skillMapper.selectByResumeId(resume.getId());
            BigDecimal skillScore = calcSkillScore(posSkills, resumeSkills);
            BigDecimal eduScore = calcEduScore(pos.getEduRequire(), resume.getHighestEdu());
            BigDecimal expScore = calcExpScore(pos.getExpRequire(), resume.getTotalExpYears());
            BigDecimal total = skillScore.multiply(bd(config.getSkillWeight()))
                    .add(eduScore.multiply(bd(config.getEduWeight())))
                    .add(expScore.multiply(bd(config.getExpWeight())))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            MatchResult mr = new MatchResult();
            mr.setPositionId(positionId);
            mr.setResumeId(resume.getId());
            mr.setTotalScore(total);
            mr.setSkillScore(skillScore);
            mr.setEduScore(eduScore);
            mr.setExpScore(expScore);
            return mr;
        }).sorted(Comparator.comparing(MatchResult::getTotalScore).reversed()).toList();

        matchResultMapper.deleteByPositionId(positionId);
        if (!results.isEmpty()) matchResultMapper.insertBatch(results);
        return matchResultMapper.selectByPositionId(positionId);
    }

    @Override
    public List<MatchResult> getResults(Long positionId) {
        return matchResultMapper.selectByPositionId(positionId);
    }

    @Override
    public MatchConfig getConfig() {
        MatchConfig config = matchConfigMapper.selectOne();
        if (config == null) throw new ServiceException(500, "匹配配置未初始化");
        return config;
    }

    @Override
    public void updateConfig(MatchConfigRequest req, Long updaterId) {
        if (req.getSkillWeight() + req.getEduWeight() + req.getExpWeight() != 100) {
            throw new ServiceException(422, "各维度权重总和必须等于 100%");
        }
        MatchConfig config = getConfig();
        config.setSkillWeight(req.getSkillWeight());
        config.setEduWeight(req.getEduWeight());
        config.setExpWeight(req.getExpWeight());
        config.setUpdaterId(updaterId);
        matchConfigMapper.updateConfig(config);
    }

    private BigDecimal calcSkillScore(List<String> posSkills, List<ResumeSkill> resumeSkills) {
        if (posSkills == null || posSkills.isEmpty()) return BigDecimal.valueOf(100);
        Set<String> resumeSet = new HashSet<>();
        resumeSkills.forEach(s -> resumeSet.add(s.getSkillName().toLowerCase()));
        long matched = posSkills.stream().filter(s -> resumeSet.contains(s.toLowerCase())).count();
        return BigDecimal.valueOf(matched * 100.0 / posSkills.size()).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcEduScore(String required, String actual) {
        if (required == null || actual == null) return BigDecimal.valueOf(100);
        int req = getEduLevel(required), act = getEduLevel(actual);
        if (act >= req) return BigDecimal.valueOf(100);
        if (act == req - 1) return BigDecimal.valueOf(50);
        return BigDecimal.ZERO;
    }

    private BigDecimal calcExpScore(Integer required, Integer actual) {
        if (required == null || required == 0) return BigDecimal.valueOf(100);
        if (actual == null || actual <= 0) return BigDecimal.ZERO;
        if (actual >= required) return BigDecimal.valueOf(100);
        return BigDecimal.valueOf(actual * 100.0 / required).setScale(2, RoundingMode.HALF_UP);
    }

    private int getEduLevel(String code) {
        for (EduLevel e : EduLevel.values()) {
            if (e.name().equals(code)) return e.getLevel();
        }
        return 0;
    }

    private List<String> parseSkillTags(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private BigDecimal bd(int v) { return BigDecimal.valueOf(v); }
}
