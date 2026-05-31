package com.sva.admin.controller.result;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.RecognitionBox;
import com.sva.system.query.ResultQuery;
import com.sva.system.service.IRecognitionResultService;
import com.sva.system.vo.ResultDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "识别结果管理")
@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController extends BaseController {

    private final IRecognitionResultService resultService;

    @Operation(summary = "结果列表")
    @PreAuthorize("hasAuthority('result:list')")
    @GetMapping
    public TableDataInfo list(ResultQuery query,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(resultService.list(query));
    }

    @Operation(summary = "结果详情（含检测框）")
    @GetMapping("/{id}")
    public AjaxResult<ResultDetailVO> getDetail(@PathVariable Long id) {
        return AjaxResult.success(resultService.getDetail(id));
    }

    @Operation(summary = "添加检测框")
    @PreAuthorize("hasAuthority('result:review')")
    @PostMapping("/{id}/boxes")
    public AjaxResult<RecognitionBox> addBox(@PathVariable Long id,
                                              @RequestBody RecognitionBox box,
                                              @AuthenticationPrincipal LoginUser user) {
        box.setUpdatedBy(user.getUserId());
        return AjaxResult.success(resultService.addBox(id, box));
    }

    @Operation(summary = "修改检测框")
    @PreAuthorize("hasAuthority('result:review')")
    @PutMapping("/{id}/boxes/{boxId}")
    public AjaxResult<Void> updateBox(@PathVariable Long id, @PathVariable Long boxId,
                                       @RequestBody RecognitionBox box,
                                       @AuthenticationPrincipal LoginUser user) {
        box.setUpdatedBy(user.getUserId());
        resultService.updateBox(id, boxId, box);
        return AjaxResult.success();
    }

    @Operation(summary = "删除检测框")
    @PreAuthorize("hasAuthority('result:review')")
    @DeleteMapping("/{id}/boxes/{boxId}")
    public AjaxResult<Void> deleteBox(@PathVariable Long id, @PathVariable Long boxId) {
        resultService.deleteBox(id, boxId);
        return AjaxResult.success();
    }

    @Operation(summary = "确认结果")
    @PreAuthorize("hasAuthority('result:review')")
    @PostMapping("/{id}/confirm")
    public AjaxResult<Void> confirm(@PathVariable Long id) {
        resultService.confirm(id);
        return AjaxResult.success();
    }

    @Operation(summary = "驳回结果")
    @PreAuthorize("hasAuthority('result:review')")
    @PostMapping("/{id}/reject")
    public AjaxResult<Void> reject(@PathVariable Long id) {
        resultService.reject(id);
        return AjaxResult.success();
    }

    @Operation(summary = "批量审核")
    @PreAuthorize("hasAuthority('result:review')")
    @PostMapping("/batch/review")
    public AjaxResult<Void> batchReview(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("ids");
        Integer reviewStatus = (Integer) body.get("reviewStatus");
        resultService.batchReview(ids, reviewStatus);
        return AjaxResult.success();
    }
}
