package com.sursoft.iidp.system.stats.service.impl;

import com.sursoft.iidp.system.datasource.mapper.DatasourceConfigMapper;
import com.sursoft.iidp.system.ingest.mapper.IngestRecordMapper;
import com.sursoft.iidp.system.ingest.mapper.IngestTaskMapper;
import com.sursoft.iidp.system.process.mapper.ProcessTaskMapper;
import com.sursoft.iidp.system.stats.dto.OverviewDTO;
import com.sursoft.iidp.system.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final DatasourceConfigMapper datasourceMapper;
    private final IngestTaskMapper ingestTaskMapper;
    private final IngestRecordMapper ingestRecordMapper;
    private final ProcessTaskMapper processTaskMapper;

    @Override
    public OverviewDTO getOverview() {
        OverviewDTO dto = new OverviewDTO();
        dto.setDatasourceCount(datasourceMapper.countTotal());
        dto.setActiveDatasourceCount(datasourceMapper.countByStatus(1));
        dto.setTodayIngestCount(ingestRecordMapper.countToday());
        dto.setTodayIngestSize(ingestRecordMapper.sumTodaySize());
        dto.setTodayProcessCount(0L);
        dto.setIngestTaskCount(ingestTaskMapper.countTotal());
        dto.setProcessTaskCount(processTaskMapper.countTotal());
        File root = new File("/");
        long total = root.getTotalSpace();
        dto.setStorageUsageRate(total > 0
                ? Math.round((total - root.getFreeSpace()) * 10000.0 / total) / 100.0
                : 0.0);
        return dto;
    }

    @Override
    public List<Map<String, Object>> getIngestTrend(String days) {
        int d = days == null ? 7 : Integer.parseInt(days);
        return ingestRecordMapper.selectIngestTrend(d);
    }

    @Override
    public Map<String, Object> getProcessSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", processTaskMapper.countTotal());
        result.put("activeCount", processTaskMapper.countByStatus(1));
        return result;
    }

    @Override
    public List<Map<String, Object>> getIngestAnalysis() {
        return ingestRecordMapper.selectStatusAnalysis();
    }

    @Override
    public List<Map<String, Object>> getDatasourceContribution() {
        return ingestRecordMapper.selectDatasourceContribution();
    }

    @Override
    public List<Map<String, Object>> getFileTypeDistribution() {
        return new ArrayList<>();
    }
}
