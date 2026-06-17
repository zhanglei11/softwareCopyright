package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.KbKnowledgeBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KbKnowledgeBaseMapper {
    List<KbKnowledgeBase> selectAll();
    KbKnowledgeBase selectById(@Param("id") Long id);
    int insert(KbKnowledgeBase kb);
    int updateById(KbKnowledgeBase kb);
    int deleteById(@Param("id") Long id);
    int incrementDocCount(@Param("id") Long id, @Param("count") int count);
    int decrementDocCount(@Param("id") Long id, @Param("count") int count);
}
