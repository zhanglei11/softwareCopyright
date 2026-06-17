package com.sursoft.vision.admin.controller.defect;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.framework.security.LoginUser;
import com.sursoft.vision.system.domain.DefectImage;
import com.sursoft.vision.system.dto.DefectDisposeDTO;
import com.sursoft.vision.system.query.DefectRecordQuery;
import com.sursoft.vision.system.service.DefectRecordService;
import com.sursoft.vision.system.vo.DefectRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/defect/records")
@Tag(name = "缺陷记录管理")
@RequiredArgsConstructor
public class DefectRecordController {

    private final DefectRecordService recordService;

    @GetMapping
    @Operation(summary = "查询缺陷记录列表")
    @PreAuthorize("hasAuthority('defect:record:list')")
    public TableDataInfo<DefectRecordVO> list(DefectRecordQuery query) {
        return recordService.list(query);
    }

    @GetMapping("/{id}")
    @Operation(summary = "缺陷记录详情")
    @PreAuthorize("hasAuthority('defect:record:list')")
    public AjaxResult<DefectRecordVO> detail(@PathVariable Long id) {
        return AjaxResult.success(recordService.getById(id));
    }

    @PatchMapping("/{id}/dispose")
    @Operation(summary = "更新处置状态")
    @PreAuthorize("hasAuthority('defect:record:dispose')")
    public AjaxResult<Void> dispose(@PathVariable Long id,
            @Valid @RequestBody DefectDisposeDTO dto,
            @AuthenticationPrincipal LoginUser loginUser) {
        recordService.dispose(id, dto, loginUser.getUser().getId());
        return AjaxResult.success();
    }

    @GetMapping("/{id}/images")
    @Operation(summary = "查看缺陷影像")
    @PreAuthorize("hasAuthority('defect:record:list')")
    public AjaxResult<List<DefectImage>> images(@PathVariable Long id) {
        return AjaxResult.success(recordService.getImages(id));
    }

    @GetMapping("/export")
    @Operation(summary = "导出缺陷记录 Excel")
    @PreAuthorize("hasAuthority('defect:record:export')")
    public void export(DefectRecordQuery query, HttpServletResponse response) {
        recordService.exportExcel(query, response);
    }
}
