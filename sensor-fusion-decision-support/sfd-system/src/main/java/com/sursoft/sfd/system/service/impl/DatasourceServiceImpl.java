package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.common.utils.SnowflakeUtils;
import com.sursoft.sfd.system.domain.DatasourceConfig;
import com.sursoft.sfd.system.mapper.DatasourceConfigMapper;
import com.sursoft.sfd.system.service.IDatasourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasourceServiceImpl implements IDatasourceService {
    private final DatasourceConfigMapper dsMapper;

    @Override
    public List<DatasourceConfig> list(String sceneType, String dsType, Integer status, String keyword) {
        return dsMapper.selectList(sceneType, dsType, status, keyword);
    }

    @Override
    public DatasourceConfig getById(Long id) {
        DatasourceConfig c = dsMapper.selectById(id);
        if (c == null) throw new ServiceException(404, "数据源不存在");
        return c;
    }

    @Override
    public void add(DatasourceConfig config, Long operatorId) {
        config.setId(SnowflakeUtils.nextId());
        config.setCreatedBy(operatorId);
        config.setStatus(config.getStatus() != null ? config.getStatus() : 1);
        dsMapper.insert(config);
    }

    @Override
    public void edit(Long id, DatasourceConfig config, Long operatorId) {
        getById(id);
        config.setId(id);
        config.setUpdatedBy(operatorId);
        dsMapper.update(config);
    }

    @Override
    public void updateStatus(Long id, Integer status, Long operatorId) {
        getById(id);
        dsMapper.updateStatus(id, status, operatorId);
    }

    @Override
    public boolean testConn(Long id) {
        DatasourceConfig config = getById(id);
        // 实际连通性测试可根据 dsType 扩展；此处返回 true 作为占位
        return config != null;
    }

    @Override
    public List<DatasourceConfig> statusOverview() {
        return dsMapper.selectAllEnabled();
    }

    @Override
    public void delete(Long id, Long operatorId) {
        getById(id);
        dsMapper.deleteById(id, operatorId);
    }
}
