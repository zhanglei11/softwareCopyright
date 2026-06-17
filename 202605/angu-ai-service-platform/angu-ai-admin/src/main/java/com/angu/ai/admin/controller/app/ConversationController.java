package com.angu.ai.admin.controller.app;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.common.utils.SecurityUtils;
import com.angu.ai.system.domain.dto.SendMessageDTO;
import com.angu.ai.system.domain.entity.AiConversation;
import com.angu.ai.system.domain.query.ConversationQuery;
import com.angu.ai.system.domain.vo.ConversationVO;
import com.angu.ai.system.domain.vo.MessageVO;
import com.angu.ai.system.service.IAiConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;
import java.util.Map;

@Tag(name = "对话管理")
@RestController
@RequestMapping("/api/app/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final IAiConversationService conversationService;

    @Operation(summary = "我的对话列表")
    @GetMapping
    public AjaxResult<TableDataInfo<ConversationVO>> page(ConversationQuery query) {
        return AjaxResult.success(conversationService.pageList(query, SecurityUtils.getUserId()));
    }

    @Operation(summary = "创建对话")
    @PostMapping
    public AjaxResult<AiConversation> create(@RequestBody Map<String, Long> body) {
        return AjaxResult.success(conversationService.create(SecurityUtils.getUserId(), body.get("sceneId")));
    }

    @Operation(summary = "删除对话")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        conversationService.deleteById(id, SecurityUtils.getUserId()); return AjaxResult.success();
    }

    @Operation(summary = "重命名对话")
    @PutMapping("/{id}/title")
    public AjaxResult<Void> rename(@PathVariable Long id, @RequestBody Map<String, String> body) {
        conversationService.renameTitle(id, SecurityUtils.getUserId(), body.get("title")); return AjaxResult.success();
    }

    @Operation(summary = "消息历史")
    @GetMapping("/{id}/messages")
    public AjaxResult<List<MessageVO>> messages(@PathVariable Long id) {
        return AjaxResult.success(conversationService.getMessages(id, SecurityUtils.getUserId()));
    }

    @Operation(summary = "发送消息（SSE流式）")
    @PostMapping("/{id}/messages")
    public SseEmitter send(@PathVariable Long id, @RequestBody SendMessageDTO dto) {
        return conversationService.sendMessage(id, SecurityUtils.getUserId(), dto);
    }
}
