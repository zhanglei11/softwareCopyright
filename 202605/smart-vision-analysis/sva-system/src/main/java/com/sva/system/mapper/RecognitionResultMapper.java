package com.sva.system.mapper;

import com.sva.system.domain.RecognitionBox;
import com.sva.system.domain.RecognitionResult;
import com.sva.system.query.ResultQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RecognitionResultMapper {
    List<RecognitionResult> selectList(ResultQuery query);
    RecognitionResult selectById(@Param("id") Long id);
    int insert(RecognitionResult result);
    int update(RecognitionResult result);
    List<RecognitionBox> selectBoxesByResultId(@Param("resultId") Long resultId);
    int insertBox(RecognitionBox box);
    int updateBox(RecognitionBox box);
    int deleteBoxById(@Param("id") Long id, @Param("resultId") Long resultId);
    int batchUpdateReviewStatus(@Param("ids") List<Long> ids, @Param("reviewStatus") Integer reviewStatus, @Param("reviewedBy") Long reviewedBy);
}
