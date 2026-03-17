package com.todolist.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签创建DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagCreateDTO {

    @NotBlank(message = "标签名称不能为空")
    private String name;

    private String color;
}
