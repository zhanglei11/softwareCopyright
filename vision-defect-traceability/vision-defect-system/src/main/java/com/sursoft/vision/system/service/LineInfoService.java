package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.LineInfo;
import com.sursoft.vision.system.dto.LineDTO;

public interface LineInfoService {
    TableDataInfo<LineInfo> list(String lineName, Integer status, int pageNum, int pageSize);
    LineInfo getById(Long id);
    void add(LineDTO dto);
    void edit(Long id, LineDTO dto);
    void updateStatus(Long id, Integer status);
    void delete(Long id);
}
