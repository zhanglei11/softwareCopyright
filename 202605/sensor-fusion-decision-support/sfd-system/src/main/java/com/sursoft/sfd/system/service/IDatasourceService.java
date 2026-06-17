package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.DatasourceConfig;
import java.util.List;
public interface IDatasourceService {
    List<DatasourceConfig> list(String sceneType, String dsType, Integer status, String keyword);
    DatasourceConfig getById(Long id);
    void add(DatasourceConfig config, Long operatorId);
    void edit(Long id, DatasourceConfig config, Long operatorId);
    void updateStatus(Long id, Integer status, Long operatorId);
    boolean testConn(Long id);
    List<DatasourceConfig> statusOverview();
    void delete(Long id, Long operatorId);
}
