package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.todolist.enums.ReminderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 提醒实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("reminder")
public class Reminder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private ReminderType type;

    private LocalDateTime remindAt;

    private Boolean isSent = false;

    private LocalDateTime sentAt;

    private Long todoId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private TodoItem todoItem;
}
