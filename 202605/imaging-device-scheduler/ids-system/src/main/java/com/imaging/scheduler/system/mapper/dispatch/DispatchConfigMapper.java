package com.imaging.scheduler.system.mapper.dispatch;

import com.imaging.scheduler.system.domain.dispatch.DispatchConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DispatchConfigMapper {
    DispatchConfig selectConfig();
    DispatchConfig selectFirst();
    int update(DispatchConfig config);
}
