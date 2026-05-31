package com.sva.system.service;

import com.sva.system.domain.SysOperationLog;
import com.sva.system.query.LogQuery;
import java.util.List;

public interface ISysOperationLogService {
    List<SysOperationLog> list(LogQuery query);
    void save(SysOperationLog log);
    void clear();
}
