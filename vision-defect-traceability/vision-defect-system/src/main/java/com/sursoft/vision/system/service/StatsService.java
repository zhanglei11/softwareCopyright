package com.sursoft.vision.system.service;

import com.sursoft.vision.system.vo.DashboardVO;
import com.sursoft.vision.system.vo.TrendVO;

public interface StatsService {
    DashboardVO dashboard(String date);
    TrendVO trend(String startDate, String endDate, String granularity, Long lineId, Long categoryId);
}
