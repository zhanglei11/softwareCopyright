package com.imaging.scheduler.system.mapper.scene;

import com.imaging.scheduler.system.domain.scene.SceneInfo;
import com.imaging.scheduler.system.dto.req.SceneQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SceneInfoMapper {
    List<SceneInfo> selectList(SceneQueryReq req);
    SceneInfo selectById(@Param("id") Long id);
    int insert(SceneInfo scene);
    int update(SceneInfo scene);
    int deleteById(@Param("id") Long id);
    int countDeviceBySceneId(@Param("sceneId") Long sceneId);
    int countTaskBySceneId(@Param("sceneId") Long sceneId);
    List<SceneInfo> selectActiveScenes();
}
