package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.FusionScheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FusionSchemeMapper {
    List<FusionScheme> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    FusionScheme selectById(@Param("id") Long id);
    int insert(FusionScheme scheme);
    int update(FusionScheme scheme);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("updatedBy") Long updatedBy);
    List<FusionScheme> selectAllEnabled();
}
