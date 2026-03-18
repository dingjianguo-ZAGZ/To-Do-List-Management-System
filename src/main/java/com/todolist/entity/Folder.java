package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件夹实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("folders")
public class Folder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Set<TodoItem> todoItems = new HashSet<>();
}
