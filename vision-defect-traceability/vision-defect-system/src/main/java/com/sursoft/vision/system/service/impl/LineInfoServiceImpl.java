package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.LineInfo;
import com.sursoft.vision.system.dto.LineDTO;
import com.sursoft.vision.system.mapper.LineInfoMapper;
import com.sursoft.vision.system.service.LineInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineInfoServiceImpl implements LineInfoService {

    private final LineInfoMapper lineMapper;

    @Override
    public TableDataInfo<LineInfo> list(String lineName, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<LineInfo> list = lineMapper.selectList(lineName, status);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public LineInfo getById(Long id) {
        return lineMapper.selectById(id);
    }

    @Override
    public void add(LineDTO dto) {
        if (lineMapper.selectByLineNo(dto.getLineNo()) != null) {
            throw new ServiceException("产线编号已存在");
        }
        LineInfo line = new LineInfo();
        line.setLineNo(dto.getLineNo());
        line.setLineName(dto.getLineName());
        line.setWorkshop(dto.getWorkshop());
        line.setManagerId(dto.getManagerId());
        line.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        line.setRemark(dto.getRemark());
        line.setIsDeleted(0);
        lineMapper.insert(line);
    }

    @Override
    public void edit(Long id, LineDTO dto) {
        LineInfo line = getById(id);
        if (line == null) throw new ServiceException("产线不存在");
        line.setLineName(dto.getLineName());
        line.setWorkshop(dto.getWorkshop());
        line.setManagerId(dto.getManagerId());
        line.setStatus(dto.getStatus());
        line.setRemark(dto.getRemark());
        lineMapper.updateById(line);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        LineInfo line = new LineInfo();
        line.setId(id);
        line.setStatus(status);
        lineMapper.updateById(line);
    }

    @Override
    public void delete(Long id) {
        LineInfo line = new LineInfo();
        line.setId(id);
        line.setIsDeleted(1);
        lineMapper.updateById(line);
    }
}
