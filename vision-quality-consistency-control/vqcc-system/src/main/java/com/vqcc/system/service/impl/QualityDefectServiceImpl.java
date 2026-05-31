package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.QualityDefect;
import com.vqcc.system.domain.QualityDefectDispose;
import com.vqcc.system.dto.request.DefectDisposeReq;
import com.vqcc.system.dto.request.DefectIgnoreReq;
import com.vqcc.system.dto.request.DefectVerifyReq;
import com.vqcc.system.mapper.QualityDefectDisposeMapper;
import com.vqcc.system.mapper.QualityDefectMapper;
import com.vqcc.system.service.IQualityDefectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QualityDefectServiceImpl implements IQualityDefectService {

    private final QualityDefectMapper defectMapper;
    private final QualityDefectDisposeMapper disposeMapper;

    @Override
    public List<QualityDefect> list(Long taskId, Integer disposeStatus, Integer verifyStatus, String imageId) {
        return defectMapper.selectList(taskId, disposeStatus, verifyStatus, imageId);
    }

    @Override
    public QualityDefect getById(Long id) {
        QualityDefect d = defectMapper.selectById(id);
        if (d == null) throw new BusinessException(404, "不合格品记录不存在");
        return d;
    }

    @Override
    @Transactional
    public void dispose(DefectDisposeReq req, Long operatorId) {
        QualityDefect defect = getById(req.getDefectId());
        if (defect.getDisposeStatus() == 3 || defect.getDisposeStatus() == 4) {
            throw new BusinessException(400, "该不合格品已处置或已忽略");
        }
        QualityDefectDispose dispose = new QualityDefectDispose();
        dispose.setDefectId(req.getDefectId());
        dispose.setDisposePlan(req.getDisposePlan());
        dispose.setOperatorId(operatorId);
        dispose.setOperateAt(LocalDateTime.now());
        dispose.setResultDesc(req.getResultDesc());
        disposeMapper.insert(dispose);

        defect.setDisposeStatus(2);
        defect.setUpdatedBy(operatorId);
        defectMapper.update(defect);
    }

    @Override
    public void ignore(DefectIgnoreReq req, Long operatorId) {
        QualityDefect defect = getById(req.getDefectId());
        if (defect.getDisposeStatus() == 3 || defect.getDisposeStatus() == 4) {
            throw new BusinessException(400, "该不合格品已处置或已忽略");
        }
        defect.setDisposeStatus(4);
        defect.setIgnoreReason(req.getIgnoreReason());
        defect.setUpdatedBy(operatorId);
        defectMapper.update(defect);
    }

    @Override
    @Transactional
    public void verify(DefectVerifyReq req, Long operatorId) {
        QualityDefectDispose dispose = disposeMapper.selectById(req.getDisposeId());
        if (dispose == null) throw new BusinessException(404, "处置记录不存在");
        dispose.setVerifyStatus(req.getVerifyStatus());
        dispose.setVerifyComment(req.getVerifyComment());
        dispose.setVerifyAt(LocalDateTime.now());
        disposeMapper.update(dispose);

        QualityDefect defect = getById(dispose.getDefectId());
        defect.setVerifyStatus(req.getVerifyStatus());
        if (req.getVerifyStatus() == 1) {
            defect.setDisposeStatus(3);
        }
        defect.setUpdatedBy(operatorId);
        defectMapper.update(defect);
    }

    @Override
    public List<QualityDefectDispose> getDisposeHistory(Long defectId) {
        return disposeMapper.selectByDefectId(defectId);
    }

    @Override
    public Map<String, Object> summaryStats() {
        return defectMapper.selectSummaryStats();
    }

    @Override
    public List<Map<String, Object>> trendStats(String startDate, String endDate) {
        return defectMapper.selectTrendStats(startDate, endDate);
    }
}
