package com.vqcc.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.QualityDetectionRecord;
import com.vqcc.system.domain.QualityDefect;
import com.vqcc.system.domain.QualityMetric;
import com.vqcc.system.domain.QualityTask;
import com.vqcc.system.dto.request.DetectionRecordSubmitReq;
import com.vqcc.system.dto.request.TaskCreateReq;
import com.vqcc.system.mapper.QualityDefectMapper;
import com.vqcc.system.mapper.QualityDetectionRecordMapper;
import com.vqcc.system.mapper.QualityMetricMapper;
import com.vqcc.system.mapper.QualityTaskMapper;
import com.vqcc.system.service.IQualityTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QualityTaskServiceImpl implements IQualityTaskService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final QualityTaskMapper taskMapper;
    private final QualityDetectionRecordMapper recordMapper;
    private final QualityDefectMapper defectMapper;
    private final QualityMetricMapper metricMapper;

    @Override
    public List<QualityTask> list(String taskName, String detectionTarget, Integer status, Long templateId) {
        return taskMapper.selectList(taskName, detectionTarget, status, templateId);
    }

    @Override
    public QualityTask getById(Long id) {
        QualityTask task = taskMapper.selectById(id);
        if (task == null) throw new BusinessException(404, "检测任务不存在");
        return task;
    }

    @Override
    public void create(TaskCreateReq req, Long operatorId) {
        QualityTask task = new QualityTask();
        task.setTaskCode(generateTaskCode());
        task.setTaskName(req.getTaskName());
        task.setDetectionTarget(req.getDetectionTarget());
        task.setTemplateId(req.getTemplateId());
        task.setImageCount(req.getImageCount());
        task.setPlanExecuteTime(req.getPlanExecuteTime());
        task.setPriority(req.getPriority() != null ? req.getPriority() : 1);
        task.setRemark(req.getRemark());
        task.setStatus(1);
        task.setQualifiedCount(0);
        task.setUnqualifiedCount(0);
        task.setCreatedBy(operatorId);
        task.setUpdatedBy(operatorId);
        taskMapper.insert(task);
    }

    @Override
    public void startTask(Long id, Long operatorId) {
        QualityTask task = getById(id);
        if (task.getStatus() != 1) throw new BusinessException(400, "只有待执行状态的任务可以开始");
        task.setStatus(2);
        task.setStartTime(LocalDateTime.now());
        task.setUpdatedBy(operatorId);
        taskMapper.update(task);
    }

    @Override
    public void completeTask(Long id, Long operatorId) {
        QualityTask task = getById(id);
        if (task.getStatus() != 2) throw new BusinessException(400, "只有执行中状态的任务可以完成");
        long total = recordMapper.countByTaskId(id);
        long qualified = recordMapper.countQualifiedByTaskId(id);
        long unqualified = total - qualified;
        task.setStatus(3);
        task.setEndTime(LocalDateTime.now());
        task.setQualifiedCount((int) qualified);
        task.setUnqualifiedCount((int) unqualified);
        task.setUpdatedBy(operatorId);
        taskMapper.update(task);
        if (total > 0) {
            taskMapper.updateStatistics(id, (int) qualified, (int) unqualified);
        }
    }

    @Override
    public void cancelTask(Long id, Long operatorId) {
        QualityTask task = getById(id);
        if (task.getStatus() == 3 || task.getStatus() == 4) {
            throw new BusinessException(400, "已完成或已取消的任务无法取消");
        }
        task.setStatus(4);
        task.setUpdatedBy(operatorId);
        taskMapper.update(task);
    }

    @Override
    @Transactional
    public QualityDetectionRecord submitRecord(DetectionRecordSubmitReq req, Long operatorId) {
        QualityTask task = getById(req.getTaskId());
        if (task.getStatus() != 2) throw new BusinessException(400, "任务不在执行中状态，无法提交检测记录");

        // 根据模板指标判断合格性
        List<QualityMetric> metrics = metricMapper.selectByTemplateId(task.getTemplateId());
        Map<Long, QualityMetric> metricMap = metrics.stream()
                .collect(Collectors.toMap(QualityMetric::getId, m -> m));

        List<Long> exceededIds = new ArrayList<>();
        Map<Long, Object> exceededVals = new LinkedHashMap<>();
        Map<Long, Map<String, Object>> standardRanges = new LinkedHashMap<>();

        try {
            List<Map<String, Object>> measured = OBJECT_MAPPER.readValue(
                    req.getMeasuredValues(), new TypeReference<>() {});
            for (Map<String, Object> mv : measured) {
                Long metricId = ((Number) mv.get("metricId")).longValue();
                Object valObj = mv.get("value");
                QualityMetric metric = metricMap.get(metricId);
                if (metric == null || valObj == null) continue;
                // 仅对数值型指标（metricType=0）做范围校验
                if (metric.getMetricType() == 0
                        && metric.getMinValue() != null && metric.getMaxValue() != null) {
                    BigDecimal value = new BigDecimal(valObj.toString());
                    if (value.compareTo(metric.getMinValue()) < 0
                            || value.compareTo(metric.getMaxValue()) > 0) {
                        exceededIds.add(metricId);
                        exceededVals.put(metricId, value);
                        Map<String, Object> range = new LinkedHashMap<>();
                        range.put("min", metric.getMinValue());
                        range.put("max", metric.getMaxValue());
                        standardRanges.put(metricId, range);
                    }
                }
            }
        } catch (Exception e) {
            // measuredValues 格式异常时按合格处理，不阻断流程
        }

        QualityDetectionRecord record = new QualityDetectionRecord();
        record.setTaskId(req.getTaskId());
        record.setImageId(req.getImageId());
        record.setMeasuredValues(req.getMeasuredValues());
        record.setIsQualified(exceededIds.isEmpty() ? 1 : 0);
        if (!exceededIds.isEmpty()) {
            try {
                record.setExceededMetrics(OBJECT_MAPPER.writeValueAsString(exceededIds));
            } catch (Exception ignored) {}
        }
        record.setDetectedAt(LocalDateTime.now());
        recordMapper.insert(record);

        // 若不合格，自动登记不合格品
        if (record.getIsQualified() == 0) {
            try {
                String defectCode = generateDefectCode();
                QualityDefect defect = new QualityDefect();
                defect.setDefectCode(defectCode);
                defect.setTaskId(req.getTaskId());
                defect.setImageId(req.getImageId());
                defect.setExceededMetrics(record.getExceededMetrics());
                defect.setExceededValues(OBJECT_MAPPER.writeValueAsString(exceededVals));
                defect.setStandardRanges(OBJECT_MAPPER.writeValueAsString(standardRanges));
                defect.setFoundAt(LocalDateTime.now());
                defect.setCreatedBy(operatorId);
                defectMapper.insert(defect);
            } catch (Exception e) {
                throw new BusinessException(500, "不合格品登记失败：" + e.getMessage());
            }
        }
        return record;
    }

    @Override
    public List<QualityDetectionRecord> getRecords(Long taskId, Integer isQualified) {
        return recordMapper.selectByTaskId(taskId, isQualified);
    }

    private String generateTaskCode() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String max = taskMapper.selectMaxCodeByDate(dateStr);
        if (max == null) return "TASK-" + dateStr + "-001";
        try {
            int num = Integer.parseInt(max.substring(max.lastIndexOf('-') + 1)) + 1;
            return String.format("TASK-%s-%03d", dateStr, num);
        } catch (Exception e) {
            return "TASK-" + dateStr + "-" + System.currentTimeMillis() % 1000;
        }
    }

    private String generateDefectCode() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String max = defectMapper.selectMaxCodeByDate(dateStr);
        if (max == null) return "DEF-" + dateStr + "-001";
        try {
            int num = Integer.parseInt(max.substring(max.lastIndexOf('-') + 1)) + 1;
            return String.format("DEF-%s-%03d", dateStr, num);
        } catch (Exception e) {
            return "DEF-" + dateStr + "-" + System.currentTimeMillis() % 1000;
        }
    }
}
