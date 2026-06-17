package com.vqcc.admin.controller.quality;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.QualityMetric;
import com.vqcc.system.dto.request.MetricCreateReq;
import com.vqcc.system.service.IQualityMetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "质量指标管理")
@RestController
@RequestMapping("/api/v1/quality/metrics")
@RequiredArgsConstructor
public class QualityMetricController {

    private final IQualityMetricService metricService;

    @Operation(summary = "指标列表")
    @PreAuthorize("hasAuthority('quality:metric:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<QualityMetric>> list(
            @RequestParam(required = false) String metricName,
            @RequestParam(required = false) Integer metricType,
            @RequestParam(required = false) Integer status) {
        return AjaxResult.ok(TableDataInfo.ok(metricService.list(metricName, metricType, status)));
    }

    @Operation(summary = "指标详情")
    @PreAuthorize("hasAuthority('quality:metric:list')")
    @GetMapping("/{id}")
    public AjaxResult<QualityMetric> getById(@PathVariable Long id) {
        return AjaxResult.ok(metricService.getById(id));
    }

    @Operation(summary = "创建指标")
    @PreAuthorize("hasAuthority('quality:metric:add')")
    @PostMapping
    public AjaxResult<Void> create(@Valid @RequestBody MetricCreateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        metricService.create(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "更新指标")
    @PreAuthorize("hasAuthority('quality:metric:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody QualityMetric metric,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        metric.setId(id);
        metricService.update(metric, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "删除指标")
    @PreAuthorize("hasAuthority('quality:metric:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        metricService.delete(id);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "修改状态")
    @PreAuthorize("hasAuthority('quality:metric:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body,
                                          @AuthenticationPrincipal LoginUser loginUser) {
        metricService.updateStatus(id, body.get("status"), loginUser.getUserId());
        return AjaxResult.ok(null);
    }
}
