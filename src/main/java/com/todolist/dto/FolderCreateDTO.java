package com.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件夹创建DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderCreateDTO {

    @NotBlank(message = "文件夹名称不能为空")
    private String name;

    private String description;
}
