package com.sursoft.iidp.system.sys.mapper;

import com.sursoft.iidp.system.sys.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysOperLogMapper {
    int insert(SysOperLog operLog);
    List<SysOperLog> selectList(SysOperLog query);
}
