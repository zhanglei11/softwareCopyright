package com.sursoft.vision.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DefectImage {
    private Long id;
    private Long recordId;
    private String imageUrl;
    private String annotations;
    private LocalDateTime createdAt;
}
