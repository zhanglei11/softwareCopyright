package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.ResumeMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResumeMainMapper {
    List<ResumeMain> selectList(@Param("name") String name,
                                 @Param("phone") String phone,
                                 @Param("highestEdu") String highestEdu,
                                 @Param("source") String source,
                                 @Param("skill") String skill);
    ResumeMain selectById(Long id);
    List<ResumeMain> selectAllValid();
    int insert(ResumeMain resume);
    int update(ResumeMain resume);
    int deleteById(Long id);
    int updateFilePath(@Param("id") Long id,
                        @Param("filePath") String filePath,
                        @Param("parseSuccess") Integer parseSuccess);
}
