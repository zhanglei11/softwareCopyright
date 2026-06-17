package com.angu.ai.system.domain.query;

import com.angu.ai.common.core.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询参数")
public class UserQuery extends PageQuery {
    @Schema(description = "姓名/账号关键字")
    private String keyword;
    @Schema(description = "状态：0 禁用 / 1 启用")
    private Integer status;
    @Schema(description = "角色 ID")
    private Long roleId;
}
