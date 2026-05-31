package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.AiScene;
import com.angu.ai.system.domain.query.SceneQuery;
import com.angu.ai.system.domain.vo.SceneVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiSceneMapper {
    List<SceneVO> selectPage(@Param("q") SceneQuery query);
    SceneVO selectById(@Param("id") Long id);
    AiScene selectEntityById(@Param("id") Long id);
    int insert(AiScene scene);
    int updateById(AiScene scene);
    int deleteById(@Param("id") Long id);
    List<SceneVO> selectOnlineByUserId(@Param("userId") Long userId, @Param("keyword") String keyword);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
