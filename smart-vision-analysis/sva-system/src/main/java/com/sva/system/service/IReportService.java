package com.sva.system.service;
import com.sva.system.domain.ImageFile;
import com.sva.system.domain.ReportTaskSummary;
import com.sva.system.vo.SummaryReportVO;
import java.util.List;
public interface IReportService {
    ReportTaskSummary getTaskReport(Long taskId);
    List<ImageFile> getLowConfidenceImages(Long taskId, int page, int pageSize);
    SummaryReportVO getSummaryReport(String startDate, String endDate);
}
