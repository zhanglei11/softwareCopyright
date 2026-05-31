package com.imaging.scheduler.system.dto.req;

import com.imaging.scheduler.common.core.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryReq extends PageQuery {
    private String username;
    private String realName;
    private Integer status;
}
