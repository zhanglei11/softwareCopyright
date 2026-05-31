package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.LineInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LineInfoMapper {
    LineInfo selectById(Long id);
    LineInfo selectByLineNo(String lineNo);
    List<LineInfo> selectList(@Param("lineName") String lineName, @Param("status") Integer status);
    int insert(LineInfo line);
    int updateById(LineInfo line);
    int deleteById(Long id);
}
