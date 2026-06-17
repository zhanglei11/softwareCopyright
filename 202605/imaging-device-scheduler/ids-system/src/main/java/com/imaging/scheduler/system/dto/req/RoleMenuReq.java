package com.imaging.scheduler.system.dto.req;

import lombok.Data;
import java.util.List;

@Data
public class RoleMenuReq {
    private List<Long> menuIds;
}
