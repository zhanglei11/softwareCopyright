package com.imaging.scheduler.system.service.scene;

import com.imaging.scheduler.system.domain.scene.SceneGroup;
import com.imaging.scheduler.system.dto.req.SceneGroupAddReq;

import java.util.List;

public interface SceneGroupService {
    List<SceneGroup> getGroupList();
    void addGroup(SceneGroupAddReq req);
    void editGroup(Long id, SceneGroupAddReq req);
    void deleteGroup(Long id);
}
