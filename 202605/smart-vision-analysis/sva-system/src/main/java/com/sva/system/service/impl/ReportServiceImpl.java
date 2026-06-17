package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.ImageFile;
import com.sva.system.domain.ReportTaskSummary;
import com.sva.system.mapper.ReportTaskSummaryMapper;
import com.sva.system.service.IReportService;
import com.sva.system.vo.SummaryReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportTaskSummaryMapper summaryMapper;

    @Override public ReportTaskSummary getTaskReport(Long taskId) {
        ReportTaskSummary s = summaryMapper.selectByTaskId(taskId);
        if (s == null) throw new ServiceException(404, "报告尚未生成，请等待任务完成");
        return s;
    }

    @Override public List<ImageFile> getLowConfidenceImages(Long taskId, int page, int pageSize) {
        return List.of();
    }

    @Override
    public SummaryReportVO getSummaryReport(String startDate, String endDate) {
        SummaryReportVO vo = new SummaryReportVO();
        vo.setTotalTasks(summaryMapper.countTasks(startDate, endDate));
        Map<String, Object> agg = summaryMapper.selectAggregates(startDate, endDate);
        if (agg != null) {
            Object ti = agg.get("totalImages");
            vo.setTotalImages(ti != null ? ((Number) ti).intValue() : 0);
            Object sr = agg.get("successRate");
            vo.setSuccessRate(sr != null ? new BigDecimal(sr.toString()) : BigDecimal.ZERO);
            Object ac = agg.get("avgConfidence");
            vo.setAvgConfidence(ac != null ? new BigDecimal(ac.toString()) : BigDecimal.ZERO);
        }
        List<Map<String, Object>> trendRows = summaryMapper.selectTrend(startDate, endDate);
        vo.setTrend(trendRows.stream().map(r -> {
            SummaryReportVO.TrendItem t = new SummaryReportVO.TrendItem();
            t.setDate(r.get("date") != null ? r.get("date").toString() : "");
            t.setCount(((Number) r.get("count")).intValue());
            return t;
        }).collect(Collectors.toList()));
        List<Map<String, Object>> modelRows = summaryMapper.selectModelDistribution(startDate, endDate);
        Map<String, Integer> modelDist = new LinkedHashMap<>();
        modelRows.forEach(r -> modelDist.put(String.valueOf(r.get("modelName")), ((Number) r.get("count")).intValue()));
        vo.setModelDistribution(modelDist);
        List<Map<String, Object>> summaryRows = summaryMapper.selectTaskSummaries(startDate, endDate);
        vo.setTaskSummaries(summaryRows.stream().map(r -> {
            SummaryReportVO.TaskSummaryItem item = new SummaryReportVO.TaskSummaryItem();
            item.setTaskId(r.get("taskId") != null ? ((Number) r.get("taskId")).longValue() : null);
            item.setTaskName(String.valueOf(r.get("taskName")));
            item.setModelName(String.valueOf(r.get("modelName")));
            item.setTotalImages(r.get("totalImages") != null ? ((Number) r.get("totalImages")).intValue() : 0);
            Object sr = r.get("successRate");
            item.setSuccessRate(sr != null ? new BigDecimal(sr.toString()) : BigDecimal.ZERO);
            Object ac = r.get("avgConfidence");
            item.setAvgConfidence(ac != null ? new BigDecimal(ac.toString()) : BigDecimal.ZERO);
            item.setFinishTime(r.get("finishTime") != null ? r.get("finishTime").toString() : null);
            return item;
        }).collect(Collectors.toList()));
        return vo;
    }
}
