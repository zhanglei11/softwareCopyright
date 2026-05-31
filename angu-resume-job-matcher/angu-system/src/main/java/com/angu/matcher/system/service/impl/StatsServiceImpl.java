package com.angu.matcher.system.service.impl;

import com.angu.matcher.system.mapper.JobApplicationMapper;
import com.angu.matcher.system.mapper.JobPositionMapper;
import com.angu.matcher.system.mapper.ResumeMainMapper;
import com.angu.matcher.system.service.IStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements IStatsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getDashboard() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("monthlyApplications", queryCount(
            "SELECT COUNT(*) FROM job_application WHERE MONTH(created_time)=MONTH(NOW()) AND YEAR(created_time)=YEAR(NOW())"));
        result.put("monthlyInterviewDone", queryCount(
            "SELECT COUNT(*) FROM interview_record WHERE MONTH(created_time)=MONTH(NOW()) AND YEAR(created_time)=YEAR(NOW()) AND result IS NOT NULL"));
        result.put("monthlyHired", queryCount(
            "SELECT COUNT(*) FROM job_application WHERE status='HIRED' AND MONTH(operate_time)=MONTH(NOW()) AND YEAR(operate_time)=YEAR(NOW())"));

        Map<String, Object> funnel = new LinkedHashMap<>();
        funnel.put("applied", queryCount("SELECT COUNT(*) FROM job_application WHERE MONTH(created_time)=MONTH(NOW()) AND YEAR(created_time)=YEAR(NOW())"));
        funnel.put("passed", queryCount("SELECT COUNT(*) FROM job_application WHERE status NOT IN('PENDING','RESUME_REJECTED') AND MONTH(created_time)=MONTH(NOW()) AND YEAR(created_time)=YEAR(NOW())"));
        funnel.put("interviewed", queryCount("SELECT COUNT(*) FROM job_application WHERE status IN('INTERVIEWING','INTERVIEW_PASSED','INTERVIEW_REJECTED','HIRED') AND MONTH(created_time)=MONTH(NOW()) AND YEAR(created_time)=YEAR(NOW())"));
        funnel.put("hired", queryCount("SELECT COUNT(*) FROM job_application WHERE status='HIRED' AND MONTH(created_time)=MONTH(NOW()) AND YEAR(created_time)=YEAR(NOW())"));
        result.put("funnel", funnel);
        return result;
    }

    @Override
    public Map<String, Object> getSourceStats(String startDate, String endDate) {
        Map<String, Object> result = new LinkedHashMap<>();
        String sql = "SELECT source, COUNT(*) as cnt FROM resume_main WHERE deleted=0";
        if (startDate != null && !startDate.isBlank()) sql += " AND DATE(created_time) >= '" + startDate + "'";
        if (endDate != null && !endDate.isBlank()) sql += " AND DATE(created_time) <= '" + endDate + "'";
        sql += " GROUP BY source";
        List<Map<String, Object>> distribution = jdbcTemplate.queryForList(sql);
        result.put("distribution", distribution);
        return result;
    }

    private int queryCount(String sql) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
}
