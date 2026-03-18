package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String email;

    private String phone;

    private String password;

    private String avatarUrl;

    private Boolean isActive = true;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private UserSettings settings;

    @TableField(exist = false)
    private Set<TodoItem> todoItems = new HashSet<>();

    @TableField(exist = false)
    private Set<Tag> tags = new HashSet<>();

    @TableField(exist = false)
    private Set<Folder> folders = new HashSet<>();
}
