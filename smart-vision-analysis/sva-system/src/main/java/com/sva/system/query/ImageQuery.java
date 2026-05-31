package com.sva.system.query;
import lombok.Data;
@Data
public class ImageQuery {
    private Long categoryId;
    private String fileName;
    private Integer recognitionStatus;
    private String startTime;
    private String endTime;
}
