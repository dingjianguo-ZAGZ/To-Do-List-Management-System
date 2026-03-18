package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 统计实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("statistics")
public class Statistics {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate statDate;

    private Integer completedCount = 0;

    private Integer overdueCount = 0;

    private Integer createdCount = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private User user;
}
