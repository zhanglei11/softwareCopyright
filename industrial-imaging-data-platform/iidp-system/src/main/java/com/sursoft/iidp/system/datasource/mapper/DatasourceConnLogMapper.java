package com.sursoft.iidp.system.datasource.mapper;

import com.sursoft.iidp.system.datasource.domain.DatasourceConnLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DatasourceConnLogMapper {
    int insert(DatasourceConnLog log);
    List<DatasourceConnLog> selectByDatasourceId(@Param("datasourceId") Long datasourceId);
    DatasourceConnLog selectLatestByDatasourceId(@Param("datasourceId") Long datasourceId);
}
