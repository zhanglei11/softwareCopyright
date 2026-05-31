package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.AiMessage;
import com.angu.ai.system.domain.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiMessageMapper {
    List<MessageVO> selectByConversationId(@Param("conversationId") Long conversationId);
    List<AiMessage> selectEntityByConversationId(@Param("conversationId") Long conversationId);
    int insert(AiMessage message);
    int batchInsert(@Param("messages") List<AiMessage> messages);
}
