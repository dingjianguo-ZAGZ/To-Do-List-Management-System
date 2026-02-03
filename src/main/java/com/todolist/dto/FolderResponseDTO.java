package com.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件夹响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Integer todoCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
