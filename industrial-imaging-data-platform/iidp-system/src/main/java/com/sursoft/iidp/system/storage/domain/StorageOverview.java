package com.sursoft.iidp.system.storage.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "存储空间总览")
public class StorageOverview {
    private Long totalBytes;
    private Long usedBytes;
    private Long freeBytes;
    private Double usageRate;
    private LocalDateTime snapshotTime;
    private List<Map<String, Object>> dirStats;
}
