package com.sva.system.mapper;

import com.sva.system.domain.ReportTaskSummary;
import com.sva.system.vo.SummaryReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportTaskSummaryMapper {
    ReportTaskSummary selectByTaskId(@Param("taskId") Long taskId);
    int insert(ReportTaskSummary summary);
    int update(ReportTaskSummary summary);

    @Select("<script>SELECT COUNT(*) FROM recognition_task WHERE status=3" +
            "<if test='startDate!=null'> AND DATE(created_at)&gt;=#{startDate}</if>" +
            "<if test='endDate!=null'> AND DATE(created_at)&lt;=#{endDate}</if></script>")
    Integer countTasks(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("<script>SELECT IFNULL(SUM(rts.total_images),0) AS totalImages," +
            "ROUND(IFNULL(SUM(rts.success_count),0)/NULLIF(SUM(rts.total_images),0),4) AS successRate," +
            "ROUND(AVG(rts.avg_confidence),4) AS avgConfidence" +
            " FROM report_task_summary rts JOIN recognition_task rt ON rts.task_id=rt.id WHERE 1=1" +
            "<if test='startDate!=null'> AND DATE(rts.generated_at)&gt;=#{startDate}</if>" +
            "<if test='endDate!=null'> AND DATE(rts.generated_at)&lt;=#{endDate}</if></script>")
    Map<String, Object> selectAggregates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("<script>SELECT DATE(rts.generated_at) AS date, COUNT(*) AS count" +
            " FROM report_task_summary rts WHERE 1=1" +
            "<if test='startDate!=null'> AND DATE(rts.generated_at)&gt;=#{startDate}</if>" +
            "<if test='endDate!=null'> AND DATE(rts.generated_at)&lt;=#{endDate}</if>" +
            " GROUP BY DATE(rts.generated_at) ORDER BY date ASC</script>")
    List<Map<String, Object>> selectTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("<script>SELECT mv.model_name AS modelName, COUNT(rt.id) AS count" +
            " FROM recognition_task rt JOIN model_version mv ON rt.model_version_id=mv.id" +
            " WHERE rt.status=3" +
            "<if test='startDate!=null'> AND DATE(rt.created_at)&gt;=#{startDate}</if>" +
            "<if test='endDate!=null'> AND DATE(rt.created_at)&lt;=#{endDate}</if>" +
            " GROUP BY mv.model_name</script>")
    List<Map<String, Object>> selectModelDistribution(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("<script>SELECT rt.id AS taskId, rt.task_name AS taskName, mv.model_name AS modelName," +
            " rts.total_images AS totalImages," +
            " ROUND(rts.success_count/NULLIF(rts.total_images,0),4) AS successRate," +
            " rts.avg_confidence AS avgConfidence," +
            " DATE_FORMAT(rt.finished_at,'%Y-%m-%d %H:%i') AS finishTime" +
            " FROM report_task_summary rts JOIN recognition_task rt ON rts.task_id=rt.id" +
            " JOIN model_version mv ON rt.model_version_id=mv.id" +
            "<where><if test='startDate!=null'> AND DATE(rts.generated_at)&gt;=#{startDate}</if>" +
            "<if test='endDate!=null'> AND DATE(rts.generated_at)&lt;=#{endDate}</if></where>" +
            " ORDER BY rt.finished_at DESC</script>")
    List<Map<String, Object>> selectTaskSummaries(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
