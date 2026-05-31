package com.sursoft.iidp.system.datasource.service.impl;

import com.sursoft.iidp.common.constant.HttpStatus;
import com.sursoft.iidp.common.exception.BusinessException;
import com.sursoft.iidp.common.utils.AesUtils;
import com.sursoft.iidp.system.datasource.domain.DatasourceConfig;
import com.sursoft.iidp.system.datasource.domain.DatasourceConnLog;
import com.sursoft.iidp.system.datasource.mapper.DatasourceConfigMapper;
import com.sursoft.iidp.system.datasource.mapper.DatasourceConnLogMapper;
import com.sursoft.iidp.system.datasource.service.DatasourceConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasourceConfigServiceImpl implements DatasourceConfigService {

    private final DatasourceConfigMapper configMapper;
    private final DatasourceConnLogMapper connLogMapper;

    @Value("${iidp.aes.key:IidpAesKey2026!!}")
    private String aesKey;

    @Value("${iidp.aes.iv:IidpAesIV2026!!}")
    private String aesIv;

    @Override
    public List<DatasourceConfig> listDatasources(DatasourceConfig query) {
        List<DatasourceConfig> list = configMapper.selectList(query);
        list.forEach(this::maskSensitive);
        return list;
    }

    @Override
    public DatasourceConfig getById(Long id) {
        DatasourceConfig config = configMapper.selectById(id);
        if (config == null) throw new BusinessException("数据源不存在");
        maskSensitive(config);
        return config;
    }

    @Override
    public int addDatasource(DatasourceConfig config, Long operatorId) {
        if (configMapper.checkNameUnique(config.getDatasourceName(), null) > 0) {
            throw new BusinessException(HttpStatus.CONFLICT, "数据源名称已存在");
        }
        encryptSensitive(config);
        config.setDatasourceCode(generateCode());
        config.setCreatedBy(operatorId);
        config.setStatus(1);
        return configMapper.insert(config);
    }

    @Override
    public int editDatasource(DatasourceConfig config, Long operatorId) {
        if (configMapper.checkNameUnique(config.getDatasourceName(), config.getId()) > 0) {
            throw new BusinessException(HttpStatus.CONFLICT, "数据源名称已存在");
        }
        encryptSensitive(config);
        config.setUpdatedBy(operatorId);
        return configMapper.update(config);
    }

    @Override
    public int removeDatasource(Long id) {
        return configMapper.deleteById(id);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        return configMapper.updateStatus(id, status);
    }

    @Override
    public DatasourceConnLog testConnection(Long id, Long operatorId) {
        DatasourceConfig config = configMapper.selectById(id);
        if (config == null) throw new BusinessException("数据源不存在");
        long start = System.currentTimeMillis();
        DatasourceConnLog log = new DatasourceConnLog();
        log.setDatasourceId(id);
        log.setTestedAt(LocalDateTime.now());
        log.setTestedBy(operatorId);
        // 模拟连通性测试（实际项目中根据datasourceType做真实连接测试）
        try {
            Thread.sleep(50);
            log.setResult(1);
            log.setCostTime((int)(System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.setResult(0);
            log.setErrorMsg(e.getMessage());
            log.setCostTime((int)(System.currentTimeMillis() - start));
        }
        connLogMapper.insert(log);
        return log;
    }

    private void encryptSensitive(DatasourceConfig config) {
        try {
            if (config.getAuthPassword() != null && !config.getAuthPassword().equals("****")) {
                config.setAuthPassword(AesUtils.encrypt(config.getAuthPassword(), aesKey, aesIv));
            }
            if (config.getAuthKey() != null && !config.getAuthKey().equals("****")) {
                config.setAuthKey(AesUtils.encrypt(config.getAuthKey(), aesKey, aesIv));
            }
        } catch (Exception e) {
            throw new BusinessException("敏感信息加密失败");
        }
    }

    private void maskSensitive(DatasourceConfig config) {
        config.setAuthPassword(AesUtils.mask(config.getAuthPassword()));
        config.setAuthKey(AesUtils.mask(config.getAuthKey()));
    }

    private String generateCode() {
        String max = configMapper.selectMaxCode();
        if (max == null) return "DS-0001";
        try {
            int num = Integer.parseInt(max.substring(3)) + 1;
            return String.format("DS-%04d", num);
        } catch (Exception e) {
            return "DS-" + System.currentTimeMillis();
        }
    }
}
