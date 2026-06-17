package com.angu.ai.admin.controller.kb;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.utils.SecurityUtils;
import com.angu.ai.system.domain.dto.KbDTO;
import com.angu.ai.system.domain.entity.KbDocument;
import com.angu.ai.system.domain.entity.KbKnowledgeBase;
import com.angu.ai.system.service.IKbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Tag(name = "知识库管理")
@RestController
@RequestMapping("/api/kb")
@RequiredArgsConstructor
public class KnowledgeBaseController {
    private final IKbService kbService;

    @Operation(summary = "知识库列表")
    @GetMapping
    @PreAuthorize("hasAuthority('kb:kb:list')")
    public AjaxResult<List<KbKnowledgeBase>> list() { return AjaxResult.success(kbService.listKb()); }

    @Operation(summary = "创建知识库")
    @PostMapping
    @PreAuthorize("hasAuthority('kb:kb:add')")
    public AjaxResult<Void> create(@RequestBody KbDTO dto) {
        kbService.createKb(dto, SecurityUtils.getUserId()); return AjaxResult.success();
    }

    @Operation(summary = "编辑知识库")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:kb:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody KbDTO dto) {
        kbService.updateKb(id, dto); return AjaxResult.success();
    }

    @Operation(summary = "删除知识库")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:kb:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) { kbService.deleteKb(id); return AjaxResult.success(); }

    @Operation(summary = "文档列表")
    @GetMapping("/{kbId}/documents")
    @PreAuthorize("hasAuthority('kb:doc:list')")
    public AjaxResult<List<KbDocument>> docs(@PathVariable Long kbId) { return AjaxResult.success(kbService.listDocuments(kbId)); }

    @Operation(summary = "上传文档")
    @PostMapping("/{kbId}/documents")
    @PreAuthorize("hasAuthority('kb:doc:add')")
    public AjaxResult<Void> upload(@PathVariable Long kbId, @RequestParam("files") MultipartFile[] files) {
        kbService.uploadDocuments(kbId, files); return AjaxResult.success();
    }

    @Operation(summary = "删除文档")
    @DeleteMapping("/{kbId}/documents/{docId}")
    @PreAuthorize("hasAuthority('kb:doc:delete')")
    public AjaxResult<Void> deleteDoc(@PathVariable Long kbId, @PathVariable Long docId) {
        kbService.deleteDocument(kbId, docId); return AjaxResult.success();
    }

    @Operation(summary = "重新解析")
    @PostMapping("/{kbId}/documents/{docId}/retry")
    @PreAuthorize("hasAuthority('kb:doc:edit')")
    public AjaxResult<Void> retry(@PathVariable Long kbId, @PathVariable Long docId) {
        kbService.retryParse(kbId, docId); return AjaxResult.success();
    }
}
