package com.todolist.dto;

import com.todolist.enums.ReminderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 提醒响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponseDTO {

    private Long id;
    private ReminderType type;
    private LocalDateTime remindAt;
    private Boolean isSent;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
