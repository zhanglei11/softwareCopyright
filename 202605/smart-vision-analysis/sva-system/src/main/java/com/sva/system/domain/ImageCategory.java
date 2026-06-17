package com.sva.system.domain;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImageCategory {
    private Long id;
    private String categoryName;
    private Long parentId;
    private Integer sortOrder;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Integer deleted;
    private List<ImageCategory> children;
    // 关联展示字段
    private Integer imageCount;
    private String parentName;
}
