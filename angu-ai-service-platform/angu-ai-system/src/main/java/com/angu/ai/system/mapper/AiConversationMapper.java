package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.AiConversation;
import com.angu.ai.system.domain.query.ConversationQuery;
import com.angu.ai.system.domain.vo.ConversationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiConversationMapper {
    List<ConversationVO> selectPage(@Param("q") ConversationQuery query, @Param("userId") Long userId);
    List<ConversationVO> selectAdminPage(@Param("q") ConversationQuery query);
    ConversationVO selectById(@Param("id") Long id);
    AiConversation selectEntityById(@Param("id") Long id);
    int insert(AiConversation conversation);
    int updateById(AiConversation conversation);
    int deleteById(@Param("id") Long id);
    int updateTitle(@Param("id") Long id, @Param("title") String title);
}
