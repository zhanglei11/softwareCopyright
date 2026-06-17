package com.imaging.scheduler.admin.controller.device;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.framework.web.BaseController;
import com.imaging.scheduler.system.domain.device.DeviceInfo;
import com.imaging.scheduler.system.dto.req.DeviceAddReq;
import com.imaging.scheduler.system.dto.req.DeviceQueryReq;
import com.imaging.scheduler.system.service.device.DeviceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "设备管理", description = "设备信息CRUD及状态管理")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceInfoController extends BaseController {

    private final DeviceInfoService deviceInfoService;

    @Operation(summary = "设备分页列表")
    @PreAuthorize("hasAuthority('device:info:list')")
    @GetMapping
    public TableDataInfo<DeviceInfo> list(DeviceQueryReq req) {
        return deviceInfoService.getDeviceList(req);
    }

    @Operation(summary = "设备详情")
    @PreAuthorize("hasAuthority('device:info:list')")
    @GetMapping("/{id}")
    public AjaxResult<DeviceInfo> detail(@PathVariable("id") Long id) {
        return AjaxResult.success(deviceInfoService.getDeviceById(id));
    }

    @Operation(summary = "新增设备")
    @PreAuthorize("hasAuthority('device:info:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody DeviceAddReq req) {
        deviceInfoService.addDevice(req, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "编辑设备")
    @PreAuthorize("hasAuthority('device:info:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody DeviceAddReq req) {
        deviceInfoService.editDevice(id, req, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "设备状态变更")
    @PreAuthorize("hasAuthority('device:info:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, Integer> body) {
        deviceInfoService.updateDeviceStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "删除设备")
    @PreAuthorize("hasAuthority('device:info:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        deviceInfoService.deleteDevice(id, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "获取可用设备列表")
    @PreAuthorize("hasAuthority('device:info:list')")
    @GetMapping("/available")
    public AjaxResult<List<DeviceInfo>> available(
            @RequestParam(name = "sceneId", required = false) Long sceneId,
            @RequestParam(name = "deviceType", required = false) Integer deviceType) {
        return AjaxResult.success(deviceInfoService.getAvailableDevices(sceneId, deviceType));
    }

    @Operation(summary = "设备状态总览")
    @PreAuthorize("hasAuthority('device:info:list')")
    @GetMapping("/status-overview")
    public AjaxResult<Map<String, Object>> statusOverview() {
        return AjaxResult.success(deviceInfoService.getStatusOverview());
    }
}
