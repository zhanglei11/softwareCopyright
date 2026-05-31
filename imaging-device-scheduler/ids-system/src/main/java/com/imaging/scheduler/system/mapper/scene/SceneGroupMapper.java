package com.imaging.scheduler.system.mapper.scene;

import com.imaging.scheduler.system.domain.scene.SceneGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SceneGroupMapper {
    List<SceneGroup> selectList();
    SceneGroup selectById(@Param("id") Long id);
    int insert(SceneGroup group);
    int update(SceneGroup group);
    int deleteById(@Param("id") Long id);
    int countSceneByGroupId(@Param("groupId") Long groupId);
    int countByGroupName(@Param("groupName") String groupName, @Param("excludeId") Long excludeId);
}
