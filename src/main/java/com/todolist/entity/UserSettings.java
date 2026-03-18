package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.todolist.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户设置实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_settings")
public class UserSettings {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Theme theme = Theme.LIGHT;

    private Integer fontSize = 14;

    private Boolean showTitle = true;

    private Boolean showDueDate = true;

    private Boolean showPriority = true;

    private Boolean showTags = true;

    private Boolean reminderWorkHoursOnly = true;

    private Boolean reminderWorkDaysOnly = true;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private User user;
}
