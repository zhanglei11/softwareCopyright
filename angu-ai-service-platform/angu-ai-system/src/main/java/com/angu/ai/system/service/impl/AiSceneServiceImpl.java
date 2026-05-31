package com.angu.ai.system.service.impl;

import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.AiSceneDTO;
import com.angu.ai.system.domain.entity.AiScene;
import com.angu.ai.system.domain.entity.UserSceneFavorite;
import com.angu.ai.system.domain.query.SceneQuery;
import com.angu.ai.system.domain.vo.SceneVO;
import com.angu.ai.system.mapper.AiSceneMapper;
import com.angu.ai.system.mapper.UserSceneFavoriteMapper;
import com.angu.ai.system.service.IAiSceneService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiSceneServiceImpl implements IAiSceneService {
    private final AiSceneMapper sceneMapper;
    private final UserSceneFavoriteMapper favoriteMapper;

    @Override
    public TableDataInfo<SceneVO> pageList(SceneQuery query) {
        PageHelper.startPage(query.getPage(), query.getSize());
        List<SceneVO> list = sceneMapper.selectPage(query);
        PageInfo<SceneVO> info = new PageInfo<>(list);
        return TableDataInfo.of(info.getTotal(), info.getPages(), list);
    }

    @Override
    public SceneVO getById(Long id) {
        SceneVO vo = sceneMapper.selectById(id);
        if (vo == null) throw new ServiceException(404, "场景不存在");
        return vo;
    }

    @Override
    @Transactional
    public void create(AiSceneDTO dto, Long creatorId) {
        AiScene scene = new AiScene();
        copyDtoToEntity(dto, scene);
        scene.setCreatorId(creatorId);
        scene.setStatus("DRAFT");
        sceneMapper.insert(scene);
    }

    @Override
    public void update(Long id, AiSceneDTO dto) {
        if (sceneMapper.selectEntityById(id) == null) throw new ServiceException(404, "场景不存在");
        AiScene scene = new AiScene();
        copyDtoToEntity(dto, scene);
        scene.setId(id);
        sceneMapper.updateById(scene);
    }

    @Override
    public void deleteById(Long id) {
        if (sceneMapper.selectEntityById(id) == null) throw new ServiceException(404, "场景不存在");
        sceneMapper.deleteById(id);
    }

    @Override
    public void publish(Long id) {
        AiScene s = sceneMapper.selectEntityById(id);
        if (s == null) throw new ServiceException(404, "场景不存在");
        sceneMapper.updateStatus(id, "ONLINE");
    }

    @Override
    public void offline(Long id) {
        AiScene s = sceneMapper.selectEntityById(id);
        if (s == null) throw new ServiceException(404, "场景不存在");
        sceneMapper.updateStatus(id, "OFFLINE");
    }

    @Override
    public List<SceneVO> getOnlineByUser(Long userId, String keyword) {
        return sceneMapper.selectOnlineByUserId(userId, keyword);
    }

    @Override
    public void addFavorite(Long userId, Long sceneId) {
        if (favoriteMapper.selectByUserAndScene(userId, sceneId) == null) {
            UserSceneFavorite fav = new UserSceneFavorite();
            fav.setUserId(userId); fav.setSceneId(sceneId);
            favoriteMapper.insert(fav);
        }
    }

    @Override
    public void removeFavorite(Long userId, Long sceneId) {
        favoriteMapper.deleteByUserAndScene(userId, sceneId);
    }

    @Override
    public List<SceneVO> getFavorites(Long userId) {
        return favoriteMapper.selectFavoritesByUserId(userId);
    }

    private void copyDtoToEntity(AiSceneDTO dto, AiScene scene) {
        scene.setName(dto.getName()); scene.setCategoryId(dto.getCategoryId());
        scene.setIcon(dto.getIcon()); scene.setDescription(dto.getDescription());
        scene.setUsageGuide(dto.getUsageGuide()); scene.setModelId(dto.getModelId());
        scene.setKbId(dto.getKbId()); scene.setSystemPrompt(dto.getSystemPrompt());
        scene.setUserPromptTpl(dto.getUserPromptTpl()); scene.setInputVariables(dto.getInputVariables());
        scene.setMaxTokens(dto.getMaxTokens()); scene.setTemperature(dto.getTemperature());
        scene.setMultiTurn(dto.getMultiTurn());
    }
}
