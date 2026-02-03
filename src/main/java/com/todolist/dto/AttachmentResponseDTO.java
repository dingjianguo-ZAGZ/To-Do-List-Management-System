package com.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 附件响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponseDTO {

    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private LocalDateTime uploadedAt;
}
