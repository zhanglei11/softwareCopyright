package com.sursoft.sfd.admin.controller.datasource;

import com.github.pagehelper.PageInfo;
import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.common.core.PageResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.DatasourceConfig;
import com.sursoft.sfd.system.service.IDatasourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "数据源管理")
@RestController
@RequestMapping("/api/datasource")
@RequiredArgsConstructor
public class DatasourceController extends BaseController {
    private final IDatasourceService dsService;

    @Operation(summary = "数据源列表")
    @GetMapping
    public AjaxResult<PageResult<DatasourceConfig>> list(
            @RequestParam(required = false) String sceneType,
            @RequestParam(required = false) String dsType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return getDataTable(pageNum, pageSize, () -> dsService.list(sceneType, dsType, status, keyword));
    }

    @Operation(summary = "数据源详情")
    @GetMapping("/{id}")
    public AjaxResult<DatasourceConfig> getById(@PathVariable Long id) { return AjaxResult.ok(dsService.getById(id)); }

    @Operation(summary = "新增数据源")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody DatasourceConfig config) { dsService.add(config, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑数据源")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody DatasourceConfig config) { dsService.edit(id, config, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "启用/禁用数据源")
    @PutMapping("/{id}/status")
    public AjaxResult<Void> status(@PathVariable Long id, @RequestParam Integer status) { dsService.updateStatus(id, status, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "测试连接")
    @PostMapping("/{id}/test-conn")
    public AjaxResult<Boolean> testConn(@PathVariable Long id) { return AjaxResult.ok(dsService.testConn(id)); }

    @Operation(summary = "状态总览")
    @GetMapping("/status-overview")
    public AjaxResult<List<DatasourceConfig>> statusOverview() { return AjaxResult.ok(dsService.statusOverview()); }

    @Operation(summary = "删除数据源")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) { dsService.delete(id, getCurrentUserId()); return AjaxResult.ok(); }
}
