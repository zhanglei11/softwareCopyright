package com.imaging.scheduler.system.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "设备参数保存请求")
public class DeviceParamReq {
    @NotNull
    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "参数列表")
    private List<ParamItem> params;

    @Data
    public static class ParamItem {
        @Schema(description = "参数Key")
        private String paramKey;
        @Schema(description = "参数值")
        private String paramValue;
        @Schema(description = "参数描述")
        private String paramDesc;
    }
}
