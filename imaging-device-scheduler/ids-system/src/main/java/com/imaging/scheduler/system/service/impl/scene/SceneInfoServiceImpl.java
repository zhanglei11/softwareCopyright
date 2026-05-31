package com.imaging.scheduler.system.service.impl.scene;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.scene.SceneInfo;
import com.imaging.scheduler.system.dto.req.SceneAddReq;
import com.imaging.scheduler.system.dto.req.SceneQueryReq;
import com.imaging.scheduler.system.mapper.scene.SceneInfoMapper;
import com.imaging.scheduler.system.service.scene.SceneInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SceneInfoServiceImpl implements SceneInfoService {
    private final SceneInfoMapper sceneMapper;

    @Override
    public TableDataInfo<SceneInfo> getSceneList(SceneQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<SceneInfo> list = sceneMapper.selectList(req);
        PageInfo<SceneInfo> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(pageInfo.getTotal(), req.getPage(), req.getPageSize(), list);
    }

    @Override
    public SceneInfo getSceneById(Long id) {
        SceneInfo scene = sceneMapper.selectById(id);
        if (scene == null) throw new BusinessException(404, "场景不存在");
        return scene;
    }

    @Override
    public void addScene(SceneAddReq req) {
        SceneInfo scene = new SceneInfo();
        scene.setSceneName(req.getSceneName());
        scene.setSceneType(req.getSceneType());
        scene.setGroupId(req.getGroupId());
        scene.setOwnerId(req.getOwnerId());
        scene.setDescription(req.getDescription());
        scene.setStatus(req.getStatus() != null ? req.getStatus() : 1);
        scene.setIsDeleted(0);
        scene.setCreatedAt(LocalDateTime.now());
        sceneMapper.insert(scene);
    }

    @Override
    public void editScene(Long id, SceneAddReq req) {
        SceneInfo scene = getSceneById(id);
        scene.setSceneName(req.getSceneName());
        scene.setSceneType(req.getSceneType());
        scene.setGroupId(req.getGroupId());
        scene.setOwnerId(req.getOwnerId());
        scene.setDescription(req.getDescription());
        scene.setUpdatedAt(LocalDateTime.now());
        sceneMapper.update(scene);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SceneInfo scene = getSceneById(id);
        scene.setStatus(status);
        sceneMapper.update(scene);
    }

    @Override
    @Transactional
    public void deleteScene(Long id) {
        int deviceCount = sceneMapper.countDeviceBySceneId(id);
        if (deviceCount > 0) throw new BusinessException(422, "场景下有关联设备，无法删除");
        int taskCount = sceneMapper.countTaskBySceneId(id);
        if (taskCount > 0) throw new BusinessException(422, "场景下有关联任务，无法删除");
        sceneMapper.deleteById(id);
    }

    @Override
    public List<SceneInfo> getActiveScenes() {
        return sceneMapper.selectActiveScenes();
    }
}
