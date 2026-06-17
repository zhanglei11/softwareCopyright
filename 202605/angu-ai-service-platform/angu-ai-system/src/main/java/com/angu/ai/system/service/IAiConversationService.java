package com.angu.ai.system.service;

import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.system.domain.dto.SendMessageDTO;
import com.angu.ai.system.domain.entity.AiConversation;
import com.angu.ai.system.domain.query.ConversationQuery;
import com.angu.ai.system.domain.vo.ConversationVO;
import com.angu.ai.system.domain.vo.MessageVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface IAiConversationService {
    TableDataInfo<ConversationVO> pageList(ConversationQuery query, Long userId);
    TableDataInfo<ConversationVO> adminPageList(ConversationQuery query);
    AiConversation create(Long userId, Long sceneId);
    void deleteById(Long id, Long userId);
    void renameTitle(Long id, Long userId, String title);
    List<MessageVO> getMessages(Long conversationId, Long userId);
    SseEmitter sendMessage(Long conversationId, Long userId, SendMessageDTO dto);
}
