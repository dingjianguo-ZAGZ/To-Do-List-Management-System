package com.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 标签响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {

    private Long id;
    private String name;
    private String color;
    private Integer todoCount;
    private LocalDateTime createdAt;
}
