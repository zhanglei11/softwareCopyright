package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.DatasourceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DatasourceConfigMapper {
    List<DatasourceConfig> selectList(@Param("sceneType") String sceneType,
                                       @Param("dsType") String dsType,
                                       @Param("status") Integer status,
                                       @Param("keyword") String keyword);
    DatasourceConfig selectById(@Param("id") Long id);
    int insert(DatasourceConfig config);
    int update(DatasourceConfig config);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("updatedBy") Long updatedBy);
    List<DatasourceConfig> selectAllEnabled();
    int deleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);
}
