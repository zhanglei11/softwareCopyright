package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.KbDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KbDocumentMapper {
    List<KbDocument> selectByKbId(@Param("kbId") Long kbId);
    KbDocument selectById(@Param("id") Long id);
    int insert(KbDocument doc);
    int updateById(KbDocument doc);
    int deleteById(@Param("id") Long id);
    int countByKbId(@Param("kbId") Long kbId);
    int updateParseStatus(@Param("id") Long id, @Param("status") String status, @Param("errorMsg") String errorMsg);
}
