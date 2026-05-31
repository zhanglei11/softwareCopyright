package com.imaging.scheduler.system.service.scene;

import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.scene.SceneInfo;
import com.imaging.scheduler.system.dto.req.*;

import java.util.List;

public interface SceneInfoService {
    TableDataInfo<SceneInfo> getSceneList(SceneQueryReq req);
    SceneInfo getSceneById(Long id);
    void addScene(SceneAddReq req);
    void editScene(Long id, SceneAddReq req);
    void updateStatus(Long id, Integer status);
    void deleteScene(Long id);
    List<SceneInfo> getActiveScenes();
}
