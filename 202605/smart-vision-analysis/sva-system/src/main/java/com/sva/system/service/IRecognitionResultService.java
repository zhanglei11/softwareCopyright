package com.sva.system.service;
import com.sva.system.domain.RecognitionBox;
import com.sva.system.domain.RecognitionResult;
import com.sva.system.query.ResultQuery;
import com.sva.system.vo.ResultDetailVO;
import java.util.List;
public interface IRecognitionResultService {
    List<RecognitionResult> list(ResultQuery query);
    ResultDetailVO getDetail(Long id);
    RecognitionBox addBox(Long id, RecognitionBox box);
    void updateBox(Long id, Long boxId, RecognitionBox box);
    void deleteBox(Long id, Long boxId);
    void confirm(Long id);
    void reject(Long id);
    void batchReview(List<Long> ids, Integer reviewStatus);
}
