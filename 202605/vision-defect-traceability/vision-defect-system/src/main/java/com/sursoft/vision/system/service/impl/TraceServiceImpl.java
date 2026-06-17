package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.mapper.DefectRecordMapper;
import com.sursoft.vision.system.service.TraceService;
import com.sursoft.vision.system.vo.BatchTraceVO;
import com.sursoft.vision.system.vo.DefectRecordVO;
import com.sursoft.vision.system.vo.ProductTraceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraceServiceImpl implements TraceService {

    private final DefectRecordMapper recordMapper;

    @Override
    public BatchTraceVO traceBatch(String batchNo, Long lineId) {
        Long total = recordMapper.countBatchTotal(batchNo, lineId);
        if (total == null || total == 0) throw new ServiceException("未找到该批次数据");
        Long qualified = recordMapper.countBatchQualified(batchNo, lineId);
        if (qualified == null) qualified = 0L;
        List<BatchTraceVO.CategoryDistVO> dist = recordMapper.selectBatchCategoryDist(batchNo, lineId);
        List<DefectRecordVO> records = recordMapper.selectByBatchNo(batchNo, lineId);

        BatchTraceVO vo = new BatchTraceVO();
        vo.setBatchNo(batchNo);
        vo.setLineName(records.isEmpty() ? "" : records.get(0).getLineName());
        vo.setTotalCount(total);
        vo.setQualifiedCount(qualified);
        vo.setDefectCount(total - qualified);
        vo.setQualifiedRate(total > 0 ? (double) qualified / total : 0);
        vo.setCategoryDistribution(dist);
        vo.setRecords(records);
        return vo;
    }

    @Override
    public ProductTraceVO traceProduct(String serialNo) {
        List<DefectRecordVO> records = recordMapper.selectBySerialNo(serialNo);
        if (records.isEmpty()) throw new ServiceException("未找到该产品的检测记录");

        ProductTraceVO vo = new ProductTraceVO();
        DefectRecordVO first = records.get(0);
        vo.setSerialNo(serialNo);
        vo.setTypeName(first.getProductTypeName());
        vo.setBatchNo(first.getBatchNo());
        vo.setLineName(first.getLineName());

        List<ProductTraceVO.DetectRecordItem> items = records.stream().map(r -> {
            ProductTraceVO.DetectRecordItem item = new ProductTraceVO.DetectRecordItem();
            item.setId(r.getId());
            item.setDetectTime(r.getDetectTime());
            item.setResult(r.getResult());
            item.setResultLabel(r.getResultLabel());
            item.setCategoryName(r.getCategoryName());
            item.setLevel(r.getLevel());
            item.setDisposeStatus(r.getDisposeStatus());
            item.setDisposeRemark(r.getDisposeStatusLabel());
            return item;
        }).collect(Collectors.toList());
        vo.setDetectRecords(items);

        boolean hasDefect = records.stream().anyMatch(r -> r.getResult() == 0);
        vo.setFinalConclusion(hasDefect ? "存在不合格缺陷，请查看处置详情" : "全部检测合格");
        return vo;
    }
}
