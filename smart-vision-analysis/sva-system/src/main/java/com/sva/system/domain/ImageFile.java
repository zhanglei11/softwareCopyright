package com.sva.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ImageFile {
    private Long id;
    private String imageNo;
    private String fileName;
    private String filePath;
    private String fileFormat;
    private Long fileSize;
    private Long categoryId;
    private Integer recognitionStatus;
    private String remark;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
    private Integer deleted;
}
