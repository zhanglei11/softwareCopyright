package com.angu.ai.system.service;

import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.system.domain.dto.AiSceneDTO;
import com.angu.ai.system.domain.query.SceneQuery;
import com.angu.ai.system.domain.vo.SceneVO;

import java.util.List;

public interface IAiSceneService {
    TableDataInfo<SceneVO> pageList(SceneQuery query);
    SceneVO getById(Long id);
    void create(AiSceneDTO dto, Long creatorId);
    void update(Long id, AiSceneDTO dto);
    void deleteById(Long id);
    void publish(Long id);
    void offline(Long id);
    List<SceneVO> getOnlineByUser(Long userId, String keyword);
    void addFavorite(Long userId, Long sceneId);
    void removeFavorite(Long userId, Long sceneId);
    List<SceneVO> getFavorites(Long userId);
}
