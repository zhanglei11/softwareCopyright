package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.common.result.TableDataInfo;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.dto.ResumeRequest;
import com.angu.matcher.system.service.IResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "简历管理")
@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController extends BaseController {

    private final IResumeService resumeService;

    @Operation(summary = "简历分页列表")
    @PreAuthorize("hasAuthority('resume:resume:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<?>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String highestEdu,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        startPage(page, size);
        return AjaxResult.success(getDataTable(resumeService.listResumes(name, phone, highestEdu, source, skill)));
    }

    @Operation(summary = "简历详情")
    @PreAuthorize("hasAuthority('resume:resume:list')")
    @GetMapping("/{id}")
    public AjaxResult<?> getById(@PathVariable Long id) {
        return AjaxResult.success(resumeService.getById(id));
    }

    @Operation(summary = "录入简历（结构化）")
    @PreAuthorize("hasAuthority('resume:resume:add')")
    @PostMapping
    public AjaxResult<?> create(@RequestBody ResumeRequest req) {
        return AjaxResult.success(resumeService.createResume(req, getUserId()));
    }

    @Operation(summary = "编辑简历")
    @PreAuthorize("hasAuthority('resume:resume:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody ResumeRequest req) {
        resumeService.updateResume(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "删除简历")
    @PreAuthorize("hasAuthority('resume:resume:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return AjaxResult.success();
    }

    @Operation(summary = "上传简历文件")
    @PreAuthorize("hasAuthority('resume:resume:add')")
    @PostMapping("/upload")
    public AjaxResult<?> upload(@RequestParam("file") MultipartFile file) {
        return AjaxResult.success(resumeService.uploadFile(file, getUserId()));
    }

    @Operation(summary = "下载/预览简历文件")
    @PreAuthorize("hasAuthority('resume:resume:list')")
    @GetMapping("/{id}/file")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        resumeService.downloadFile(id, response);
    }

    @Operation(summary = "导出简历 Excel")
    @PreAuthorize("hasAuthority('resume:resume:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        resumeService.exportExcel(response);
    }
}
