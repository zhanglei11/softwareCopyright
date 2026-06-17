package com.angu.ai.system.service.impl;

import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.SendMessageDTO;
import com.angu.ai.system.domain.entity.AiConversation;
import com.angu.ai.system.domain.entity.AiMessage;
import com.angu.ai.system.domain.query.ConversationQuery;
import com.angu.ai.system.domain.vo.ConversationVO;
import com.angu.ai.system.domain.vo.MessageVO;
import com.angu.ai.system.mapper.AiConversationMapper;
import com.angu.ai.system.mapper.AiMessageMapper;
import com.angu.ai.system.service.IAiConversationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiConversationServiceImpl implements IAiConversationService {
    private final AiConversationMapper conversationMapper;
    private final AiMessageMapper messageMapper;

    @Override
    public TableDataInfo<ConversationVO> pageList(ConversationQuery query, Long userId) {
        PageHelper.startPage(query.getPage(), query.getSize());
        List<ConversationVO> list = conversationMapper.selectPage(query, userId);
        PageInfo<ConversationVO> info = new PageInfo<>(list);
        return TableDataInfo.of(info.getTotal(), info.getPages(), list);
    }

    @Override
    public TableDataInfo<ConversationVO> adminPageList(ConversationQuery query) {
        PageHelper.startPage(query.getPage(), query.getSize());
        List<ConversationVO> list = conversationMapper.selectAdminPage(query);
        PageInfo<ConversationVO> info = new PageInfo<>(list);
        return TableDataInfo.of(info.getTotal(), info.getPages(), list);
    }

    @Override
    @Transactional
    public AiConversation create(Long userId, Long sceneId) {
        AiConversation conv = new AiConversation();
        conv.setUserId(userId); conv.setSceneId(sceneId);
        conv.setTitle("新对话 " + LocalDateTime.now().toString().substring(0, 16));
        conversationMapper.insert(conv);
        return conv;
    }

    @Override
    public void deleteById(Long id, Long userId) {
        AiConversation c = conversationMapper.selectEntityById(id);
        if (c == null || !c.getUserId().equals(userId)) throw new ServiceException(403, "无权限");
        conversationMapper.deleteById(id);
    }

    @Override
    public void renameTitle(Long id, Long userId, String title) {
        AiConversation c = conversationMapper.selectEntityById(id);
        if (c == null || !c.getUserId().equals(userId)) throw new ServiceException(403, "无权限");
        conversationMapper.updateTitle(id, title);
    }

    @Override
    public List<MessageVO> getMessages(Long conversationId, Long userId) {
        AiConversation c = conversationMapper.selectEntityById(conversationId);
        if (c == null || !c.getUserId().equals(userId)) throw new ServiceException(403, "无权限");
        return messageMapper.selectByConversationId(conversationId);
    }

    @Override
    public SseEmitter sendMessage(Long conversationId, Long userId, SendMessageDTO dto) {
        AiConversation c = conversationMapper.selectEntityById(conversationId);
        if (c == null || !c.getUserId().equals(userId)) throw new ServiceException(403, "无权限");

        AiMessage userMsg = new AiMessage();
        userMsg.setConversationId(conversationId);
        userMsg.setRole("USER"); userMsg.setContent(dto.getContent());
        messageMapper.insert(userMsg);

        SseEmitter emitter = new SseEmitter(60_000L);
        String mockReply = "【AI回复占位】您发送了：" + dto.getContent();

        new Thread(() -> {
            try {
                for (char ch : mockReply.toCharArray()) {
                    emitter.send(SseEmitter.event().data(String.valueOf(ch)));
                    Thread.sleep(20);
                }
                AiMessage aiMsg = new AiMessage();
                aiMsg.setConversationId(conversationId);
                aiMsg.setRole("ASSISTANT"); aiMsg.setContent(mockReply);
                messageMapper.insert(aiMsg);
                emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }
}
