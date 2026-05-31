package com.sva.admin.controller.model;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.ModelVersion;
import com.sva.system.service.IModelVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "模型版本管理")
@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class ModelController extends BaseController {

    private final IModelVersionService modelService;

    @Operation(summary = "模型列表")
    @PreAuthorize("hasAuthority('model:list')")
    @GetMapping
    public TableDataInfo list(@RequestParam(required = false) String modelName,
                              @RequestParam(required = false) Integer status,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(modelService.list(modelName, status));
    }

    @Operation(summary = "模型详情")
    @GetMapping("/{id}")
    public AjaxResult<ModelVersion> getInfo(@PathVariable Long id) {
        return AjaxResult.success(modelService.getById(id));
    }

    @Operation(summary = "新增模型版本")
    @PreAuthorize("hasAuthority('model:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody ModelVersion version, @AuthenticationPrincipal LoginUser user) {
        version.setCreatedBy(user.getUserId());
        modelService.add(version);
        return AjaxResult.success();
    }

    @Operation(summary = "修改模型版本")
    @PreAuthorize("hasAuthority('model:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody ModelVersion version) {
        modelService.update(id, version);
        return AjaxResult.success();
    }

    @Operation(summary = "废弃模型版本")
    @PreAuthorize("hasAuthority('model:edit')")
    @PostMapping("/{id}/deprecate")
    public AjaxResult<Void> deprecate(@PathVariable Long id) {
        modelService.deprecate(id);
        return AjaxResult.success();
    }

    @Operation(summary = "恢复模型版本")
    @PreAuthorize("hasAuthority('model:edit')")
    @PostMapping("/{id}/restore")
    public AjaxResult<Void> restore(@PathVariable Long id) {
        modelService.restore(id);
        return AjaxResult.success();
    }

    @Operation(summary = "删除模型版本")
    @PreAuthorize("hasAuthority('model:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        modelService.delete(id);
        return AjaxResult.success();
    }
}
