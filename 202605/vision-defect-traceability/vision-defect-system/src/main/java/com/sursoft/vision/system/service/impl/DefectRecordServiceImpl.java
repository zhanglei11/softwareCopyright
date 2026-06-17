package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.DefectImage;
import com.sursoft.vision.system.domain.DefectRecord;
import com.sursoft.vision.system.dto.DefectDisposeDTO;
import com.sursoft.vision.system.mapper.DefectImageMapper;
import com.sursoft.vision.system.mapper.DefectRecordMapper;
import com.sursoft.vision.system.query.DefectRecordQuery;
import com.sursoft.vision.system.service.DefectRecordService;
import com.sursoft.vision.system.vo.DefectRecordVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefectRecordServiceImpl implements DefectRecordService {

    private final DefectRecordMapper recordMapper;
    private final DefectImageMapper imageMapper;

    @Override
    public TableDataInfo<DefectRecordVO> list(DefectRecordQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<DefectRecordVO> list = recordMapper.selectList(query);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public DefectRecordVO getById(Long id) {
        DefectRecordVO vo = recordMapper.selectVoById(id);
        if (vo == null) throw new ServiceException("缺陷记录不存在");
        return vo;
    }

    @Override
    public void dispose(Long id, DefectDisposeDTO dto, Long operatorId) {
        DefectRecord record = recordMapper.selectById(id);
        if (record == null) throw new ServiceException("缺陷记录不存在");
        record.setDisposeStatus(dto.getDisposeStatus());
        record.setDisposeRemark(dto.getDisposeRemark());
        record.setDisposeBy(operatorId);
        record.setDisposeAt(LocalDateTime.now());
        recordMapper.updateDisposeStatus(record);
    }

    @Override
    public List<DefectImage> getImages(Long id) {
        return imageMapper.selectByRecordId(id);
    }

    @Override
    public void exportExcel(DefectRecordQuery query, HttpServletResponse response) {
        query.setPageSize(50000);
        query.setPageNum(1);
        List<DefectRecordVO> list = recordMapper.selectList(query);
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=defect_records.xlsx");
            com.alibaba.excel.EasyExcel.write(response.getOutputStream(), DefectRecordVO.class)
                    .sheet("缺陷记录")
                    .doWrite(list);
        } catch (Exception e) {
            throw new ServiceException("导出失败：" + e.getMessage());
        }
    }
}
