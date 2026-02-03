package com.todolist.dto;

import com.todolist.enums.ReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 提醒创建DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderCreateDTO {

    @NotNull(message = "提醒类型不能为空")
    private ReminderType type;

    @NotNull(message = "提醒时间不能为空")
    private LocalDateTime remindAt;
}
