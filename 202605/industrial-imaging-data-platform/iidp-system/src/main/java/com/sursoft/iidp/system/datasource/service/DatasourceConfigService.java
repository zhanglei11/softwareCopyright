package com.sursoft.iidp.system.datasource.service;

import com.sursoft.iidp.system.datasource.domain.DatasourceConfig;
import com.sursoft.iidp.system.datasource.domain.DatasourceConnLog;
import java.util.List;

public interface DatasourceConfigService {
    List<DatasourceConfig> listDatasources(DatasourceConfig query);
    DatasourceConfig getById(Long id);
    int addDatasource(DatasourceConfig config, Long operatorId);
    int editDatasource(DatasourceConfig config, Long operatorId);
    int removeDatasource(Long id);
    int updateStatus(Long id, Integer status);
    DatasourceConnLog testConnection(Long id, Long operatorId);
}
