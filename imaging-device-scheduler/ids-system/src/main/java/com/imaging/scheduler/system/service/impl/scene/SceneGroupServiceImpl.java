package com.imaging.scheduler.system.service.impl.scene;

import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.scene.SceneGroup;
import com.imaging.scheduler.system.dto.req.SceneGroupAddReq;
import com.imaging.scheduler.system.mapper.scene.SceneGroupMapper;
import com.imaging.scheduler.system.service.scene.SceneGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SceneGroupServiceImpl implements SceneGroupService {
    private final SceneGroupMapper groupMapper;

    @Override
    public List<SceneGroup> getGroupList() {
        return groupMapper.selectList();
    }

    @Override
    public void addGroup(SceneGroupAddReq req) {
        int count = groupMapper.countByGroupName(req.getGroupName(), null);
        if (count > 0) throw new BusinessException(409, "分组名称已存在");
        SceneGroup group = new SceneGroup();
        group.setGroupName(req.getGroupName());
        group.setRemark(req.getRemark());
        group.setIsDeleted(0);
        group.setCreatedAt(LocalDateTime.now());
        groupMapper.insert(group);
    }

    @Override
    public void editGroup(Long id, SceneGroupAddReq req) {
        SceneGroup group = groupMapper.selectById(id);
        if (group == null) throw new BusinessException(404, "分组不存在");
        int count = groupMapper.countByGroupName(req.getGroupName(), id);
        if (count > 0) throw new BusinessException(409, "分组名称已存在");
        group.setGroupName(req.getGroupName());
        group.setRemark(req.getRemark());
        group.setUpdatedAt(LocalDateTime.now());
        groupMapper.update(group);
    }

    @Override
    public void deleteGroup(Long id) {
        int count = groupMapper.countSceneByGroupId(id);
        if (count > 0) throw new BusinessException(422, "分组下有关联场景，无法删除");
        groupMapper.deleteById(id);
    }
}
