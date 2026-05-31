package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class TaskAssignReq {
    @NotEmpty(message = "设备列表不能为空")
    private List<Long> deviceIds;
}
