package com.sva.admin.controller.image;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.ImageFile;
import com.sva.system.query.ImageQuery;
import com.sva.system.service.IImageFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "图像文件管理")
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController extends BaseController {

    private final IImageFileService imageService;

    @Operation(summary = "图像列表")
    @PreAuthorize("hasAuthority('image:file:list')")
    @GetMapping
    public TableDataInfo list(ImageQuery query,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(imageService.list(query));
    }

    @Operation(summary = "图像详情")
    @GetMapping("/{id}")
    public AjaxResult<ImageFile> getInfo(@PathVariable Long id) {
        return AjaxResult.success(imageService.getById(id));
    }

    @Operation(summary = "上传图像")
    @PreAuthorize("hasAuthority('image:file:upload')")
    @PostMapping("/upload")
    public AjaxResult<ImageFile> upload(@RequestParam("file") MultipartFile file,
                                        @RequestParam(required = false) Long categoryId,
                                        @AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(imageService.upload(file, categoryId, user.getUserId()));
    }

    @Operation(summary = "删除图像")
    @PreAuthorize("hasAuthority('image:file:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        imageService.deleteById(id);
        return AjaxResult.success();
    }
}
