package com.imaging.scheduler.admin.controller.device;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.system.domain.device.DeviceParam;
import com.imaging.scheduler.system.dto.req.DeviceParamReq;
import com.imaging.scheduler.system.service.device.DeviceParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "设备参数管理")
@RestController
@RequestMapping("/api/v1/devices/{deviceId}/params")
@RequiredArgsConstructor
public class DeviceParamController {

    private final DeviceParamService deviceParamService;

    @Operation(summary = "获取设备参数")
    @PreAuthorize("hasAuthority('device:param:list')")
    @GetMapping
    public AjaxResult<List<DeviceParam>> list(@PathVariable Long deviceId) {
        return AjaxResult.success(deviceParamService.getParamsByDeviceId(deviceId));
    }

    @Operation(summary = "保存设备参数(全量替换)")
    @PreAuthorize("hasAuthority('device:param:edit')")
    @PutMapping
    public AjaxResult<Void> save(@PathVariable Long deviceId, @RequestBody DeviceParamReq req) {
        deviceParamService.saveParams(deviceId, req);
        return AjaxResult.success();
    }
}
