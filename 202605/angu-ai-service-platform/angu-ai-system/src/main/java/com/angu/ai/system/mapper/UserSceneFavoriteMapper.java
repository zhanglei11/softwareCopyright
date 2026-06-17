package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.UserSceneFavorite;
import com.angu.ai.system.domain.vo.SceneVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserSceneFavoriteMapper {
    List<SceneVO> selectFavoritesByUserId(@Param("userId") Long userId);
    UserSceneFavorite selectByUserAndScene(@Param("userId") Long userId, @Param("sceneId") Long sceneId);
    int insert(UserSceneFavorite favorite);
    int deleteByUserAndScene(@Param("userId") Long userId, @Param("sceneId") Long sceneId);
}
