package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.AlertRecord;
import com.sursoft.vision.system.dto.AlertHandleDTO;
import com.sursoft.vision.system.query.AlertRecordQuery;

public interface AlertRecordService {
    TableDataInfo<AlertRecord> list(AlertRecordQuery query);
    void handle(Long id, AlertHandleDTO dto, Long operatorId);
}
