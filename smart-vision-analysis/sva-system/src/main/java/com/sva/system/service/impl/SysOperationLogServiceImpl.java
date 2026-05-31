package com.sva.system.service.impl;

import com.sva.system.domain.SysOperationLog;
import com.sva.system.mapper.SysOperationLogMapper;
import com.sva.system.query.LogQuery;
import com.sva.system.service.ISysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl implements ISysOperationLogService {

    private final SysOperationLogMapper logMapper;

    @Override
    public List<SysOperationLog> list(LogQuery query) {
        return logMapper.selectList(query);
    }

    @Override
    public void save(SysOperationLog log) {
        logMapper.insert(log);
    }

    @Override
    public void clear() {
        // 实际项目可按日期清理，这里简单实现
    }
}
