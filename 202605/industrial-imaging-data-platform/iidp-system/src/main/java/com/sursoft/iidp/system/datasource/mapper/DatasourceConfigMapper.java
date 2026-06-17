package com.sursoft.iidp.system.datasource.mapper;

import com.sursoft.iidp.system.datasource.domain.DatasourceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DatasourceConfigMapper {
    List<DatasourceConfig> selectList(DatasourceConfig query);
    DatasourceConfig selectById(@Param("id") Long id);
    int insert(DatasourceConfig config);
    int update(DatasourceConfig config);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int checkNameUnique(@Param("name") String name, @Param("excludeId") Long excludeId);
    String selectMaxCode();
    long countTotal();
    long countByStatus(@Param("status") Integer status);
}
