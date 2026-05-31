package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.ImageFile;
import com.sva.system.domain.RecognitionBox;
import com.sva.system.domain.RecognitionResult;
import com.sva.system.mapper.ImageFileMapper;
import com.sva.system.mapper.RecognitionResultMapper;
import com.sva.system.query.ResultQuery;
import com.sva.system.service.IRecognitionResultService;
import com.sva.system.vo.ResultDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecognitionResultServiceImpl implements IRecognitionResultService {

    private final RecognitionResultMapper resultMapper;
    private final ImageFileMapper imageFileMapper;

    @Override public List<RecognitionResult> list(ResultQuery query) { return resultMapper.selectList(query); }

    @Override
    public ResultDetailVO getDetail(Long id) {
        RecognitionResult r = resultMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "结果不存在");
        ResultDetailVO vo = new ResultDetailVO();
        vo.setId(r.getId()); vo.setTaskId(r.getTaskId()); vo.setImageId(r.getImageId());
        vo.setReviewStatus(r.getReviewStatus()); vo.setReviewedAt(r.getReviewedAt());
        String[] descs = {"待审核","已确认","需修正","已修正"};
        vo.setReviewStatusDesc(r.getReviewStatus() < descs.length ? descs[r.getReviewStatus()] : "");
        vo.setBoxes(resultMapper.selectBoxesByResultId(id));
        if (r.getImageId() != null) {
            ImageFile img = imageFileMapper.selectById(r.getImageId());
            if (img != null) {
                vo.setImageNo(img.getImageNo());
                vo.setFileName(img.getFileName());
                vo.setImageUrl(img.getFilePath());
            }
        }
        return vo;
    }

    @Override
    public RecognitionBox addBox(Long id, RecognitionBox box) {
        box.setResultId(id); box.setSource(1); box.setIsDeleted(0);
        box.setUpdatedAt(LocalDateTime.now());
        resultMapper.insertBox(box);
        return box;
    }

    @Override public void updateBox(Long id, Long boxId, RecognitionBox box) {
        box.setId(boxId); box.setResultId(id); box.setUpdatedAt(LocalDateTime.now());
        resultMapper.updateBox(box);
    }

    @Override public void deleteBox(Long id, Long boxId) { resultMapper.deleteBoxById(boxId, id); }

    @Override @Transactional
    public void confirm(Long id) {
        RecognitionResult r = resultMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "结果不存在");
        r.setReviewStatus(1); r.setReviewedAt(LocalDateTime.now());
        resultMapper.update(r);
    }

    @Override @Transactional
    public void reject(Long id) {
        RecognitionResult r = resultMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "结果不存在");
        r.setReviewStatus(2); r.setReviewedAt(LocalDateTime.now());
        resultMapper.update(r);
    }

    @Override public void batchReview(List<Long> ids, Integer reviewStatus) {
        resultMapper.batchUpdateReviewStatus(ids, reviewStatus, null);
    }
}
