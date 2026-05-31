package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.system.mapper.DefectRecordMapper;
import com.sursoft.vision.system.service.StatsService;
import com.sursoft.vision.system.vo.DashboardVO;
import com.sursoft.vision.system.vo.TrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final DefectRecordMapper recordMapper;

    @Override
    public DashboardVO dashboard(String date) {
        if (date == null || date.isBlank()) {
            date = LocalDate.now().toString();
        }
        Long total = recordMapper.countTodayTotal(date);
        Long defect = recordMapper.countTodayDefect(date);
        Long qualified = recordMapper.countTodayQualified(date);
        if (total == null) total = 0L;
        if (defect == null) defect = 0L;
        if (qualified == null) qualified = 0L;

        String startDate = LocalDate.now().minusDays(29).toString();
        DashboardVO vo = new DashboardVO();
        vo.setTodayTotal(total);
        vo.setTodayQualifiedRate(total > 0 ? (double) qualified / total : 0);
        vo.setTodayDefectCount(defect);
        vo.setMonthTrend(recordMapper.selectMonthTrend(startDate));
        vo.setCategoryDistribution(recordMapper.selectCategoryDistribution(startDate, date));
        vo.setLineComparison(recordMapper.selectLineComparison(date));
        return vo;
    }

    @Override
    public TrendVO trend(String startDate, String endDate, String granularity, Long lineId, Long categoryId) {
        if (granularity == null || granularity.isBlank()) granularity = "day";
        List<TrendVO.TrendItem> series = recordMapper.selectTrend(startDate, endDate, granularity, lineId, categoryId);
        TrendVO vo = new TrendVO();
        long totalCount = series.stream().mapToLong(TrendVO.TrendItem::getTotalCount).sum();
        double avgQR = series.stream().mapToDouble(TrendVO.TrendItem::getQualifiedRate).average().orElse(0);
        vo.setTotalCount(totalCount);
        vo.setAvgQualifiedRate(Math.round(avgQR * 1000.0) / 10.0);
        vo.setSeries(series);
        return vo;
    }
}
