package com.sursoft.iidp.system.datasource.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.datasource.domain.DatasourceConfig;
import com.sursoft.iidp.system.datasource.domain.DatasourceConnLog;
import com.sursoft.iidp.system.datasource.service.DatasourceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "数据源管理")
@RestController
@RequestMapping("/api/datasource")
@RequiredArgsConstructor
public class DatasourceController extends BaseController {
    private final DatasourceConfigService datasourceService;

    @Operation(summary = "数据源列表")
    @PreAuthorize("hasAuthority('datasource:config:list')")
    @GetMapping
    public TableDataInfo<DatasourceConfig> list(DatasourceConfig query, PageDomain page) {
        startPage(page);
        return getDataTable(datasourceService.listDatasources(query));
    }

    @Operation(summary = "数据源详情")
    @PreAuthorize("hasAuthority('datasource:config:list')")
    @GetMapping("/{id}")
    public AjaxResult<DatasourceConfig> getInfo(@PathVariable Long id) {
        return AjaxResult.success(datasourceService.getById(id));
    }

    @Operation(summary = "新增数据源")
    @PreAuthorize("hasAuthority('datasource:config:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody DatasourceConfig config,
                                @AuthenticationPrincipal LoginUser user) {
        datasourceService.addDatasource(config, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改数据源")
    @PreAuthorize("hasAuthority('datasource:config:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody DatasourceConfig config,
                                 @AuthenticationPrincipal LoginUser user) {
        config.setId(id);
        datasourceService.editDatasource(config, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改数据源状态")
    @PreAuthorize("hasAuthority('datasource:config:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        datasourceService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "删除数据源")
    @PreAuthorize("hasAuthority('datasource:config:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> remove(@PathVariable Long id) {
        datasourceService.removeDatasource(id);
        return AjaxResult.success();
    }

    @Operation(summary = "连通性测试")
    @PreAuthorize("hasAuthority('datasource:config:edit')")
    @PostMapping("/{id}/test")
    public AjaxResult<DatasourceConnLog> testConn(@PathVariable Long id,
                                                   @AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(datasourceService.testConnection(id, user.getUserId()));
    }
}
