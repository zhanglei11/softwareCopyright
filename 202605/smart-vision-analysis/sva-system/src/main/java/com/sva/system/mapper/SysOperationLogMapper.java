package com.sva.system.mapper;

import com.sva.system.domain.SysOperationLog;
import com.sva.system.query.LogQuery;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysOperationLogMapper {
    List<SysOperationLog> selectList(LogQuery query);
    int insert(SysOperationLog log);
}
