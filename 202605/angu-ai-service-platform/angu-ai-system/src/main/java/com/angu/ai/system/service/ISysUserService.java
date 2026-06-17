package com.angu.ai.system.service;

import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.system.domain.dto.UserCreateDTO;
import com.angu.ai.system.domain.query.UserQuery;
import com.angu.ai.system.domain.vo.UserVO;

import java.util.Map;

public interface ISysUserService {
    TableDataInfo<UserVO> pageList(UserQuery query);
    UserVO getById(Long id);
    void create(UserCreateDTO dto);
    void update(Long id, UserCreateDTO dto);
    void deleteById(Long id);
    void resetPassword(Long id);
    void updateStatus(Long id, Integer status);
    Map<String, Object> getUserUsage(Long id);
}
