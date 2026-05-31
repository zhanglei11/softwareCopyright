package com.sursoft.vision.admin.controller.line;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.LineInfo;
import com.sursoft.vision.system.dto.LineDTO;
import com.sursoft.vision.system.service.LineInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lines")
@Tag(name = "产线管理")
@RequiredArgsConstructor
public class LineController {

    private final LineInfoService lineService;

    @GetMapping
    @Operation(summary = "查询产线列表")
    @PreAuthorize("hasAuthority('line:list')")
    public TableDataInfo<LineInfo> list(@RequestParam(value = "lineName", required=false) String lineName,
            @RequestParam(value = "status", required=false) Integer status,
            @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        return lineService.list(lineName, status, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    @Operation(summary = "产线详情")
    @PreAuthorize("hasAuthority('line:list')")
    public AjaxResult<LineInfo> detail(@PathVariable Long id) {
        return AjaxResult.success(lineService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增产线")
    @PreAuthorize("hasAuthority('line:add')")
    public AjaxResult<Void> add(@Valid @RequestBody LineDTO dto) {
        lineService.add(dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑产线")
    @PreAuthorize("hasAuthority('line:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody LineDTO dto) {
        lineService.edit(id, dto);
        return AjaxResult.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "修改产线状态")
    @PreAuthorize("hasAuthority('line:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        lineService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除产线")
    @PreAuthorize("hasAuthority('line:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        lineService.delete(id);
        return AjaxResult.success();
    }
}
