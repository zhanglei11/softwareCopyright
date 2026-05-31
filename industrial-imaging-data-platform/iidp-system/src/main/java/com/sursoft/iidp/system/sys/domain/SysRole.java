package com.sursoft.iidp.system.sys.domain;

import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色")
public class SysRole extends BaseEntity {
    private Long id;
    private String roleName;
    private String roleKey;
    private Integer sortOrder;
    private Integer status;
    private String remark;
    private List<Long> menuIds;
}
