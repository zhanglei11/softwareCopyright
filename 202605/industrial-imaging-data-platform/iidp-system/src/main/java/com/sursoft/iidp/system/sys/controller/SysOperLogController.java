package com.sursoft.iidp.system.sys.controller;

import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.sys.domain.SysOperLog;
import com.sursoft.iidp.system.sys.mapper.SysOperLogMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "操作日志")
@RestController
@RequestMapping("/api/logs/operation")
@RequiredArgsConstructor
public class SysOperLogController extends BaseController {
    private final SysOperLogMapper operLogMapper;

    @Operation(summary = "操作日志列表")
    @PreAuthorize("hasAuthority('logs:operation:list')")
    @GetMapping
    public TableDataInfo<SysOperLog> list(SysOperLog query, PageDomain page) {
        startPage(page);
        return getDataTable(operLogMapper.selectList(query));
    }
}
