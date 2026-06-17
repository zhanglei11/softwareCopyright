package com.imaging.scheduler.system.mapper.dispatch;

import com.imaging.scheduler.system.domain.dispatch.DispatchLog;
import com.imaging.scheduler.system.dto.req.DispatchLogQueryReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DispatchLogMapper {
    List<DispatchLog> selectList(DispatchLogQueryReq req);
    List<DispatchLog> selectAllForExport(DispatchLogQueryReq req);
    int insert(DispatchLog log);
}
