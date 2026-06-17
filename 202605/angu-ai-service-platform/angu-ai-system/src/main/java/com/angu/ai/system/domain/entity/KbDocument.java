package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "知识库文档")
public class KbDocument extends BaseEntity {

    @Schema(description = "知识库 ID")
    private Long kbId;

    @Schema(description = "原始文件名")
    private String fileName;

    @Schema(description = "存储路径")
    private String filePath;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "文件类型：pdf / docx / txt / md")
    private String fileType;

    @Schema(description = "解析状态：PENDING / PROCESSING / DONE / FAILED")
    private String parseStatus;

    @Schema(description = "切分片段数")
    private Integer chunkCount;

    @Schema(description = "解析失败原因")
    private String errorMsg;

    @Schema(description = "是否删除：0 正常 / 1 已删除")
    private Integer deleted;
}
