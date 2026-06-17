package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.AlertRecord;
import com.sursoft.vision.system.dto.AlertHandleDTO;
import com.sursoft.vision.system.mapper.AlertRecordMapper;
import com.sursoft.vision.system.query.AlertRecordQuery;
import com.sursoft.vision.system.service.AlertRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertRecordServiceImpl implements AlertRecordService {

    private final AlertRecordMapper alertRecordMapper;

    @Override
    public TableDataInfo<AlertRecord> list(AlertRecordQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<AlertRecord> list = alertRecordMapper.selectList(query);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public void handle(Long id, AlertHandleDTO dto, Long operatorId) {
        AlertRecord record = alertRecordMapper.selectById(id);
        if (record == null) throw new ServiceException("告警记录不存在");
        record.setHandleStatus(dto.getHandleStatus());
        record.setHandleRemark(dto.getHandleRemark());
        record.setHandleBy(operatorId);
        record.setHandleAt(LocalDateTime.now());
        alertRecordMapper.updateById(record);
    }
}
