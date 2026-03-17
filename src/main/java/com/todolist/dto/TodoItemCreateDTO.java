package com.todolist.dto;

import com.todolist.enums.Priority;
import com.todolist.enums.RepeatCycle;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 创建待办事项DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemCreateDTO {

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private Priority priority = Priority.MEDIUM;

    private LocalDateTime dueDate;

    private RepeatCycle repeatCycle = RepeatCycle.NONE;

    private Long folderId;

    private Set<Long> tagIds;

    private Set<ReminderCreateDTO> reminders;
}
