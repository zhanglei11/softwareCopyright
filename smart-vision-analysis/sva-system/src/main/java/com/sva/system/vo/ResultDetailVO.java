package com.sva.system.vo;
import com.sva.system.domain.RecognitionBox;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ResultDetailVO {
    private Long id;
    private Long taskId;
    private Long imageId;
    private String imageNo;
    private String fileName;
    private String imageUrl;
    private Integer reviewStatus;
    private String reviewStatusDesc;
    private LocalDateTime reviewedAt;
    private List<RecognitionBox> boxes;
}
